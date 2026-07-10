package com.lhboy.bookmark.controller;

import com.lhboy.bookmark.common.Result;
import com.lhboy.bookmark.common.UserContext;
import com.lhboy.bookmark.dto.LoginRequest;
import com.lhboy.bookmark.dto.RegisterRequest;
import com.lhboy.bookmark.service.AuthService;
import com.lhboy.bookmark.vo.LoginResponse;
import com.lhboy.bookmark.vo.UserResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "认证")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public Result<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return Result.success(authService.register(request));
    }

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @ApiOperation(value = "当前用户")
    @GetMapping("/me")
    public Result<UserResponse> me() {
        return Result.success(authService.me(UserContext.requireUserId()));
    }
}
