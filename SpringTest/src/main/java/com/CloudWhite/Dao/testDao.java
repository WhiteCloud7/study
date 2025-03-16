package com.CloudWhite.Dao;

import com.CloudWhite.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper//如果你不想用MapperScannerConfigurer可以用mapper注解，然后在启动类加上mapper扫描的注解，但这一般用在SpringBoot，后面再说
public interface testDao {
    List<UserInfo> selectAll();
}

