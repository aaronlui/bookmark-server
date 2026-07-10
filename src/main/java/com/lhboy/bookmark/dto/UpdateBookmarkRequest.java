package com.lhboy.bookmark.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ApiModel("更新书签")
@Data
public class UpdateBookmarkRequest {

    @Size(max = 2048, message = "url长度不能超过2048")
    @Pattern(regexp = "^https?://.+", message = "url必须以http://或https://开头")
    @ApiModelProperty("url")
    private String url;

    @Size(max = 100, message = "标题长度不能超过100")
    @ApiModelProperty("标题")
    private String title;

    @Size(max = 500, message = "描述长度不能超过500")
    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("收藏夹ID")
    private Long collectionId;

    @ApiModelProperty("是否星标")
    private Boolean isFavorite;

    @ApiModelProperty("是否归档")
    private Boolean isArchived;
}
