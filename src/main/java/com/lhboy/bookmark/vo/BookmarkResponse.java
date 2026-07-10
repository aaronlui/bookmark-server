package com.lhboy.bookmark.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ApiModel("书签信息")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponse {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("收藏夹ID")
    private Long collectionId;

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("封面地址")
    private String coverUrl;

    @ApiModelProperty("图标")
    private String faviconUrl;

    @ApiModelProperty("是否星标")
    private Boolean isFavorite;

    @ApiModelProperty("是否归档")
    private Boolean isArchived;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedAt;
}
