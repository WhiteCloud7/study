package com.CloudWhite.PersonalBlog.Model.Redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class redisCommonTemplate {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void deleteKey(String key) {
        stringRedisTemplate.delete(key);
    }

    public void setExpire(String key, long timeout, TimeUnit unit) {
        stringRedisTemplate.expire(key, timeout, unit);
    }

}
