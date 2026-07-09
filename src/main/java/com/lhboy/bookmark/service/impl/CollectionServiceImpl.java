package com.lhboy.bookmark.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhboy.bookmark.common.ResultCode;
import com.lhboy.bookmark.common.UserContext;
import com.lhboy.bookmark.dto.CreateCollectionRequest;
import com.lhboy.bookmark.dto.UpdateCollectionRequest;
import com.lhboy.bookmark.entity.Collection;
import com.lhboy.bookmark.exception.BusinessException;
import com.lhboy.bookmark.mapper.CollectionMapper;
import com.lhboy.bookmark.service.CollectionService;
import com.lhboy.bookmark.vo.CollectionResponse;
import com.lhboy.bookmark.vo.CollectionTreeResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements CollectionService {
    @Override
    public List<CollectionResponse> listByCurrentUser() {
        Long userId = UserContext.getUserId();
        return lambdaQuery()
                .eq(Collection::getUserId, userId)
                .orderByAsc(Collection::getSortOrder)
                .orderByDesc(Collection::getUpdatedAt)
                .list()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CollectionTreeResponse> treeByCurrentUser() {
        List<CollectionResponse> list = listByCurrentUser();
        Map<Long, CollectionTreeResponse> map = new LinkedHashMap<>();

        for (CollectionResponse item : list) {
            CollectionTreeResponse node = CollectionTreeResponse.from(item);
            map.put(node.getId(), node);
        }

        List<CollectionTreeResponse> roots = new ArrayList<>();
        for (CollectionTreeResponse item : map.values()) {
            if (item.getParentId()  == null) {
                roots.add(item);
            } else {
                CollectionTreeResponse parent = map.get(item.getParentId());
                if (parent != null) {
                    parent.getChildren().add(item);
                } else {
                    roots.add(item);
                }
            }
        }
        return roots;
    }

    @Override
    public CollectionResponse create(CreateCollectionRequest request) {
        Long userId = UserContext.getUserId();

        Long parentId = request.getParentId();
        if (parentId != null && parentId == 0L) {
            parentId = null;
        }

        if (parentId != null) {
            getOwnedCollection(parentId);
        }

        checkNameDuplicate(userId, parentId, request.getName(), null);

        Collection collection = new Collection();
        collection.setUserId(userId);
        collection.setParentId(parentId);
        collection.setName(request.getName());
        collection.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);

        save(collection);
        return toResponse(collection);
    }

    @Override
    public CollectionResponse update(Long id, UpdateCollectionRequest request) {
        Collection collection = getOwnedCollection(id);

        if (StringUtils.hasText(request.getName())) {
            Long targetParentId = request.getParentId() != null ? request.getParentId() : collection.getParentId();

            checkNameDuplicate(collection.getUserId(), targetParentId, request.getName(), id);

            collection.setName(request.getName());
        }

        if (request.getParentId() != null) {
            Long newParentId;
            if (request.getParentId() == 0L) {
                newParentId = null;
            } else {
                newParentId = request.getParentId();
            }

            Long currentParentId = collection.getParentId();
            boolean parentChanged = (newParentId == null && currentParentId != null)
                    || (newParentId != null && !newParentId.equals(currentParentId));

            if (parentChanged) {
                if (newParentId != null) {
                    if (newParentId.equals(id)) {
                        throw new BusinessException(ResultCode.BAD_REQUEST, "不能将自己设为父文件夹");
                    }
                    getOwnedCollection(newParentId);
                    assertNotDescendant(id, newParentId);
                }

                checkNameDuplicate(collection.getUserId(), newParentId, collection.getName(), id);

                collection.setParentId(newParentId);
            }
        }

        if (request.getSortOrder() != null) {
            collection.setSortOrder(request.getSortOrder());
        }

        updateById(collection);
        return toResponse(collection);
    }

    @Override
    public void delete(Long id) {
        Collection collection = getOwnedCollection(id);

        boolean hasChildren = lambdaQuery()
                .eq(Collection::getUserId, collection.getUserId())
                .eq(Collection::getParentId, id)
                .exists();
        if (hasChildren) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "请先删除子文件夹");
        }

        removeById(id);
    }

    @Override
    public Collection getOwnedCollection(Long id) {
        Long userId = UserContext.getUserId();
        Collection collection = getById(id);
        if (collection == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (!collection.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        return collection;
    }
    
    private void assertNotDescendant(Long nodeId, Long newParentId) {
        Long current = newParentId;

        while (current != null) {
            if (current.equals(nodeId)) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "不能移动到子文件夹下");
            }
            Collection parent = getById(current);
            current = parent != null ? parent.getParentId() : null;
        }
    }

    /**
     * 检查同一用户、同一父目录下是否已有同名收藏夹
     * @param excludeId 更新时排除自身，创建时传 null
     */
    private void checkNameDuplicate(Long userId, Long parentId, String name, Long excludeId) {

        if (parentId != null && parentId == 0L) {
            parentId = null;
        }
        Long finalParentId = parentId;
        boolean exists = lambdaQuery()
                .eq(Collection::getUserId, userId)
                .eq(Collection::getName, name)
                .ne(excludeId != null, Collection::getId, excludeId)
                .func(wrapper -> {
                    if (finalParentId == null) {
                        wrapper.isNull(Collection::getParentId);
                    } else {
                        wrapper.eq(Collection::getParentId, finalParentId);
                    }
                })
                .exists();
        if (exists) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "收藏夹已存在");
        }
    }

    private CollectionResponse toResponse(Collection collection) {
        return CollectionResponse.builder()
                .id(collection.getId())
                .parentId(collection.getParentId())
                .name(collection.getName())
                .sortOrder(collection.getSortOrder())
                .createdAt(collection.getCreatedAt())
                .updatedAt(collection.getUpdatedAt())
                .build();
    }

}
