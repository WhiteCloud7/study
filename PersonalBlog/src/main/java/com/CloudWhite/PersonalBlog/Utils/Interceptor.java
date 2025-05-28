package com.CloudWhite.PersonalBlog.Utils;
import com.CloudWhite.PersonalBlog.Entity.DTO.token;
import com.CloudWhite.PersonalBlog.Entity.role;
import com.CloudWhite.PersonalBlog.Model.UserContext;
import com.CloudWhite.PersonalBlog.Utils.Annotation.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Interceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;
    public Interceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        PermissionRequired permissionRequired = method.getAnnotation(PermissionRequired.class);
        LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
        RateLimitForAll rateLimitForAll = method.getAnnotation(RateLimitForAll.class);
        RateLimitForUnlogin rateLimitForUnlogin = method.getAnnotation(RateLimitForUnlogin.class);
        RateLimitForCurrent rateLimitForCurrent = method.getAnnotation(RateLimitForCurrent.class);

        if (rateLimitForAll != null) {
            String rateLimitKeyForAll = "ratelimit:global:" + method.getName();
            int maxForAll = rateLimitForAll.maxRequests();
            int secondsForAll = rateLimitForAll.seconds();
            Long countForAll = stringRedisTemplate.opsForValue().increment(rateLimitKeyForAll);
            if (countForAll == 1)
                stringRedisTemplate.expire(rateLimitKeyForAll, Duration.ofSeconds(secondsForAll));
            if (countForAll > maxForAll) {
                response.setStatus(429);
                response.getWriter().write("请求频繁!");
                return false;
            }
        }

        if (rateLimitForUnlogin != null) {
            String rateLimitKeyForUnlogin = "ratelimit:ip:" + getClientIp(request) + ":" +method.getName();
            int maxForUnlogin = rateLimitForUnlogin.maxRequests();
            int secondsForUnlogin = rateLimitForUnlogin.seconds();
            Long countForUnlogin = stringRedisTemplate.opsForValue().increment(rateLimitKeyForUnlogin);
            if (countForUnlogin == 1)
                stringRedisTemplate.expire(rateLimitKeyForUnlogin, Duration.ofSeconds(secondsForUnlogin));
            if (countForUnlogin > maxForUnlogin) {
                response.setStatus(429);
                response.getWriter().write("请求频繁!");
                return false;
            }
        }

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
        if(rateLimitForCurrent!=null){  //是否单个用户限流
            String RateLimitKeyForCurrent = "ratelimit:user:" + userId + ":" + method.getName();
            int secondsForCurrent = rateLimitForCurrent.seconds();
            int maxForCurrent = rateLimitForCurrent.maxRequests();
            Long countForCurrent = stringRedisTemplate.opsForValue().increment(RateLimitKeyForCurrent);
            if(countForCurrent == 1)
                stringRedisTemplate.expire(RateLimitKeyForCurrent,Duration.ofSeconds(secondsForCurrent));
            if(countForCurrent>maxForCurrent){  //超过最大请求拦截
                response.setStatus(429);
                response.getWriter().write("请求频繁!");
                return false;
            }
        }

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

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0];
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}