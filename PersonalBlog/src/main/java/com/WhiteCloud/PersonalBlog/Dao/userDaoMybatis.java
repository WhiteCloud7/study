package com.WhiteCloud.PersonalBlog.Dao;

import com.WhiteCloud.PersonalBlog.Entity.userInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface userDaoMybatis {
    @Select("select * from userinfo")
    public List<userInfo> selectAll();

    @Select("select * from userinfo where userId = #{userId}")
    public userInfo selectById(int userId);

    public void updateUserInfo(userInfo userInfo);
}
