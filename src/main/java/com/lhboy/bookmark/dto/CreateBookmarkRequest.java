package com.lhboy.bookmark.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel("创建书签")
@Data
public class CreateBookmarkRequest {

    @NotBlank(message = "url不能为空")
    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("笔记")
    private String note;

    @ApiModelProperty("收藏夹ID")
    private Long collectionId;

    @ApiModelProperty("是否星标")
    private Boolean isFavorite;
}
