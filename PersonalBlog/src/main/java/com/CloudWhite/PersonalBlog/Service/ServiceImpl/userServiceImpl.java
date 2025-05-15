package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import com.CloudWhite.PersonalBlog.Dao.DTO.tokenDao;
import com.CloudWhite.PersonalBlog.Dao.userDao;
import com.CloudWhite.PersonalBlog.Dao.userDaoMybatis;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class userServiceImpl implements userService {
    private userDao userDao;
    private tokenDao tokenDao;
    private redisStringTemplateConfig redisStringTemplate;
    private redisHashTemplateConfig redisHashTemplate;
    private redisCommonTemplate redisCommonTemplate;
    private userDaoMybatis userDaoMybatis;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    public userServiceImpl(com.CloudWhite.PersonalBlog.Dao.userDao userDao, com.CloudWhite.PersonalBlog.Dao.DTO.tokenDao tokenDao, redisStringTemplateConfig redisStringTemplate, redisHashTemplateConfig redisHashTemplate, com.CloudWhite.PersonalBlog.Dao.userDaoMybatis userDaoMybatis,redisCommonTemplate redisCommonTemplate,StringRedisTemplate stringRedisTemplate) {
        this.userDao = userDao;
        this.tokenDao = tokenDao;
        this.redisStringTemplate = redisStringTemplate;
        this.redisHashTemplate = redisHashTemplate;
        this.userDaoMybatis = userDaoMybatis;
        this.redisCommonTemplate = redisCommonTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
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

    public void saveProfile(user user){
        int userId = UserContext.getCurrentToken().getUserId();
        String username = UserContext.getCurrentToken().getUsername();
        user.setUserId(userId);
        userDaoMybatis.updateUserProfile(user);
        userInfo userInfo = new userInfo(userId,username,user.getNikeName(),user.getSex(),user.getBirthday(),user.getPhone(),user.getQq(),user.getWechat(),user.getSchool(),user.getAvatar_src());
        redisHashTemplate.setHashObject("user",userInfo.getUsername(),userInfo);
    }

    public String refreshToken(String refreshToken){
        return JWTUtils.refreshAccessToken(refreshToken,1000*60*30);
    }

    public String updateAvatar(MultipartFile multipartFile) {
        String username = UserContext.getCurrentToken().getUsername();
        try {
            if (multipartFile.isEmpty())
                return "上传文件为空";

            String newFilename = "avatar-"+multipartFile.getOriginalFilename();
            String uploadDir = System.getProperty("user.dir") + "/uploads/"+username;
            Path path = Paths.get(uploadDir);

            if (!Files.exists(path))
                Files.createDirectories(path);

            multipartFile.transferTo(path.resolve(newFilename).toFile());

            int userId = UserContext.getCurrentToken().getUserId();
            userDaoMybatis.setUserAvatar("http://59.110.48.56:8081/uploads/" + username + "/" + newFilename, userId);

            return "/uploads/" + newFilename;
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
