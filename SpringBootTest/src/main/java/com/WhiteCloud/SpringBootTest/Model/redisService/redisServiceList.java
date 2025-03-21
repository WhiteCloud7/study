package com.WhiteCloud.SpringBootTest.Model.redisService;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class redisServiceList {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void setList(String key,String value){
        ListOperations<String,String> listOperations = stringRedisTemplate.opsForList();
        listOperations.leftPush(key,value);
    }

    public String getList(String key){
        ListOperations<String,String> listOperations = stringRedisTemplate.opsForList();
        return listOperations.leftPop(key);
    }
}
