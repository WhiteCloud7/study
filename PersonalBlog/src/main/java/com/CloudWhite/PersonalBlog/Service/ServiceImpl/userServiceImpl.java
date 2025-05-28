package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import cn.hutool.core.lang.Snowflake;
import com.CloudWhite.PersonalBlog.Dao.DTO.tokenDao;
import com.CloudWhite.PersonalBlog.Dao.article.articleDao;
import com.CloudWhite.PersonalBlog.Dao.mybatisDao;
import com.CloudWhite.PersonalBlog.Dao.starDao;
import com.CloudWhite.PersonalBlog.Dao.userDao;
import com.CloudWhite.PersonalBlog.Dao.userDaoMybatis;
import com.CloudWhite.PersonalBlog.Entity.DTO.starDto;
import com.CloudWhite.PersonalBlog.Entity.DTO.token;
import com.CloudWhite.PersonalBlog.Entity.DTO.userInfo;
import com.CloudWhite.PersonalBlog.Entity.role;
import com.CloudWhite.PersonalBlog.Entity.user;
import com.CloudWhite.PersonalBlog.Model.Redis.redisCommonTemplate;
import com.CloudWhite.PersonalBlog.Model.Redis.redisHashTemplateConfig;
import com.CloudWhite.PersonalBlog.Model.Redis.redisStringTemplateConfig;
import com.CloudWhite.PersonalBlog.Model.ResponseEntity;
import com.CloudWhite.PersonalBlog.Model.UserContext;
import com.CloudWhite.PersonalBlog.Service.userService;
import com.CloudWhite.PersonalBlog.Utils.JWTUtils;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class userServiceImpl implements userService {
    private Snowflake snowflake = new Snowflake();
    private userDao userDao;
    private tokenDao tokenDao;
    private redisStringTemplateConfig redisStringTemplate;
    private redisHashTemplateConfig redisHashTemplate;
    private redisCommonTemplate redisCommonTemplate;
    private userDaoMybatis userDaoMybatis;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private StringRedisTemplate stringRedisTemplate;
    private mybatisDao mybatisDao;
    private starDao starDao;
    private articleDao articleDao;
    @Autowired
    public userServiceImpl(com.CloudWhite.PersonalBlog.Dao.userDao userDao, com.CloudWhite.PersonalBlog.Dao.DTO.tokenDao tokenDao, redisStringTemplateConfig redisStringTemplate, redisHashTemplateConfig redisHashTemplate, com.CloudWhite.PersonalBlog.Model.Redis.redisCommonTemplate redisCommonTemplate, com.CloudWhite.PersonalBlog.Dao.userDaoMybatis userDaoMybatis, StringRedisTemplate stringRedisTemplate, com.CloudWhite.PersonalBlog.Dao.mybatisDao mybatisDao, com.CloudWhite.PersonalBlog.Dao.starDao starDao, com.CloudWhite.PersonalBlog.Dao.article.articleDao articleDao) {
        this.userDao = userDao;
        this.tokenDao = tokenDao;
        this.redisStringTemplate = redisStringTemplate;
        this.redisHashTemplate = redisHashTemplate;
        this.redisCommonTemplate = redisCommonTemplate;
        this.userDaoMybatis = userDaoMybatis;
        this.stringRedisTemplate = stringRedisTemplate;
        this.mybatisDao = mybatisDao;
        this.starDao = starDao;
        this.articleDao = articleDao;
    }

    @PostConstruct
    @Order(1)
    public void initUser(){
        List<user> users = userDao.findAll();
        for(user user : users){
            if(user!=null){
                userInfo dto = new userInfo(user.getUserId(),user.getUsername(),user.getNikeName(), user.getSex(), user.getBirthday(), user.getPhone(),user.getQq(), user.getWechat(),  user.getSchool(),user.getAvatar_src());
                redisHashTemplate.setHashObject("user",user.getUsername(),dto);
            }
        }
    }

    public void permissionCheck(){};
    public List<user> findAll(){
        return userDao.findAll();
    }

    public userInfo getUserByUserId() {
        int userId = UserContext.getCurrentToken().getUserId();
        String username = UserContext.getCurrentToken().getUsername();
        userInfo cachedUserInfo = redisHashTemplate.getHashObject("user",username,userInfo.class);
        if (cachedUserInfo != null) {
            return cachedUserInfo;
        }
        user user = userDao.findByUserId(userId);
        if (user == null) {
            return null;
        }
        userInfo dto = new userInfo(user.getUsername(),user.getNikeName(), user.getSex(), user.getBirthday(), user.getPhone(),user.getQq(), user.getWechat(),  user.getSchool(),user.getAvatar_src());

        redisHashTemplate.setHashObject("user",username,dto);
        return dto;
    }

    public userInfo getUserByUsername(String username) {
        int userId = UserContext.getCurrentToken().getUserId();
        userInfo cachedUserInfo = redisHashTemplate.getHashObject("user",username,userInfo.class);
        if (cachedUserInfo != null) {
            return cachedUserInfo;
        }
        user user = userDao.findByUserId(userId);
        if (user == null) {
            return null;
        }
        userInfo dto = new userInfo(user.getUsername(),user.getNikeName(), user.getSex(), user.getBirthday(), user.getPhone(),user.getQq(), user.getWechat(),  user.getSchool(),user.getAvatar_src());
        redisHashTemplate.setHashObject("user",username,dto);
        return dto;
    }

    public void addFriend(String username){
        int friendId = userDao.findUserIdByUserName(username);
        int userId = UserContext.getCurrentToken().getUserId();
        mybatisDao.saveNewFriend(userId,friendId);
    }

    public ResponseEntity login(String username,String password){
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user user = userDao.findByUsername(username);
        role role = new role();
        if(user!=null)
            if(bCryptPasswordEncoder.matches(password,userDao.findPasswordByUserName(username))){
                token token = new token();
                token.setUserId(user.getUserId());
                token.setUsername(user.getUsername());
                token.setRole(role);
                token.getRole().setRoleName(user.getRole().getRoleName());

                String refreshToken = JWTUtils.createRefreshToken(token,0);
                String accessToken = JWTUtils.createAccessToken(token,1000*60*30);

                redisStringTemplate.setObject(username.concat("Token"),refreshToken);
                redisCommonTemplate.setExpire(username.concat("Token"),7, TimeUnit.DAYS);
                return new ResponseEntity("200", "登录成功", new String[]{accessToken, refreshToken});
            }
            else
                return new ResponseEntity("500", "密码错误", null);
        else
            return new ResponseEntity("500", "账号不存在", null);
    }

    public String register(String username, String password) {
        String regex1 = "^[A-za-z0-9]{6,16}$";

        List<String> illegalityChars = Arrays.asList("&", ";", "#", " ", "=", "?", "<", ">", "'", "\"", "$", "-");
        StringBuilder regexPattern = new StringBuilder("[");
        for (String charStr : illegalityChars) {
            String escapedChar = charStr.replaceAll("[.*+?^${}()|\\[\\\\\\]]", "\\" + charStr);
            regexPattern.append(escapedChar);
        }
        regexPattern.append("]");

        Pattern pattern1 = Pattern.compile(regex1);
        Pattern pattern2 = Pattern.compile(regexPattern.toString());
        Matcher matcher1 = pattern1.matcher(username);
        Matcher matcher2 = pattern2.matcher(password);

        if(username.length()<6||username.length()>16||password.length()<6||password.length()>20){
            return "账号或密码校验错误，请重新输入";
        }
        if(!matcher1.matches()||matcher2.find()){
            return "账号或密码校验错误，请重新输入";
        }
        if (userDao.findByUsername(username) != null) {
            return "用户已存在";
        }
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        // 构建新用户
        user newUser = new user();
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);
        newUser.setRoleId(2);
        userDaoMybatis.insertUser(newUser);
        // 获取用户ID（建议直接从 insert 中返回或使用 selectKey）
        int userId = userDao.findUserIdByUserName(username);
        // 设置默认信息
        userDaoMybatis.setDefaultNikeName("用户" + userId, userId);
        userDaoMybatis.setAuthor(userId);
        userDaoMybatis.setDefaultMessage(username, "欢迎访问我的博客！");

        // 缓存用户
        userInfo userInfo = new userInfo(userId,username);
        redisHashTemplate.setHashObject("user",username,userInfo);
        return "注册成功";
    }
    public List<starDto> getStar(){
        int userId = UserContext.getCurrentToken().getUserId();
        List<Integer> articleIds = starDao.getArticleIdByUserId(userId);
        List<starDto> starDtos = new ArrayList<>();
        for (Integer articleId : articleIds) {
            int userId2 = articleDao.getUserIdByArticleId(articleId);
            user user = userDao.findByUserId(userId);
            String title = articleDao.getArticleTitleByArticleId(articleId);
            starDto starDto = new starDto(userId2,articleId,user.getNikeName(),user.getAvatar_src(),title);
            starDtos.add(starDto);
        }
        return starDtos;
    }
    public String saveProfile(user user){
        int userId = UserContext.getCurrentToken().getUserId();
        String username = UserContext.getCurrentToken().getUsername();
        user.setUserId(userId);
        if(user.getNikeName()!=null&&userDao.findNikeNameByUserName(username).equals(user.getNikeName())){
            return "昵称已被使用！";
        }
        userDaoMybatis.updateUserProfile(user);
        userInfo userInfo = new userInfo(userId,username,user.getNikeName(),user.getSex(),user.getBirthday(),user.getPhone(),user.getQq(),user.getWechat(),user.getSchool(),user.getAvatar_src());
        redisHashTemplate.setHashObject("user",userInfo.getUsername(),userInfo);
        return "保存成功";
    }

    public String refreshToken(String refreshToken){
        return JWTUtils.refreshAccessToken(refreshToken,1000*60*30);
    }

    private String getExtension(String contentType) {
        switch (contentType) {
            case "image/jpeg": return ".jpg";
            case "image/png": return ".png";
            case "image/gif": return ".gif";
            default: return ""; // 或者抛出异常
        }
    }
    public String updateAvatar(MultipartFile multipartFile) {
        String username = UserContext.getCurrentToken().getUsername();
        try {
            if (multipartFile.isEmpty())
                return "上传文件为空";
            String extension = getExtension(multipartFile.getContentType());
            if (extension.isEmpty())
                return "不支持的文件类型";
            String newFilename = "avatar-" + snowflake.nextIdStr() + extension;
            String uploadDir = System.getProperty("user.dir") + "/uploads/" + username;
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path))
                Files.createDirectories(path);
            // 清空原头像
            Files.list(path).forEach(file -> {
                try {
                    Files.delete(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            multipartFile.transferTo(path.resolve(newFilename).toFile());
            int userId = UserContext.getCurrentToken().getUserId();
            userDaoMybatis.setUserAvatar("http://localhost:8081/uploads/" + username + "/" + newFilename, userId);
            user user = userDao.findByUserId(userId);
            userInfo userInfo = new userInfo();
            BeanUtils.copyProperties(user,userInfo);
            redisHashTemplate.setHashObject("user",userInfo.getUsername(),userInfo);
            return "/uploads/" + username + "/" + newFilename;
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败";
        }
    }

    public void logout(String username){
        redisCommonTemplate.deleteKey(username.concat("Token"));
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void scheduledRefreshCache() {
        redisCommonTemplate.deleteKey("user");
        initUser();
    }
}
