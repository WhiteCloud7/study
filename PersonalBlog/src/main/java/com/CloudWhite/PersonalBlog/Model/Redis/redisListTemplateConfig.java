package com.CloudWhite.PersonalBlog.Model.Redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    // 添加对象到 list 左侧
    public <T> void leftPush(String key, T value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            stringRedisTemplate.opsForList().leftPush(key, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化失败", e);
        }
    }

    // 限制 list 长度（保留最新 size 条）
    public void trim(String key, long size) {
        stringRedisTemplate.opsForList().trim(key, 0, size - 1);
    }

    // 获取指定范围的对象列表
    public <T> List<T> range(String key, long start, long end, Class<T> clazz) {
        List<String> jsonList = stringRedisTemplate.opsForList().range(key, start, end);
        if (jsonList == null) return Collections.emptyList();
        List<T> result = new ArrayList<>();
        for (String json : jsonList) {
            try {
                result.add(objectMapper.readValue(json, clazz));
            } catch (IOException e) {
                // 反序列化失败则跳过
                continue;
            }
        }
        return result;
    }

    // 删除指定值（等于某条消息的 json 字符串）
    public <T> void remove(String key, T value, int count) {
        try {
            String json = objectMapper.writeValueAsString(value);
            stringRedisTemplate.opsForList().remove(key, count, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化失败", e);
        }
    }
}
