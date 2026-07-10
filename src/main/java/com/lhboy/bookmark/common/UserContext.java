package com.lhboy.bookmark.common;

import com.lhboy.bookmark.exception.BusinessException;

public class UserContext {
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }
    public static Long getUserId() {
        return USER_ID.get();
    }
    public static Long requireUserId() {
        Long userId = USER_ID.get();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return userId;
    }
    public static void clear() {
        USER_ID.remove();
    }
}
