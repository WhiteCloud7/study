package com.CloudWhite.PersonalBlog.Utils;
import com.CloudWhite.PersonalBlog.Entity.DTO.token;
import com.CloudWhite.PersonalBlog.Entity.role;
import com.CloudWhite.PersonalBlog.Model.UserContext;
import com.CloudWhite.PersonalBlog.Utils.Annotation.LoginRequired;
import com.CloudWhite.PersonalBlog.Utils.Annotation.PermissionRequired;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        PermissionRequired permissionRequired = method.getAnnotation(PermissionRequired.class);
        LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);

        String header = request.getHeader("Authorization");
        if (header == null || !header.toLowerCase().startsWith("bearer ")) {
            if (loginRequired != null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("未登录");
                return false;
            }
            return true; // 匿名访问允许
        }
        String tokenStr = header.substring(7);
        Claims claims;
        try {
            claims = JWTUtils.parseToken(tokenStr);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("token无效");
            return false;
        }
        if (!JWTUtils.validateToken(tokenStr)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("token已过期");
            return false;
        }
        // 统一提取用户信息并注入上下文（无论是否有权限注解）
        int userId = (int) claims.get("userId");
        String username = (String) claims.get("username");
        String roleName = (String) claims.get("roleName");

        token userToken = new token();
        userToken.setUserId(userId);
        userToken.setUsername(username);
        role role = new role();
        role.setRoleName(roleName);
        userToken.setRole(role);
        UserContext.setCurrentToken(userToken);
        // 论是否需要权限，已登录就存入上下文  // 再判断权限
        if (permissionRequired != null && !permissionRequired.type().equals(roleName)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("无权限访问");
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}