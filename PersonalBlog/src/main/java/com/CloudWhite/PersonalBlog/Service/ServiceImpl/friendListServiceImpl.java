package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import com.CloudWhite.PersonalBlog.Dao.friendListDao;
import com.CloudWhite.PersonalBlog.Entity.friendList;
import com.CloudWhite.PersonalBlog.Service.friendListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class friendListServiceImpl implements friendListService {
    private friendListDao friendListDao;
    @Autowired
    public friendListServiceImpl(friendListDao friendListDao) {
        this.friendListDao = friendListDao;
    }

    public List<String[]> getFriendBasicInfo(int userId) {
        return friendListDao.findFriendBasicInfo(userId);
    }

}
