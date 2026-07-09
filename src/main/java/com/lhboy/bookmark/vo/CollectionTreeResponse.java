package com.lhboy.bookmark.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@ApiModel("收藏夹树")
@Data
@EqualsAndHashCode(callSuper = true)
public class CollectionTreeResponse extends CollectionResponse {

    private List<CollectionTreeResponse> children = new ArrayList<>();

    public static CollectionTreeResponse from(CollectionResponse response) {
        CollectionTreeResponse result = new CollectionTreeResponse();
        result.setId(response.getId());
        result.setParentId(response.getParentId());
        result.setName(response.getName());
        result.setSortOrder(response.getSortOrder());
        result.setCreatedAt(response.getCreatedAt());
        result.setUpdatedAt(response.getUpdatedAt());
        return result;
    }
}
