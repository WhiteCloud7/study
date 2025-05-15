package com.CloudWhite.PersonalBlog.Controller;

import com.CloudWhite.PersonalBlog.Dao.userDaoMybatis;
import com.CloudWhite.PersonalBlog.Entity.role;
import com.CloudWhite.PersonalBlog.Entity.user;
import com.CloudWhite.PersonalBlog.Model.Redis.redisStringTemplateConfig;
import com.CloudWhite.PersonalBlog.Model.ResponseEntity;
import com.CloudWhite.PersonalBlog.Model.UserContext;
import com.CloudWhite.PersonalBlog.Service.userService;
import com.CloudWhite.PersonalBlog.Utils.Annotation.LoginRequired;
import com.CloudWhite.PersonalBlog.Utils.Annotation.PermissionRequired;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
public class userController{
    @Autowired
    private redisStringTemplateConfig redisStringTemplateConfig;
    public userService userService;
    @Autowired
    public userController(com.CloudWhite.PersonalBlog.Service.userService userService) {
        this.userService = userService;
    }
    @GetMapping("permissionCheck")
    @PermissionRequired(type = "admin")
    public ResponseEntity permissionCheck(){
        return new ResponseEntity();}
    @GetMapping("/refreshToken")
    public ResponseEntity refreshToken(HttpServletRequest request){
        String refreshToken = null;
        for (Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals("refreshToken")){
                refreshToken = cookie.getValue();
            }
        }
        return new ResponseEntity(userService.refreshToken(refreshToken));
    }

    @GetMapping("/profile")
    @LoginRequired
    public ResponseEntity profile(){
        return new ResponseEntity("200","查询成功",userService.getUserByUserId());
    }

    @GetMapping("/login")
    public ResponseEntity login(@RequestParam String username, @RequestParam String password, HttpServletResponse response){
        ResponseEntity res = userService.login(username,password);
        if(res.getCode().equals("200")){
            Cookie cookie = new Cookie("refreshToken", ((String[])res.getData())[1]);
            cookie.setPath("/"); // 根路径，前端所有接口都能带上
            cookie.setHttpOnly(true); // JavaScript 无法访问，提升安全性
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7天
            cookie.setSecure(false); // 若部署到 HTTPS，请设为 true
            response.addCookie(cookie);
            String accessToken = ((String[])res.getData())[0];
            res.setData(accessToken);
            return res;
        } else {
            return userService.login(username,password);
        }
    }

    @GetMapping("/register")
    public String register(String username,String password){
        return userService.register(username,password);
    }

    @PostMapping ("/saveProfile")
    @LoginRequired
    public ResponseEntity saveProfile(@RequestBody user user){
        userService.saveProfile(user);
        return new ResponseEntity();
    }

    @PostMapping("/uploadAvatar")
    @LoginRequired
    public ResponseEntity uploadAvatar(MultipartFile file){
        return new ResponseEntity(userService.updateAvatar(file));
    }

    @GetMapping("/logout")
    public ResponseEntity logout(String username){
        userService.logout(username);
        return new ResponseEntity();
    }
}
