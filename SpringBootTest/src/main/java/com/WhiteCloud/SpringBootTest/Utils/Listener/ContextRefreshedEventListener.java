package com.WhiteCloud.SpringBootTest.Utils.Listener;

import com.WhiteCloud.SpringBootTest.Entity.userInfo;
import com.WhiteCloud.SpringBootTest.Service.ServiceImpl.userServiceImpl;
import com.WhiteCloud.SpringBootTest.Service.userService;
import jakarta.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        userService userService = applicationContext.getBean(userService.class);
        List<userInfo> userInfos = userService.showAllUserInfo();
        ServletContext application = applicationContext.getBean(ServletContext.class);
        application.setAttribute("userInfos",userInfos);
    }
}
