package com.lhboy.bookmark.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ApiModel(value = "登录请求对象")
@Data
public class LoginRequest {

    @ApiModelProperty("邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email
    private String email;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
