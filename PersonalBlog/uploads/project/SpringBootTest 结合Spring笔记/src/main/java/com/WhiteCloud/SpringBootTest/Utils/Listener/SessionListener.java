//package com.WhiteCloud.SpringBootTest.Utils.Listener;
//
//import jakarta.servlet.http.HttpSession;
//import jakarta.servlet.http.HttpSessionEvent;
//import jakarta.servlet.http.HttpSessionListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SessionListener implements HttpSessionListener {
//    private static int OnlineCount;
//    @Override
//    public void sessionCreated(HttpSessionEvent event){
//        HttpSession session = event.getSession();
//        OnlineCount++;
//        session.getServletContext().setAttribute("我登陆了",OnlineCount);
//        //session.setAttribute("我登陆了",OnlineCount);
//    }
//
//    @Override
//    public void sessionDestroyed(HttpSessionEvent event){
//        HttpSession session = event.getSession();
//        OnlineCount--;
//        session.getServletContext().setAttribute("我下线了",OnlineCount);
//        //session.setAttribute("我登陆了",OnlineCount);
//    }
//}
