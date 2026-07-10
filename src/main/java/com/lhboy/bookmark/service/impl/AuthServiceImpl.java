package com.lhboy.bookmark.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhboy.bookmark.common.ResultCode;
import com.lhboy.bookmark.dto.LoginRequest;
import com.lhboy.bookmark.dto.RegisterRequest;
import com.lhboy.bookmark.entity.User;
import com.lhboy.bookmark.exception.BusinessException;
import com.lhboy.bookmark.mapper.UserMapper;
import com.lhboy.bookmark.service.AuthService;
import com.lhboy.bookmark.service.UserService;
import com.lhboy.bookmark.util.JwtUtil;
import com.lhboy.bookmark.vo.LoginResponse;
import com.lhboy.bookmark.vo.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userService.findByEmail(request.getEmail()) != null) {
            throw new BusinessException(ResultCode.EMAIL_EXISTS);
        }

        User user = userService.createUser(request.getEmail(), request.getPassword(), request.getDisplayName());

        return toResponse(user);
    };

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user == null) {
            throw new BusinessException(ResultCode.INVALID_CREDENTIALS);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(ResultCode.INVALID_CREDENTIALS);
        }

        String token = jwtUtil.generateToken(user.getId());

        return LoginResponse.builder().token(token).user(toResponse(user)).build();
    }

    @Override
    public UserResponse me(Long userId) {
        User user = userService.getById(userId);
        if  (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    };
}
