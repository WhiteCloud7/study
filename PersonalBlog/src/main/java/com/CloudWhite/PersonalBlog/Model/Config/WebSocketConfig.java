package com.CloudWhite.PersonalBlog.Model.Config;

import com.CloudWhite.PersonalBlog.Model.Redis.redisHashTemplateConfig;
import com.CloudWhite.PersonalBlog.Model.Redis.redisListTemplateConfig;
import com.CloudWhite.PersonalBlog.Model.WebSocketHandler;
import com.CloudWhite.PersonalBlog.Utils.WebSocketAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private redisListTemplateConfig redisListTemplate;
    @Autowired
    private redisHashTemplateConfig redisHashTemplate;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.
            addHandler(new WebSocketHandler(redisListTemplate,redisHashTemplate), "/websocket")
            .addInterceptors(new WebSocketAuthInterceptor()) // 添加握手拦截器
            .setAllowedOrigins("*");
    }
}