package com.CloudWhite.PersonalBlog.Model.Config;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient() {
        // 创建 Redisson 配置对象
        Config config = new Config();
        // 配置 Redis 服务器地址
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        config.useSingleServer().setDatabase(0);
        config.useSingleServer().setPassword("123456");
        // 创建 RedissonClient 实例
        return Redisson.create(config);
    }
}