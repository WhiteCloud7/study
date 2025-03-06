package com.WhiteCloud.Controller;

import com.WhiteCloud.UserDao.UserManage;
import com.WhiteCloud.UserEntity.userInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class MybatisUtils{
    private static SqlSessionFactory ssf;
    static{
        String resource = "mybatis-config.xml";
        try {
            InputStream is = Resources.getResourceAsStream(resource);
            ssf = new SqlSessionFactoryBuilder().build(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SqlSession getSqlSession(){
        return ssf.openSession();
    }
}

public class test{
    public static void main(String arg[]){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserManage userManage = sqlSession.getMapper(UserManage.class);
        List<userInfo> userInfos = userManage.selectByOtherInfo(null,null,null);
        for(userInfo userInfo : userInfos){
            System.out.println(userInfo.getUsername());
            System.out.println(userInfo.getSex());
            System.out.println(userInfo.getPhone());
            System.out.println(userInfo.getEmail());
        }
    }
}