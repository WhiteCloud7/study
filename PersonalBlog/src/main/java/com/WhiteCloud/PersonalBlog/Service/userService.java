package com.WhiteCloud.PersonalBlog.Service;

import com.WhiteCloud.PersonalBlog.Entity.userInfo;

import java.util.List;

public interface userService {
    public List<userInfo> loadAllUser();

    public List<userInfo> loadAllUserByUserId(int userId);

    public List<userInfo> loadAllUser2();

    public userInfo loadAllUserByUserId2(int userId);

    public void updateUserInfo(userInfo userInfo);
}
