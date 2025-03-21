package com.WhiteCloud.SpringBootTest.Model.redisService;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class redisServiceString{
    @Resource //注入stringRedisTemplate
    private StringRedisTemplate stringRedisTemplate;

    public void setString(String key,String value){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key,value);
    }

    public Object getString(String key){
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }
}