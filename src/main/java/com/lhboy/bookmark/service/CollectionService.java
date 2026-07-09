package com.lhboy.bookmark.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lhboy.bookmark.dto.CreateCollectionRequest;
import com.lhboy.bookmark.dto.UpdateCollectionRequest;
import com.lhboy.bookmark.entity.Collection;
import com.lhboy.bookmark.vo.CollectionResponse;
import com.lhboy.bookmark.vo.CollectionTreeResponse;

import java.util.List;

public interface CollectionService extends IService<Collection> {
    List<CollectionResponse> listByCurrentUser();
    List<CollectionTreeResponse> treeByCurrentUser();
    CollectionResponse create(CreateCollectionRequest request);
    CollectionResponse update(Long id, UpdateCollectionRequest request);
    void delete(Long id);
    Collection getOwnedCollection(Long id);
}
