package com.WhiteCloud.SpringBootTest.Controller;
import com.WhiteCloud.SpringBootTest.Entity.userInfo;
import com.WhiteCloud.SpringBootTest.Model.MyException;
import com.WhiteCloud.SpringBootTest.Model.redisService.*;
//import com.WhiteCloud.SpringBootTest.Service.JMSService;
//import com.WhiteCloud.SpringBootTest.Utils.Config.JMSConfig;
import com.WhiteCloud.SpringBootTest.Model.redisService.redisServiceHash;
import com.WhiteCloud.SpringBootTest.Model.redisService.redisServiceList;
import com.WhiteCloud.SpringBootTest.Model.redisService.redisServiceString;
import com.WhiteCloud.SpringBootTest.Service.userService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.jms.Destination;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.UsernamePasswordToken;
//import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import static com.WhiteCloud.SpringBootTest.Model.ShiroRealm.loginRealm.remindMessage;

//@RestController
@Controller
@Tag(name = "测试控制器")
public class test {
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UnInterception {
    }
    @Resource
    redisServiceString redisServiceString;
    @Resource
    redisServiceHash redisServiceHash;
    @Resource
    redisServiceList redisServiceList;
//    @Resource
//    private Destination queue;
//    @Autowired
//    private JMSService jmsService;
    userService userService;
    private static final Logger logger = LoggerFactory.getLogger(test.class);

    @Autowired
    public test(userService userService) {
        this.userService = userService;
    }

    @GetMapping("SpringDocAndAopTest")
    @Operation(summary = "查找所有用户")
    public List<userInfo> SpringDocAndAopTest(){
        List<userInfo> userInfos = userService.showAllUserInfo2();
        return userInfos;
    }

    @PostMapping("/GlobalExceptionTest")
    public Object test(@RequestParam String username,@RequestParam String sex) throws MyException {
        String ParaList[] = {username,sex};
        List<userInfo> userInfos = userService.showUserInfoBySomeCondition(ParaList);
        throw new MyException();
        //return userInfos;
    }

    @PostMapping("/testRollback")
    public void testRollback(@RequestParam String username,@RequestParam int userId){
        userService.updateUsernameById(username,userId);
    }

    @GetMapping("/ListenerTest")
    public List<userInfo> ListenerTest(HttpServletRequest request){
        ServletContext servletContext = request.getServletContext();
        List<userInfo> userInfos = (List<userInfo>) servletContext.getAttribute("userInfos");
        return userInfos;
    }

    @GetMapping("/InterceptorTest")
    //@UnInterception
    public String InterceptorTest(){
            return "unauthorized.html";
        }

    @GetMapping("/redisTest")
    public String redisTest() throws JsonProcessingException {
        String a[] = {"刘"};
        ObjectMapper objectMapper = new ObjectMapper();
        List<userInfo> userInfos = userService.showUserInfoBySomeCondition(a);
        String json = objectMapper.writeValueAsString(userInfos);
        redisServiceString.setString("1",json);
        redisServiceHash.setHash("1","2",String.valueOf(2));
        redisServiceHash.getHash("1","1");
        return (String) redisServiceHash.getHash("1","2");
    }

//    @GetMapping("JMSTest")
//    public void JMSTest(){
//        jmsService.sendMessage(queue,"hello,jms!");
//    }

//    @PostMapping("/ShiroTest")
//    public String ShiroTest(String username,String password, HttpServletRequest request) {
//        // 根据用户名和密码创建token
//        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//        // 获取subject认证主体
//        Subject subject = SecurityUtils.getSubject();
//        try{
//            // 开始认证，这一步会跳到我们自定义的realm中
//            subject.login(token);
//            request.getSession().setAttribute("user", username);
//            return "index";
//        }catch(Exception e){
//            e.printStackTrace();
//            request.getSession().setAttribute("user", username);
//            request.setAttribute("error", remindMessage);
//            return "login";
//        }
//    }
}
