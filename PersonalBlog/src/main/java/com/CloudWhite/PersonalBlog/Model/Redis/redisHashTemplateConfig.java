package com.CloudWhite.PersonalBlog.Model.Redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class redisHashTemplateConfig {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 存储字符串值
    public void setHash(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> ops = stringRedisTemplate.opsForHash();
        ops.put(key, hashKey, value);
    }

    // 获取字符串值
    public Object getHash(String key, Object hashKey) {
        HashOperations<String, Object, Object> ops = stringRedisTemplate.opsForHash();
        return ops.get(key, hashKey);
    }

    public Object getHashes(String key, List<Object> hashKeys) {
        HashOperations<String, Object, Object> ops = stringRedisTemplate.opsForHash();
        return ops.multiGet(key,hashKeys);
    }

    public void setHashes(String key,Map<String,String> hashes) {
        HashOperations<String,Object ,Object> ops = stringRedisTemplate.opsForHash();
        for(String hashKey : hashes.keySet()){
            ops.put(key,hashKey,hashes.get(hashKey));
        }
    }

    public void setHashes(String key,Map<String,String> hashes,long time,TimeUnit timeUnit) {
        HashOperations<String,Object ,Object> ops = stringRedisTemplate.opsForHash();
        for(String hashKey : hashes.keySet()){
            ops.put(key,hashKey,hashes.get(hashKey));
        }
        stringRedisTemplate.expire(key,time,timeUnit);
    }

    // 存储对象为 JSON 字符串
    public <T> void setHashObject(String key, Object hashKey, T obj) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            setHash(key, hashKey, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化失败", e);
        }
    }

    // 获取 JSON 并反序列化为对象
    public <T> T getHashObject(String key, Object hashKey, Class<T> clazz) {
        Object value = getHash(key, hashKey);
        if (value == null) return null;
        try {
            return objectMapper.readValue(value.toString(), clazz);
        } catch (IOException e) {
            throw new RuntimeException("反序列化失败", e);
        }
    }

    // 可选：一次存储多个字段（批量 put）
    public void putAllHash(String key, Map<String, String> map) {
        stringRedisTemplate.opsForHash().putAll(key, map);
    }

    public void deleteHashKey(String key,String hashKey){
        stringRedisTemplate.opsForHash().delete(key,hashKey);
    }

    public void deleteHashKey(String key,List<String> hashKeys){
        stringRedisTemplate.opsForHash().delete(key,hashKeys);
    }
}
