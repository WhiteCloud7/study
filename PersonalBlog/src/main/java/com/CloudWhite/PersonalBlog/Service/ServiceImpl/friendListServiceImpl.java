package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import com.CloudWhite.PersonalBlog.Dao.friendListDao;
import com.CloudWhite.PersonalBlog.Dao.userDao;
import com.CloudWhite.PersonalBlog.Entity.DTO.userInfo;
import com.CloudWhite.PersonalBlog.Entity.friendList;
import com.CloudWhite.PersonalBlog.Entity.user;
import com.CloudWhite.PersonalBlog.Model.UserContext;
import com.CloudWhite.PersonalBlog.Service.friendListService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class friendListServiceImpl implements friendListService {
    private userDao userDao;
    private friendListDao friendListDao;
    @Autowired
    public friendListServiceImpl(friendListDao friendListDao,userDao userDao) {
        this.userDao = userDao;
        this.friendListDao = friendListDao;
    }

    public List<String[]> getFriendBasicInfo() {
        int userId = UserContext.getCurrentToken().getUserId();
        return friendListDao.findFriendBasicInfo(userId);
    }

    public userInfo getFriendBasicInfoByUsername(String friendName){
        user user = userDao.findByUsername(friendName);
        userInfo dto = new userInfo(user.getUsername(),user.getNikeName(), user.getSex(), user.getBirthday(), user.getPhone(),user.getQq(), user.getWechat(),  user.getSchool(),user.getAvatar_src());
        return dto;
    }
}
