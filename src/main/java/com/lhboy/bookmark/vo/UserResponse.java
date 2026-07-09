package com.lhboy.bookmark.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel("用户信息")
@Data
@Builder
public class UserResponse {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("名称")
    private String displayName;
}
