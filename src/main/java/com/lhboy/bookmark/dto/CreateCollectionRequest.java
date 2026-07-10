package com.lhboy.bookmark.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ApiModel("创建收藏夹")
public class CreateCollectionRequest {

    @NotBlank(message = "名称不能为空")
    @Size(max = 100, message = "名称长度不能超过100")
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("父文件夹ID，不传则为根目录")
    private Long parentId;

    @ApiModelProperty("排序")
    @Min(0)
    private Integer sortOrder;
}
