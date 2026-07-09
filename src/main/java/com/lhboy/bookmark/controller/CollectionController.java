package com.lhboy.bookmark.controller;

import com.lhboy.bookmark.common.Result;
import com.lhboy.bookmark.dto.CreateCollectionRequest;
import com.lhboy.bookmark.dto.UpdateCollectionRequest;
import com.lhboy.bookmark.service.CollectionService;
import com.lhboy.bookmark.vo.CollectionResponse;
import com.lhboy.bookmark.vo.CollectionTreeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "收藏夹")
@RestController
@RequestMapping("/api/collections")
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @ApiOperation("列表")
    @GetMapping
    public Result<List<CollectionResponse>> list() {
        return Result.success(collectionService.listByCurrentUser());
    }

    @ApiOperation("树形列表")
    @GetMapping("/tree")
    public Result<List<CollectionTreeResponse>> tree() {
        return Result.success(collectionService.treeByCurrentUser());
    }

    @ApiOperation("创建")
    @PostMapping
    public Result<CollectionResponse> create(@Valid @RequestBody CreateCollectionRequest request) {
        return Result.success(collectionService.create(request));
    }

    @ApiOperation("更新")
    @PutMapping("/{id}")
    public Result<CollectionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCollectionRequest request) {
        return Result.success(collectionService.update(id, request));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        collectionService.delete(id);
        return Result.success();
    }
}
