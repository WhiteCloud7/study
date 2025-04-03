package com.WhiteCloud.PersonalBlog.Dao;

import com.WhiteCloud.PersonalBlog.Entity.userInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface userDao extends JpaRepository<userInfo,Integer> {
    public List<userInfo> findAllByUserId(int userId);
}
