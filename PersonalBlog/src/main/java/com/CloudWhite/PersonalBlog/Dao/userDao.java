package com.CloudWhite.PersonalBlog.Dao;

import com.CloudWhite.PersonalBlog.Entity.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface userDao extends JpaRepository<user,Integer> {
    public user findByUserId(int userId);
    public user findByUsername(String username);
    @Query("select u.password from user u where u.username = :username")
    public String findPasswordByUserName(String username);

    @Query("select u.userId from user u where u.username = :username")
    public int findUserIdByUserName(String username);
}
