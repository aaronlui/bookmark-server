package com.lhboy.bookmark.common;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(0, "success"),
    // 通用 400xx
    BAD_REQUEST(40000, "请求参数错误"),
    VALIDATION_ERROR(40001, "参数校验失败"),
    UNAUTHORIZED(40100, "未登录或登录已过期"),
    FORBIDDEN(40300, "无权限访问"),
    NOT_FOUND(40400, "资源不存在"),
    // 用户 409xx
    EMAIL_EXISTS(40901, "邮箱已被注册"),
    INVALID_CREDENTIALS(40101, "邮箱或密码错误"),
    // 系统 500xx
    INTERNAL_ERROR(50000, "服务器内部错误");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
