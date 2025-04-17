package com.CloudWhite.PersonalBlog.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface mybatisDao {
    @Update("LOCK TABLES message WRITE")
    public void lockMessageTable();

    @Select("call ResetMessageAutoIncrement()")
    public void ResetMessageAutoIncrement();

    @Update("UNLOCK TABLES")
    public void unlockMessageTable();
}
