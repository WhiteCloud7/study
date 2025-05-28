package com.WhiteCloud.SpringBootTest.Dao;

import com.WhiteCloud.SpringBootTest.Entity.role;
import com.WhiteCloud.SpringBootTest.Entity.userInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface jpaUserDao extends JpaRepository<userInfo,Integer> {
    public List<userInfo> findAll();

    public List<userInfo> findByUserId(int id);

    public userInfo findByUsername(String username);

    public List<userInfo> findByEmailLikeAndSexLike(String email,String sex);
}
