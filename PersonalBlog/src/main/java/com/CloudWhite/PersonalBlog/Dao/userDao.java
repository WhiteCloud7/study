package com.CloudWhite.PersonalBlog.Dao;

import com.CloudWhite.PersonalBlog.Entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userDao extends JpaRepository<user,Integer> {
    public user getByUserId(int userId);
}
