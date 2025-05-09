package com.CloudWhite.PersonalBlog.Dao;

import com.CloudWhite.PersonalBlog.Entity.user;
import org.apache.ibatis.annotations.*;

@Mapper
public interface userDaoMybatis {
    @Insert("INSERT INTO user (username, password, role_id, nike_name) " +
            "VALUES (#{username}, #{password}, #{roleId}, #{nikeName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void insertUser(user user);
    @Update("UPDATE user SET nike_name = #{nikeName} WHERE user_id = #{userId}")
    void setDefaultNikeName(String nikeName,int userId);
    @Insert("INSERT INTO friendlist (`user_id`,friend_id) VALUES (#{userId},1)")
    void setAuthor(int userId);
    @Insert("INSERT INTO message (sender_name,receiver_name,message) VALUES ('liuwenjie',#{username},#{message})")
    void setDefaultMessage(String username,String message);
    @Update("UPDATE user SET avatar_src = #{avatarSrc} WHERE user_id = #{userId}")
    void setUserAvatar(String avatarSrc,int userId);
    @Update("UPDATE user " +
            "SET nike_name = #{nikeName}, " +
            "sex = #{sex}, " +
            "birthday = #{birthday}, " +
            "phone = #{phone}, " +
            "qq = #{qq}, " +
            "wechat = #{wechat}, " +
            "school = #{school} " +
            "WHERE user_id = #{userId}")
    int updateUserProfile(user user);
}
