package com.WhiteCloud.SpringBootTest.Controller;
import com.WhiteCloud.SpringBootTest.Dao.jpaUserDao;
import com.WhiteCloud.SpringBootTest.Dao.userDao;
import com.WhiteCloud.SpringBootTest.Entity.role;
import com.WhiteCloud.SpringBootTest.Entity.userInfo;
import com.WhiteCloud.SpringBootTest.Model.MyException;
//import com.WhiteCloud.SpringBootTest.Service.JMSService;
//import com.WhiteCloud.SpringBootTest.Utils.Config.JMSConfig;
import com.WhiteCloud.SpringBootTest.Model.redisService.redisServiceHash;
import com.WhiteCloud.SpringBootTest.Model.redisService.redisServiceList;
import com.WhiteCloud.SpringBootTest.Model.redisService.redisServiceString;
//import com.WhiteCloud.SpringBootTest.Service.ServiceImpl.UserDetailsServiceImpl;
import com.WhiteCloud.SpringBootTest.Service.ServiceImpl.jpaUserServiceImpl;
import com.WhiteCloud.SpringBootTest.Service.jpaUserService;
import com.WhiteCloud.SpringBootTest.Service.userService;
import com.WhiteCloud.SpringBootTest.Utils.LucenceTest.ChineseSearcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.UsernamePasswordToken;
//import org.apache.shiro.subject.Subject;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//import static com.WhiteCloud.SpringBootTest.Model.ShiroRealm.loginRealm.remindMessage;

//@RestController
@Controller
@Tag(name = "测试控制器")
public class test {
    private static final Logger logger = LoggerFactory.getLogger(test.class);
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
    @Resource
    ChineseSearcher chineseSearcher;
    private jpaUserService jpaUserService;
    @Autowired
    private jpaUserDao jpaUserDao;
    userService userService;
    @Autowired
    public test(userService userService,jpaUserService jpaUserService) {
        this.userService = userService;
        this.jpaUserService = jpaUserService;
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

    @GetMapping("LuceneTest")
    public String LuceneTest(Model model){
        String indexDir = "D:\\GitHub\\study\\SpringBootTest\\src\\main\\java\\com\\WhiteCloud\\SpringBootTest\\Utils\\LucenceTest\\ChineseIndex";
        try {
            List<String> list = chineseSearcher.searcher(indexDir,"关羽");
            model.addAttribute("text",list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (InvalidTokenOffsetsException e) {
            throw new RuntimeException(e);
        }
        return "luceneTest";
    }
    @RequestMapping("SpringDataJpa")
    public Object SpringDataJpa(String username){
        return jpaUserService.findAll();
    }
    public String likePattern(String str) {
        StringBuilder stringBuilder = new StringBuilder(str);
        stringBuilder.insert(0, "%");
        stringBuilder.insert(stringBuilder.length(), "%");
        str = stringBuilder.toString();
        return str;
    }
    // 访问登录页面
//    @GetMapping("/login")
//    public String login() {
//        return "login";  // 返回 login.html
//    }
//    private UserDetailsServiceImpl userDetailsService;
//    // 登录成功跳转页面
//    @GetMapping("/index")
//    public String index() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            // 获取用户名
//            String username = userDetails.getUsername();
//            // 获取用户权限
//            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
//            // 例如判断用户是否具有某个角色
//            Collection<String> roles = authorities.stream()
//                    .map(GrantedAuthority::getAuthority)
//                    .collect(Collectors.toList());
//            System.out.println(username+roles);
//        }
//        return "index";  // 返回 index.html
//    }
//
//    @GetMapping("/test")
//    @PreAuthorize("hasRole('normalUser')")
//    @ResponseBody
//    public String test(){
//        return "normalUser";
//    }
//
//    @GetMapping("/denied")
//    public String denied(){
//        return "error/denied";
//    }
//

}
