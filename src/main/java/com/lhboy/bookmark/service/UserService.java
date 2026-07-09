package com.lhboy.bookmark.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lhboy.bookmark.entity.User;

public interface UserService  extends IService<User> {
    User findByEmail(String email);
    User createUser(String email, String password, String displayName);
}
