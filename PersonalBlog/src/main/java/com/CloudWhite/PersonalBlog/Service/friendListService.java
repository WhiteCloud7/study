package com.CloudWhite.PersonalBlog.Service;

import com.CloudWhite.PersonalBlog.Entity.friendList;

import java.util.List;

public interface friendListService {
    public List<String[]> getFriendBasicInfo(int userId);
}
