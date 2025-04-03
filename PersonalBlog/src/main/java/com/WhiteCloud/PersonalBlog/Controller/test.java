package com.WhiteCloud.PersonalBlog.Controller;

import com.WhiteCloud.PersonalBlog.Entity.userInfo;
import com.WhiteCloud.PersonalBlog.Service.userService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class test {
    private userService userService;
    @Autowired
    public test(com.WhiteCloud.PersonalBlog.Service.userService userService) {
        this.userService = userService;
    }

    @GetMapping("/form/userInfoInit")
    public List<userInfo> loadAllUser(){
        return userService.loadAllUser2();
    }

    @GetMapping("/form/userInfoInitById")
    public userInfo loadAllUserById(int userId){
        return userService.loadAllUserByUserId2(userId);
    }

    @PostMapping("/form/updateUserInfo")
    public String updateUserInfo(userInfo userInfo){
        System.out.println(userInfo.getUserId());
        System.out.println(userInfo.getNikename());
        System.out.println(userInfo.getPhone());
        System.out.println(userInfo.getSex());
        System.out.println(userInfo.getPhone());
        try{
            userService.updateUserInfo(userInfo);
            return "更改成功!";
        }catch (Exception e){
            e.printStackTrace();
            return "更改失败!";
        }
    }
}
