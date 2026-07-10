package com.lhboy.bookmark.controller;

import com.lhboy.bookmark.common.PageResult;
import com.lhboy.bookmark.common.Result;
import com.lhboy.bookmark.dto.CreateBookmarkRequest;
import com.lhboy.bookmark.dto.UpdateBookmarkRequest;
import com.lhboy.bookmark.service.BookmarkService;
import com.lhboy.bookmark.vo.BookmarkResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "书签")
@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @ApiOperation("分页列表")
    @GetMapping
    public Result<PageResult<BookmarkResponse>> list(
            @RequestParam(required = false) Long collectionId,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Boolean favorite,
            @RequestParam(required = false) Boolean archived,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return Result.success(bookmarkService.list(collectionId, q, favorite, archived, page, size));
    }

    @ApiOperation("详情")
    @GetMapping("/{id}")
    public Result<BookmarkResponse> detail(@PathVariable Long id) {
        return Result.success(bookmarkService.getDetail(id));
    }

    @ApiOperation("创建")
    @PostMapping
    public Result<BookmarkResponse> create(@Valid @RequestBody CreateBookmarkRequest request) {
        return Result.success(bookmarkService.create(request));
    }

    @ApiOperation("更新")
    @PutMapping("/{id}")
    public Result<BookmarkResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateBookmarkRequest request) {
        return Result.success(bookmarkService.update(id, request));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bookmarkService.delete(id);
        return Result.success();
    }
}
