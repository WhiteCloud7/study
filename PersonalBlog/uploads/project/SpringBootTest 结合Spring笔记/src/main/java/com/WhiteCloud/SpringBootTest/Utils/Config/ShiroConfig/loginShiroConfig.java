//package com.WhiteCloud.SpringBootTest.Utils.Config.ShiroConfig;
//import com.WhiteCloud.SpringBootTest.Model.ShiroRealm.loginRealm;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//@Configuration
//public class loginShiroConfig {
//    @Bean
//    public loginRealm loginRealm() {
//        loginRealm loginRealm = new loginRealm();
//        System.out.println("Auth域注册完成————");
//        return loginRealm;
//    }
//    @Bean
//    public DefaultWebSecurityManager securityManager() {
//        // 将自定义realm加进来
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(loginRealm());
//        SecurityUtils.setSecurityManager(securityManager);  // 绑定到SecurityUtils
//        System.out.println("====securityManager注册并绑定完成====");
//        return securityManager;
//    }
//
//    @Bean
//    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
//        // 定义shiroFactoryBean
//        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
//
//        // 设置自定义的securityManager
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//
//        // 设置默认登录的url，身份认证失败会访问该url
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        // 设置成功之后要跳转的链接
//        shiroFilterFactoryBean.setSuccessUrl("/index");
//        // 设置未授权界面，权限认证失败会访问该url
//        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
//
//        // LinkedHashMap是有序的，进行顺序拦截器配置
//        Map<String,String> filterChainMap = new LinkedHashMap<>();
//
//        // 配置可以匿名访问的地址，可以根据实际情况自己添加，放行一些静态资源等，anon表示放行
//        filterChainMap.put("/css/**", "anon");
//        filterChainMap.put("/imgs/**", "anon");
//        filterChainMap.put("/js/**", "anon");
//        filterChainMap.put("/swagger-*/**", "anon");
//        filterChainMap.put("/swagger-ui.html/**", "anon");
//        // 登录url 放行
//        filterChainMap.put("/login", "anon");
//
//        // “/user/admin” 开头的需要身份认证，authc表示要身份认证
//        filterChainMap.put("/user/admin*", "authc");
//        // “/user/student” 开头的需要角色认证，是“admin”才允许
//        filterChainMap.put("/user/student*/**", "roles[admin]");
//        // “/user/teacher” 开头的需要权限认证，是“user:create”才允许
//        filterChainMap.put("/user/teacher*/**", "perms[\"user:create\"]");
//
//        // 配置logout过滤器
//        filterChainMap.put("/logout", "logout");
//
//        // 设置shiroFilterFactoryBean的FilterChainDefinitionMap
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
//        System.out.println("====shiroFilterFactoryBean注册完成====");
//        return shiroFilterFactoryBean;
//    }
//}
