package com.lhboy.bookmark.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lhboy.bookmark.common.PageResult;
import com.lhboy.bookmark.dto.CreateBookmarkRequest;
import com.lhboy.bookmark.dto.UpdateBookmarkRequest;
import com.lhboy.bookmark.entity.Bookmark;
import com.lhboy.bookmark.vo.BookmarkResponse;

public interface BookmarkService extends IService<Bookmark> {

    PageResult<BookmarkResponse> list(Long collectionId, String q, Boolean favorite, Boolean archived, int page, int size);
    BookmarkResponse getDetail(Long id);
    BookmarkResponse create(CreateBookmarkRequest request);
    BookmarkResponse update(Long id, UpdateBookmarkRequest request);
    void delete(Long id);
    Bookmark getOwnedBookmark(Long id);
    boolean existsByCollectionId(Long userId, Long collectionId);

}
