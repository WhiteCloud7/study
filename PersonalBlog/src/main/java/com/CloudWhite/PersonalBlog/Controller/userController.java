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
    @PostMapping("/refreshToken")
    public ResponseEntity refreshToken(String refreshToken){
        return new ResponseEntity(refreshToken);
    }

    @GetMapping("/profile")
    @LoginRequired
    public ResponseEntity profile(){
        return new ResponseEntity("200","查询成功",userService.getUserByUserId());
    }

    @GetMapping("/login")
    public Object login(@RequestParam String username,@RequestParam String password){
        return userService.login(username,password);
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
