package com.CloudWhite.PersonalBlog.Model.Redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class redisListTemplateConfig {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 左侧插入字符串
    public void setStringLeft(String key, String value) {
        ListOperations<String, String> ops = stringRedisTemplate.opsForList();
        ops.leftPush(key, value);
    }

    // 右侧插入字符串
    public void setStringRight(String key, String value) {
        ListOperations<String, String> ops = stringRedisTemplate.opsForList();
        ops.rightPush(key, value);
    }

    // 从左侧弹出字符串
    public String getListLeft(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    // 从右侧弹出字符串
    public String getListRight(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    // 插入对象（右）
    public <T> void setObjectRight(String key, T obj) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            setStringRight(key, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化失败", e);
        }
    }

    // 弹出对象（左）
    public <T> T getObjectLeft(String key, Class<T> clazz) {
        String json = getListLeft(key);
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("反序列化失败", e);
        }
    }
}
