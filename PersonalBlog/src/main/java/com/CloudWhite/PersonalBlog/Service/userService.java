package com.CloudWhite.PersonalBlog.Service;

import com.CloudWhite.PersonalBlog.Entity.user;

import java.util.List;

public interface userService {
    public List<user> findAll();
    public user getUserByUserId(int userId);
}
