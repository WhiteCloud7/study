package com.CloudWhite.PersonalBlog.Model.Redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class redisStringTemplateConfig {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 设置字符串值
    public void setString(String key, String value) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key, value);
    }

    // 设置字符串值并设置过期时间
    public void setStringAndDeadTime(String key, String value, long time, TimeUnit unit) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key, value, time, unit);
    }

    // 获取字符串值
    public String getString(String key) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        return ops.get(key);
    }

    // 设置对象为 JSON 字符串
    public <T> void setObject(String key, T obj) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            setString(key, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化对象失败", e);
        }
    }

    // 设置对象为 JSON 字符串并带过期时间
    public <T> void setObjectAndDeadTime(String key, T obj, long time, TimeUnit unit) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            setStringAndDeadTime(key, json, time, unit);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化对象失败", e);
        }
    }

    // 获取 JSON 字符串并反序列化为对象
    public <T> T getObject(String key, Class<T> clazz) {
        String json = getString(key);
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("反序列化对象失败", e);
        }
    }
}
