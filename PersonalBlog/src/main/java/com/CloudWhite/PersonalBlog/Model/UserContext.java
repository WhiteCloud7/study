package com.CloudWhite.PersonalBlog.Model;

import com.CloudWhite.PersonalBlog.Entity.DTO.token;

public class UserContext {
    private static final ThreadLocal<token> USER_HOLDER = new ThreadLocal<>();

    public static void setCurrentToken(token token) {
        USER_HOLDER.set(token);
    }

    public static token getCurrentToken() {
        return USER_HOLDER.get();
    }

    public static void clear() {
        USER_HOLDER.remove();
    }
}
