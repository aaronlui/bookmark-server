package com.lhboy.bookmark.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("更新书签")
@Data
public class UpdateBookmarkRequest {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("笔记")
    private String note;

    @ApiModelProperty("是否星标")
    private Boolean isFavorite;

    @ApiModelProperty("是否归档")
    private Boolean isArchived;
}
