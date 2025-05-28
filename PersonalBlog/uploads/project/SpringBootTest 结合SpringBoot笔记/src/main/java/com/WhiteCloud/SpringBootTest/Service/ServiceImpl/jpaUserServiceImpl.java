package com.WhiteCloud.SpringBootTest.Service.ServiceImpl;

import com.WhiteCloud.SpringBootTest.Dao.jpaUserDao;
import com.WhiteCloud.SpringBootTest.Entity.userInfo;
import com.WhiteCloud.SpringBootTest.Service.jpaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class jpaUserServiceImpl implements jpaUserService {
    private jpaUserDao jpaUserDao;
    @Autowired
    public jpaUserServiceImpl(jpaUserDao jpaUserDao) {
        this.jpaUserDao = jpaUserDao;
    }

    public List<userInfo> findAll(){
        return jpaUserDao.findAll();
    };

    public List<userInfo> findByUserId(int id){
        return jpaUserDao.findByUserId(id);
    };

    public userInfo findByUsername(String username){
        return jpaUserDao.findByUsername(username);
    };

    public List<userInfo> findByEmailLikeAndSexLike(String email,String sex){
        return jpaUserDao.findByEmailLikeAndSexLike(email,sex);
    };

    public void save(userInfo userInfo){jpaUserDao.save(userInfo);};
}
