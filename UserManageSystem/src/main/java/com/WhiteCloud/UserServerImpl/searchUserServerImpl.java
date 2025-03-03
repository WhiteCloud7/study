package com.WhiteCloud.UserServerImpl;

import com.WhiteCloud.UserEneity.userInfo;

import java.util.List;

public interface searchUserServerImpl {
    public List<userInfo> showUser();
    public List<userInfo> selectByUsername(String name);
    public List<userInfo> selectByOtherInfo(String sex0,String phone0,String email0);
}
