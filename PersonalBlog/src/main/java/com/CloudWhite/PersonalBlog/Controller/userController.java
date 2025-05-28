package com.CloudWhite.PersonalBlog.Controller;

import com.CloudWhite.PersonalBlog.Entity.user;
import com.CloudWhite.PersonalBlog.Model.Redis.redisStringTemplateConfig;
import com.CloudWhite.PersonalBlog.Model.ResponseEntity;
import com.CloudWhite.PersonalBlog.Service.userService;
import com.CloudWhite.PersonalBlog.Utils.Annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class userController{
    @Autowired
    private redisStringTemplateConfig redisStringTemplateConfig;
    public userService userService;
    @Autowired
    public userController(userService userService) {
        this.userService = userService;
    }
    @GetMapping("permissionCheck")
    @PermissionRequired(type = "admin")
    @RateLimitForAll(maxRequests = 1000)
    @RateLimitForUnlogin(maxRequests = 1)
    @RateLimitForCurrent(maxRequests = 1)
    @Operation(description = "权限检测")
    public ResponseEntity permissionCheck(){
        return new ResponseEntity();}
    @GetMapping("/refreshToken")
    @RateLimitForAll(maxRequests = 1000)
    @Operation(description = "刷新token")
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
    @RateLimitForUnlogin(maxRequests = 60)
    @RateLimitForAll(maxRequests = 1000)
    @RateLimitForCurrent(maxRequests = 60)
    @Operation(description = "个人信息")
    public ResponseEntity profile(){
        return new ResponseEntity("200","查询成功",userService.getUserByUserId());
    }
    @GetMapping("/getStar")
    @LoginRequired
    @RateLimitForUnlogin(maxRequests = 60)
    @RateLimitForAll(maxRequests = 1000)
    @RateLimitForCurrent(maxRequests = 60)
    @Operation(description = "收藏信息")
    public ResponseEntity getStar(){
        return new ResponseEntity(userService.getStar());
    }
    @GetMapping("/friendProfile")
    @RateLimitForUnlogin(maxRequests = 10,seconds = 10)
    @Operation(description = "查看好友信息")
    public ResponseEntity friendProfile(String username) {
        return new ResponseEntity(userService.getUserByUsername(username));
    }

    @GetMapping("/addFriend")
    @LoginRequired
    @Operation(description = "添加好友")
    public ResponseEntity addFriend(String username){
        userService.addFriend(username);
        return new ResponseEntity();
    }

    @GetMapping("/login")
    @RateLimitForUnlogin(maxRequests = 20)
    @RateLimitForAll(maxRequests = 1000)
    @Operation(description = "登录")
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
    @Operation(description = "保存用户信息")
    public ResponseEntity saveProfile(@RequestBody user user){
        return new ResponseEntity(userService.saveProfile(user));
    }

    @PostMapping("/uploadAvatar")
    @LoginRequired
    @Operation(description = "上传头像")
    public ResponseEntity uploadAvatar(MultipartFile file){
        return new ResponseEntity(userService.updateAvatar(file));
    }

    @GetMapping("/logout")
    @RateLimitForUnlogin()
    @RateLimitForAll(maxRequests = 1000)
    @RateLimitForCurrent(maxRequests = 10)
    @Operation(description = "登出")
    public ResponseEntity logout(String username){
        userService.logout(username);
        return new ResponseEntity();
    }
}
