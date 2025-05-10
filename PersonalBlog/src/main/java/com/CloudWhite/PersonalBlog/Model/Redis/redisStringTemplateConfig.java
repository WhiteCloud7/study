package com.CloudWhite.PersonalBlog.Model.Redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Time;
import java.util.*;
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
    public void setString(String key, String value, long time, TimeUnit unit) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key, value, time, unit);
    }

    // 获取字符串值
    public String getString(String key) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        return ops.get(key);
    }

    public List<String> getStrings(Collection<String> keys) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        return ops.multiGet(keys);
    }

    public void setStrings(Map<String,String> strs) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.multiSet(strs);
    }

    public void setStrings(Map<String,String> strs,long time,TimeUnit timeUnit) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.multiSet(strs);
        for(String key : strs.keySet()){
            stringRedisTemplate.expire(key,time,timeUnit);
        }
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

    public <T> void setObjects(Map<String,T> objs) {
        Set<String> keys = objs.keySet();
        Map<String,String> result = new HashMap();
        try{
            for (String key : keys) {
                if (objs.get(key) != null) {
                    result.put(key,objectMapper.writeValueAsString(objs.get(key)));
                }
            }
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        stringRedisTemplate.opsForValue().multiSet(result);
    }

    public <T> void setObjects(Map<String,T> objs, long time, TimeUnit timeUnit) {
        Set<String> keys = objs.keySet();
        Map<String,String> result = new HashMap();
        try{
            for (String key : keys) {
                if (objs.get(key) != null) {
                    result.put(key,objectMapper.writeValueAsString(objs.get(key)));

                }
            }
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        stringRedisTemplate.opsForValue().multiSet(result);
        for(String key : keys){
            stringRedisTemplate.expire(key,time,timeUnit);
        }
    }

    public <T> List<T> getObjects(Collection<String> keys, Class<T> clazz) {
        List<String> values = stringRedisTemplate.opsForValue().multiGet(keys);
        List<T> result = new ArrayList<>();
        try{
            for (String val : values) {
                if (val != null) {
                    result.add(objectMapper.readValue(val,clazz));
                }
            }
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return result;
    }
}
