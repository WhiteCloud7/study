package com.WhiteCloud.SpringBootTest.Service;

import com.WhiteCloud.SpringBootTest.Entity.userInfo;

import java.util.List;

public interface jpaUserService {
    public List<userInfo> findAll();

    public List<userInfo> findByUserId(int id);

    public userInfo findByUsername(String username);

    public List<userInfo> findByEmailLikeAndSexLike(String email,String sex);

    public void save(userInfo userInfo);
}
