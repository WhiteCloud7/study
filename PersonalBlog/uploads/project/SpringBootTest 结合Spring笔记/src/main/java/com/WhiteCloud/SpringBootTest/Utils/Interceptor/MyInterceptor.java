package com.WhiteCloud.SpringBootTest.Utils.Interceptor;

import com.WhiteCloud.SpringBootTest.Controller.test.UnInterception;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

@Component
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 检查方法是否有 @UnInterception 注解
            if (method.isAnnotationPresent(UnInterception.class)) {
                System.out.println("方法标记了 @UnInterception 注解，不进行拦截");
                return true; // 有 @UnInterception 注解，不拦截
            }
        }
        System.out.println("方法执行前，拦截了方法");
        // 没有 @UnInterception 注解，执行拦截逻辑
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        System.out.println("方法已经执行，准备渲染视图————");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println("方法完成，抛出异常");
    }
}