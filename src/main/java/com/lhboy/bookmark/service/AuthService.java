package com.lhboy.bookmark.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lhboy.bookmark.dto.LoginRequest;
import com.lhboy.bookmark.dto.RegisterRequest;
import com.lhboy.bookmark.entity.User;
import com.lhboy.bookmark.vo.LoginResponse;
import com.lhboy.bookmark.vo.UserResponse;

public interface AuthService extends IService<User> {
    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    UserResponse me(Long userId);
}
