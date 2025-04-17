package com.CloudWhite.PersonalBlog.Dao;

import com.CloudWhite.PersonalBlog.Entity.friendList;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface friendListDao extends JpaRepository<friendList,Integer> {
    @Query(value = "SELECT u.nike_name, u.avatar_src FROM friendlist f JOIN user u ON f.friend_id = u.user_id WHERE f.user_id = :userId", nativeQuery = true)
    List<String[]> findFriendBasicInfo(@Param("userId") int userId);
}
