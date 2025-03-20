package com.WhiteCloud.SpringBootTest.Dao;

import com.WhiteCloud.SpringBootTest.Entity.userInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface userDao {
    @Select("select * from userinfo")
    public List<userInfo> selectAll();

    public List<userInfo> selectBySomeCondition(String[] ParaList);

    @Update("update userinfo set username = #{username} where userId = #{userId}")
    public void updateUsernameById(@Param("username") String username,@Param("userId") int userId);
}
