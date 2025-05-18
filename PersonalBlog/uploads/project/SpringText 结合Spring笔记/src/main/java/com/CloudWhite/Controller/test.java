package com.CloudWhite.Controller;
import com.CloudWhite.Dao.testDao;
import com.CloudWhite.Service.ServiceImpl.testImpl;
import com.CloudWhite.model.UserInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
public class test{
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        testDao testDao = (testDao) context.getBean("testDao");
        testImpl testService = (testImpl) context.getBean("testService");
        try{
            List<UserInfo> userInfos = testService.selectAllUserInfo();
            for(UserInfo userinfo : userInfos){
                System.out.println(userinfo.getUsername());
                System.out.println(userinfo.getSex());
                System.out.println(userinfo.getPhone());
                System.out.println(userinfo.getEmail());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
