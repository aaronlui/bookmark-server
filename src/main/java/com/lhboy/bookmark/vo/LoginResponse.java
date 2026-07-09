package com.lhboy.bookmark.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel("登录响应")
@Data
@Builder
public class LoginResponse {

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("用户信息")
    private UserResponse user;
}
