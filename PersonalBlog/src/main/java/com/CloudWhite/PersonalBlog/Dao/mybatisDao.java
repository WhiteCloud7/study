package com.CloudWhite.PersonalBlog.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface mybatisDao {
    @Update("LOCK TABLES message WRITE")
    public void lockMessageTable();

    @Select("call ResetMessageAutoIncrement()")
    public void ResetMessageAutoIncrement();

    @Update("UNLOCK TABLES")
    public void unlockMessageTable();

    @Insert("INSERT INTO articleinfo (`user_id`,article_id) VALUES (#{userId},#{articleId})")
    void setArticleInfo(int userId,int articleId);
}
