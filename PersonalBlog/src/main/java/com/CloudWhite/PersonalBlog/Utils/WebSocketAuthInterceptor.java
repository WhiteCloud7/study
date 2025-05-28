package com.CloudWhite.PersonalBlog.Utils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketAuthInterceptor implements HandshakeInterceptor {
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (!(request instanceof ServletServerHttpRequest)) return true;
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String token = servletRequest.getParameter("token");
        if (token == null || token.isEmpty()) {
            return false;
        }
        Claims claims;
        try {
            claims = JWTUtils.parseToken(token);
        } catch (Exception e) {
            return false;
        }
        if (!JWTUtils.validateToken(token)) {
            return false;
        }
        attributes.put("userId", claims.get("userId"));
        attributes.put("username", claims.get("username"));
        attributes.put("roleName", claims.get("roleName"));
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
