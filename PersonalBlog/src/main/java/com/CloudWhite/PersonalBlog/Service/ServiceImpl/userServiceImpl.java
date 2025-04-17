package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import com.CloudWhite.PersonalBlog.Dao.userDao;
import com.CloudWhite.PersonalBlog.Entity.user;
import com.CloudWhite.PersonalBlog.Service.userService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class userServiceImpl implements userService {
    userDao userDao;
    @Autowired
    public userServiceImpl(com.CloudWhite.PersonalBlog.Dao.userDao userDao) {
        this.userDao = userDao;
    }

    public List<user> findAll(){
        return userDao.findAll();
    }

    public user getUserByUserId(int userId){
        return userDao.getByUserId(userId);
    };
}
