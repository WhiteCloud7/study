package com.WhiteCloud.SpringBootTest.Model.redisService;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class redisServiceHash {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void setHash(String key,Object hashKey,Object value){
        HashOperations<String,Object,Object> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put(key,hashKey,value);
    }

    public Object getHash(String key,String hashKey){
        HashOperations<String,Object,Object> hashOperations = stringRedisTemplate.opsForHash();
        return hashOperations.get(key,hashKey);
    }
}
