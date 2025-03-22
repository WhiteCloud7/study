package com.WhiteCloud.SpringBootTest.Service;

import com.WhiteCloud.SpringBootTest.Entity.userInfo;
//import jakarta.jms.Destination;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

public interface userService {

    public List<userInfo> showAllUserInfo();

    public List<userInfo> showAllUserInfo2();

    public List<userInfo> showUserInfoBySomeCondition(String[] ParaList);

    public void updateUsernameById(String username,int userId);

    public Set<String> getPremissionInfoByUsername(String username);

    public Set<String> getRoleNameByUsername(String username);

    public userInfo getUserInfoByUsername(String username);
}
