package com.CloudWhite.PersonalBlog.Model.Redis;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@Resource
public class redisListTemplateConfig {
    private StringRedisTemplate stringRedisTemplate;

    public void setStringLeft(String key,String value){
        ListOperations<String,String> valueOperations = stringRedisTemplate.opsForList();
        valueOperations.leftPush(key, value);
    }
    public void setStringRight(String key,String value){
        ListOperations<String,String> valueOperations = stringRedisTemplate.opsForList();
        valueOperations.rightPush(key, value);
    }

    public String getListLeft(String key){
        ListOperations<String,String> valueOperations = stringRedisTemplate.opsForList();
        return valueOperations.leftPop(key);
    }
    public String getListRight(String key){
        ListOperations<String,String> valueOperations = stringRedisTemplate.opsForList();
        return valueOperations.rightPop(key);
    }
}
