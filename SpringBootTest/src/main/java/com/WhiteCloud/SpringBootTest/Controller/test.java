package com.WhiteCloud.SpringBootTest.Controller;
import com.WhiteCloud.SpringBootTest.Entity.userInfo;
import com.WhiteCloud.SpringBootTest.Model.MyException;
import com.WhiteCloud.SpringBootTest.Service.userService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

//@RestController
@Controller
@Tag(name = "测试控制器")
public class test {
    userService userService;
    private static final Logger logger = LoggerFactory.getLogger(test.class);

    @Autowired
    public test(com.WhiteCloud.SpringBootTest.Service.userService userService) {
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
    public String InterceptorTest(HttpServletRequest request) throws IOException {
        Resource resource = new ClassPathResource("static/test.html");
        try (InputStream inputStream = resource.getInputStream()) {
            // 将输入流中的内容读取为字符串
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }
}
