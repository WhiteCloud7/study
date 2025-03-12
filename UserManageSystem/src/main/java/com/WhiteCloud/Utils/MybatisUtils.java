package com.WhiteCloud.Utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisUtils{
    private static SqlSessionFactory ssf;
    static{
        String config = "mybatis-config.xml";
        try{
            InputStream is = Resources.getResourceAsStream(config);
            ssf = new SqlSessionFactoryBuilder().build(is);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession(){
        return ssf.openSession();
    }
}
