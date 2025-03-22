package com.WhiteCloud.SpringBootTest.Dao;

import com.WhiteCloud.SpringBootTest.Entity.userInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

public interface userDao {
    @Select("select * from userinfo")
    public List<userInfo> selectAll();
    @Select("select * from userinfo where username = #{username}")
    public userInfo selectByUsername(String username);

    public List<userInfo> selectBySomeCondition(String[] ParaList);

    @Update("update userinfo set username = #{username} where userId = #{userId}")
    public void updateUsernameById(@Param("username") String username,@Param("userId") int userId);
    @Select("SELECT roleName FROM role WHERE roleId = (SELECT roleId FROM userinfo WHERE username = #{username})")
    public Set<String> getRoleNameByUsername(String username);
    @Select("SELECT premissionInfo FROM premission WHERE premissionId = (SELECT premissionId FROM role WHERE roleId = (SELECT roleId FROM userinfo WHERE username = #{username}))")
    public Set<String> getPremissionInfoIdByUsername(String username);
}
