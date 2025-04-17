package com.CloudWhite.PersonalBlog.Model.Redis;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@Resource
public class redisStringTemplateConfig {
    private StringRedisTemplate stringRedisTemplate;
    //通用设置过期时间
    public void setValueDeadTime(String key,long time,TimeUnit timeUnit){
        ListOperations<String,String> valueOperations = stringRedisTemplate.opsForList();
        stringRedisTemplate.expire(key, time, timeUnit);
    }

    public void setString(String key,String value){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key,value);
    }
    public void setStringAndDeadTime(String key,String value,long time,TimeUnit timeUnit){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key,value,time, timeUnit);
    }

    public String getString(String key){
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }
}
