package com.CloudWhite.PersonalBlog.Service;

import com.CloudWhite.PersonalBlog.Entity.DTO.userInfo;
import com.CloudWhite.PersonalBlog.Entity.friendList;
import com.CloudWhite.PersonalBlog.Entity.user;

import java.util.List;

public interface friendListService {
    public List<String[]> getFriendBasicInfo();
    public userInfo getFriendBasicInfoByUsername(String friendName);
}
