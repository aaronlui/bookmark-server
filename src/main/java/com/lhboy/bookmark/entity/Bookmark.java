package com.lhboy.bookmark.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("bookmarks")
public class Bookmark {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long collectionId;

    private String url;

    private String title;

    private String description;

    private String note;

    private String coverUrl;

    private String faviconUrl;

    private Boolean isFavorite;

    private Boolean isArchived;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
