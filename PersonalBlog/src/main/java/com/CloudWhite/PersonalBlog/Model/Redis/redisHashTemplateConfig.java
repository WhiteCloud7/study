package com.CloudWhite.PersonalBlog.Model.Redis;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

@Resource
public class redisHashTemplateConfig {
    private StringRedisTemplate stringRedisTemplate;

    public void setHash(String key,Object hashKey,Object value){
        HashOperations<String, Object, Object> valueOperations = stringRedisTemplate.opsForHash();
        valueOperations.put(key, hashKey,value);
    }

    public void getHash(String key,Object hashKey){
        HashOperations<String, Object, Object> valueOperations = stringRedisTemplate.opsForHash();
        valueOperations.get(key, hashKey);
    }
}
