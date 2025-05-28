package com.WhiteCloud.SpringBootTest.Service.ServiceImpl;

import com.WhiteCloud.SpringBootTest.Dao.userDao;
import com.WhiteCloud.SpringBootTest.Entity.userInfo;
import com.WhiteCloud.SpringBootTest.Model.MyListenerEvent;
import com.WhiteCloud.SpringBootTest.Service.userService;
//import jakarta.jms.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class userServiceImpl implements userService {
    @Autowired
    ApplicationContext applicationContext;
    userDao userDao;
    @Autowired
    public userServiceImpl(userDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<userInfo> showAllUserInfo(){
        return userDao.selectAll();
    }
    @Override
    public List<userInfo> showAllUserInfo2(){
        MyListenerEvent myListenerEvent = new MyListenerEvent(this,"查询所有用户");
        applicationContext.publishEvent(myListenerEvent);
        return userDao.selectAll();
    };
    @Override
    public List<userInfo> showUserInfoBySomeCondition(String[] ParaList){
        return userDao.selectBySomeCondition(ParaList);
    };
    @Override
    @Transactional
    public void updateUsernameById(String username,int userId){
        userDao.updateUsernameById(username,userId);
        throw new RuntimeException();
    };
    @Override
    public Set<String> getPremissionInfoByUsername(String username) {return userDao.getRoleNameByUsername(username);};
    @Override
    public Set<String> getRoleNameByUsername(String username){
        System.out.println(username);
        return userDao.getPremissionInfoIdByUsername(username);};
    @Override
    public userInfo getUserInfoByUsername(String username){return userDao.selectByUsername(username);};
}
