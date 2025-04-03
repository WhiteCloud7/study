package com.WhiteCloud.PersonalBlog.Service.ServiceImpl;

import com.WhiteCloud.PersonalBlog.Dao.userDao;
import com.WhiteCloud.PersonalBlog.Dao.userDaoMybatis;
import com.WhiteCloud.PersonalBlog.Entity.userInfo;
import com.WhiteCloud.PersonalBlog.Service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class userServiceImpl implements userService {
    private userDao userDao;
    private userDaoMybatis userDaoMybatis;
    @Autowired
    public userServiceImpl(com.WhiteCloud.PersonalBlog.Dao.userDao userDao, com.WhiteCloud.PersonalBlog.Dao.userDaoMybatis userDaoMybatis) {
        this.userDao = userDao;
        this.userDaoMybatis = userDaoMybatis;
    }

    public List<userInfo> loadAllUser(){
        return userDao.findAll();
    };

    public List<userInfo> loadAllUserByUserId(int userId){
        return userDao.findAllByUserId(userId);
    };

    public List<userInfo> loadAllUser2(){
        return userDaoMybatis.selectAll();
    };

    public userInfo loadAllUserByUserId2(int userId){
        return userDaoMybatis.selectById(userId);
    };

    public void updateUserInfo(userInfo userInfo){
        userInfo oldUserInfo = userDaoMybatis.selectById(userInfo.getUserId());
        if(userInfo.getNikename()==null)
            userInfo.setNikename(oldUserInfo.getNikename());
        if(userInfo.getSex()==null)
            userInfo.setSex(oldUserInfo.getSex());
        if(userInfo.getPhone()==null)
            userInfo.setPhone(oldUserInfo.getPhone());
        if(userInfo.getEmail()==null)
            userInfo.setEmail(oldUserInfo.getEmail());
        userDaoMybatis.updateUserInfo(userInfo);
    }
}
