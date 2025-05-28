package com.CloudWhite.PersonalBlog.Model;

import cn.hutool.core.lang.Snowflake;
import com.CloudWhite.PersonalBlog.Entity.DTO.CachedMessage;
import com.CloudWhite.PersonalBlog.Entity.DTO.messageDto;
import com.CloudWhite.PersonalBlog.Entity.DTO.userInfo;
import com.CloudWhite.PersonalBlog.Model.Redis.redisHashTemplateConfig;
import com.CloudWhite.PersonalBlog.Model.Redis.redisListTemplateConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler extends TextWebSocketHandler {
    private final Snowflake snowflake = new Snowflake();
    private final redisListTemplateConfig redisListTemplate;
    private final redisHashTemplateConfig redisHashTemplate;
    public WebSocketHandler(redisListTemplateConfig redisListTemplate, redisHashTemplateConfig redisHashTemplate) {
        this.redisListTemplate = redisListTemplate;
        this.redisHashTemplate = redisHashTemplate;
    }
    private static final Map<Integer, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        int userId = (int) attributes.get("userId");
        sessionMap.put(userId,session);
        System.out.println("连接到: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("收到: " + message.getPayload());
        ObjectMapper objectMapper = new ObjectMapper();
        messageDto messageDto = objectMapper.readValue(message.getPayload(), messageDto.class);

        Map<String, Object> attributes = session.getAttributes();
        int userId = (int) attributes.get("userId");
        String username = (String) attributes.get("username");
        String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        messageDto.setSendTime(formattedTime);
        // 写入 Redis
        int receiverId = redisHashTemplate.getHashObject("user",messageDto.getReceiverName(), userInfo.class).getUserId();
        String redisKey = buildRedisKey(userId, receiverId);
        String messageId = String.valueOf(snowflake.nextId());
        CachedMessage cached = new CachedMessage(userId, receiverId, messageId,username,
                messageDto.getReceiverName(), messageDto.getMessage(), messageDto.getSendTime());
        redisListTemplate.setObjectRight(redisKey, cached);
        messageDto.setMessageId(messageId);

        WebSocketSession webSocketSession = sessionMap.get(receiverId);
        WebSocketSession webSocketSessionToMe = sessionMap.get(userId);
        if(webSocketSessionToMe!=null&&webSocketSessionToMe.isOpen()){
            webSocketSessionToMe.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDto)));
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDto)));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Map<String, Object> attributes = session.getAttributes();
        Object userIdObj = attributes.get("userId");
        if (userIdObj instanceof Integer userId) {
            sessionMap.remove(userId);
            System.out.println("用户 " + userId + " 已断开连接");
        } else {
            System.out.println("断开连接但未找到 userId，对应 sessionId: " + session.getId());
        }
    }

    private String buildRedisKey(Integer userId, Integer friendId) {
        // 保证 key 一致性：userId 和 friendId 排序，避免重复（如 1-2 和 2-1 是同一对话）
        int minId = Math.min(userId, friendId);
        int maxId = Math.max(userId, friendId);
        return "chat:" + minId + ":" + maxId;
    }
}
