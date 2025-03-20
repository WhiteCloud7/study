package com.WhiteCloud.SpringBootTest.Service;

import com.WhiteCloud.SpringBootTest.Entity.userInfo;

import java.util.List;

public interface userService {
    public List<userInfo> showAllUserInfo();

    public List<userInfo> showAllUserInfo2();

    public List<userInfo> showUserInfoBySomeCondition(String[] ParaList);

    public void updateUsernameById(String username,int userId);
}
