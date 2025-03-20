package com.WhiteCloud.SpringBootTest.Utils.Listener;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class servletRequestListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent event){
        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        System.out.println("请求sessionId："+request.getRequestedSessionId());
        System.out.println("请求url："+request.getRequestURL());
        request.setAttribute("url",request.getRequestURL());
    }

    public void requestDestroyed(ServletRequestEvent event){
        HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
        System.out.println("来自："+request.getAttribute("uel")+"请求已销毁");
    }
}
