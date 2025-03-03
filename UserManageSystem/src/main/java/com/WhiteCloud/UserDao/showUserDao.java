package com.WhiteCloud.UserDao;

import com.WhiteCloud.UserEneity.userInfo;
import com.WhiteCloud.UserServer.searchUserServer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class showUserDao {
    public static void main(String arg[]){
//        testShowUser();
//        testSearchUserServer("冯");
//        testselectByOtherInfo(null,"186",null);
    }

    public static void testShowUser(){
        searchUserServer sus = new searchUserServer();
        List<userInfo> userInfo = sus.showUser();
        if(userInfo != null && !userInfo.isEmpty()){
            for(userInfo us : userInfo){
                System.out.println(us.getUsername());
                System.out.println(us.getSex());
                System.out.println(us.getPhone());
                System.out.println(us.getEmail());
            }
        }else{
            System.out.println("找不到数据！");
        }
    }

    public static void testSearchUserServer(String name){
        searchUserServer sus = new searchUserServer();
        List<userInfo> userInfo = sus.selectByUsername(name);
        if(userInfo != null && !userInfo.isEmpty()){
            for(userInfo us : userInfo){
                System.out.println(us.getUsername());
                System.out.println(us.getSex());
                System.out.println(us.getPhone());
                System.out.println(us.getEmail());
            }
        }else{
            System.out.println("找不到数据！");
        }
    }

    public static void testselectByOtherInfo(String sex, String phone, String email){
        searchUserServer sus = new searchUserServer();
        List<userInfo> userInfo = sus.selectByOtherInfo(sex,phone,email);
        if(userInfo != null && !userInfo.isEmpty()){
            for(userInfo us : userInfo){
                System.out.println(us.getUsername());
                System.out.println(us.getSex());
                System.out.println(us.getPhone());
                System.out.println(us.getEmail());
            }
        }else{
            System.out.println("找不到数据！");
        }
    }
}
