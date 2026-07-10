package com.lhboy.bookmark.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhboy.bookmark.common.PageResult;
import com.lhboy.bookmark.common.ResultCode;
import com.lhboy.bookmark.common.UserContext;
import com.lhboy.bookmark.dto.CreateBookmarkRequest;
import com.lhboy.bookmark.dto.UpdateBookmarkRequest;
import com.lhboy.bookmark.entity.Bookmark;
import com.lhboy.bookmark.exception.BusinessException;
import com.lhboy.bookmark.mapper.BookmarkMapper;
import com.lhboy.bookmark.service.BookmarkService;
import com.lhboy.bookmark.service.CollectionService;
import com.lhboy.bookmark.vo.BookmarkResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Service
public class BookmarkServiceImpl extends ServiceImpl<BookmarkMapper, Bookmark> implements BookmarkService {

    private final CollectionService collectionService;
    public BookmarkServiceImpl(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @Override
    public PageResult<BookmarkResponse> list(Long collectionId, String q, Boolean favorite, Boolean archived, int page, int size) {
        Long userId = UserContext.requireUserId();

        Page<Bookmark> mPage = lambdaQuery()
                .eq(Bookmark::getUserId, userId)
                .eq(collectionId != null, Bookmark::getCollectionId, normalizeCollectionId(collectionId))
                .eq(Boolean.TRUE.equals(favorite), Bookmark::getIsFavorite, true)
                .eq(Bookmark::getIsArchived, Boolean.TRUE.equals(archived))
                .and(StringUtils.hasText(q), w -> w
                        .like(Bookmark::getTitle, q)
                        .or().like(Bookmark::getUrl, q)
                        .or().like(Bookmark::getNote, q))
                .orderByDesc(Bookmark::getUpdatedAt)
                .page(new Page<>(page, size));
        Page<BookmarkResponse> mapped = new Page<>(mPage.getCurrent(), mPage.getSize(), mPage.getTotal());
        mapped.setRecords(mPage.getRecords().stream().map(this::toResponse).collect(Collectors.toList()));
        return PageResult.of(mapped);
    }

    @Override
    public BookmarkResponse create(CreateBookmarkRequest request) {
        Long userId = UserContext.requireUserId();
        Long collectionId = normalizeCollectionId(request.getCollectionId());

        validateCollectionOwnership(collectionId);

        Bookmark bookmark = new Bookmark();
        bookmark.setUserId(userId);
        bookmark.setCollectionId(collectionId);
        bookmark.setUrl(request.getUrl());
        bookmark.setTitle(request.getTitle());
        bookmark.setDescription(request.getDescription());
        bookmark.setNote(request.getNote());
        bookmark.setIsFavorite(Boolean.TRUE.equals(request.getIsFavorite()));
        bookmark.setIsArchived(false);

        save(bookmark);
        return toResponse(bookmark);
    }

    @Override
    public BookmarkResponse getDetail(Long id) {
        return toResponse(getOwnedBookmark(id));
    }

    @Override
    public BookmarkResponse update(Long id, UpdateBookmarkRequest request) {
        Bookmark bookmark = getOwnedBookmark(id);

        if (request.getTitle() != null) {
            bookmark.setTitle(request.getTitle());
        }
        if (request.getUrl() != null) {
            bookmark.setUrl(request.getUrl());
        }
        if (request.getDescription() != null) {
            bookmark.setDescription(request.getDescription());
        }
        if (request.getNote() != null) {
            bookmark.setNote(request.getNote());
        }
        if (request.getIsFavorite() != null) {
            bookmark.setIsFavorite(request.getIsFavorite());
        }
        if (request.getIsArchived() != null) {
            bookmark.setIsArchived(request.getIsArchived());
        }
        updateById(bookmark);
        return toResponse(bookmark);
    }

    @Override
    public void delete(Long id) {
        getOwnedBookmark(id);
        removeById(id);
    }

    @Override
    public Bookmark getOwnedBookmark(Long id) {
        Long userId = UserContext.requireUserId();
        Bookmark bookmark = getById(id);
        if (bookmark == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        if (!bookmark.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        return bookmark;
    }

    @Override
    public boolean existsByCollectionId(Long userId, Long collectionId) {
        return lambdaQuery()
                .eq(Bookmark::getUserId, userId)
                .eq(Bookmark::getCollectionId, collectionId)
                .exists();
    }

    private Long normalizeCollectionId(Long collectionId) {
        if (collectionId != null && collectionId == 0L) {
            return null;
        }
        return collectionId;
    }

    private void validateCollectionOwnership(Long collectionId) {
        Long normalized = normalizeCollectionId(collectionId);
        if (normalized != null) {
            collectionService.getOwnedCollection(normalized);
        }
    }

    private BookmarkResponse toResponse(Bookmark bookmark) {
        return BookmarkResponse.builder()
                .id(bookmark.getId())
                .collectionId(bookmark.getCollectionId())
                .url(bookmark.getUrl())
                .title(bookmark.getTitle())
                .description(bookmark.getDescription())
                .note(bookmark.getNote())
                .coverUrl(bookmark.getCoverUrl())
                .faviconUrl(bookmark.getFaviconUrl())
                .isFavorite(bookmark.getIsFavorite())
                .isArchived(bookmark.getIsArchived())
                .createdAt(bookmark.getCreatedAt())
                .updatedAt(bookmark.getUpdatedAt())
                .build();
    }
}
