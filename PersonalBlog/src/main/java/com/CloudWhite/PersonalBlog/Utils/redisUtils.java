package com.CloudWhite.PersonalBlog.Utils;

import com.CloudWhite.PersonalBlog.Model.Redis.redisCommonTemplate;
import com.CloudWhite.PersonalBlog.Model.Redis.redisHashTemplateConfig;
import com.CloudWhite.PersonalBlog.Model.Redis.redisListTemplateConfig;
import com.CloudWhite.PersonalBlog.Model.Redis.redisStringTemplateConfig;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class redisUtils {
    private RedissonClient redissonClient;
    private redisStringTemplateConfig redisStringTemplate;
    private redisHashTemplateConfig redisHashTemplate;
    private redisListTemplateConfig redisListTemplate;
    private redisCommonTemplate redisCommonTemplate;
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    public redisUtils(RedissonClient redissonClient, redisStringTemplateConfig redisStringTemplate, redisHashTemplateConfig redisHashTemplate, redisListTemplateConfig redisListTemplate, StringRedisTemplate stringRedisTemplate,redisCommonTemplate redisCommonTemplate) {
        this.redissonClient = redissonClient;
        this.redisStringTemplate = redisStringTemplate;
        this.redisHashTemplate = redisHashTemplate;
        this.redisListTemplate = redisListTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisCommonTemplate = redisCommonTemplate;
    }

    public <T> T queryWithCache(
            String key,
            Class<T> clazz,
            Supplier<T> dbQuery,
            long time,
            TimeUnit timeUnit
    ) {
        // 先查缓存
        T cache = redisStringTemplate.getObject(key, clazz);
        if (cache != null) {
            return cache;
        }
        T result = dbQuery.get();
        if(result==null){
           redisStringTemplate.setObject(key,null);
            redisCommonTemplate.setExpire(key, 60, TimeUnit.SECONDS);
           return null;
        }
        redisStringTemplate.setObject(key,null);
        redisCommonTemplate.setExpire(key,time,timeUnit);
        return result;
    }

    public <T> T queryWithCache(
            String key,
            Class<T> clazz,
            Supplier<T> dbQuery
            ) {
        // 先查缓存
        T cache = redisStringTemplate.getObject(key, clazz);
        if (cache != null) {
            return cache;
        }
        T result = dbQuery.get();
        if(result==null){
            redisStringTemplate.setObject(key,null);
            redisCommonTemplate.setExpire(key, 60, TimeUnit.SECONDS);
            return null;
        }
        redisStringTemplate.setObject(key,null);
        return result;
    }

    public <T> T queryWithHashCache(
            String key,
            String hashKey,
            Class<T> clazz,
            Supplier<T> dbQuery,
            long time,
            TimeUnit timeUnit
    ) {
        // 先查缓存
        T cache = redisHashTemplate.getHashObject(key,hashKey, clazz);
        if (cache != null) {
            return cache;
        }
        T result = dbQuery.get();
        if(result==null){
            redisHashTemplate.setHashObject(key,hashKey,null);
            redisCommonTemplate.setExpire(key, 60, TimeUnit.SECONDS);
            return null;
        }
        redisStringTemplate.setObject(key,null);
        redisCommonTemplate.setExpire(key,time,timeUnit);
        return result;
    }

    public <T> List<T> queryWithHashCaches(
            Collection<String> keys,
            Collection<String> hashKeys,
            Class<T> clazz,
            Supplier<Map<String, T>> dbQuery,
            long ttl,
            TimeUnit unit
    ) {
        List<T> cache = redisStringTemplate.getObjects(keys, clazz);
        if (cache != null && cache.size() == keys.size()) {
            return cache;
        }

        Map<String, T> dbResult = dbQuery.get();
        if (dbResult == null || dbResult.isEmpty()) {
            // 防止穿透：写空值
            for (String key : keys) {
                redisStringTemplate.setObject(key, "");
                stringRedisTemplate.expire(key, 60, TimeUnit.SECONDS);
            }
            return Collections.emptyList();
        }

        redisStringTemplate.setObjects(dbResult, ttl, unit);
        return new ArrayList<>(dbResult.values());
    }

    public <T> List<T> queryWithCaches(
            Collection<String> keys,
            Class<T> clazz,
            Supplier<Map<String, T>> dbQuery
    ) {
        List<T> cache = redisStringTemplate.getObjects(keys, clazz);
        if (cache != null && cache.size() == keys.size()) {
            return cache;
        }

        Map<String, T> dbResult = dbQuery.get();
        if (dbResult == null || dbResult.isEmpty()) {
            // 防止穿透：写空值
            for (String key : keys) {
                redisStringTemplate.setObject(key, "");
                stringRedisTemplate.expire(key, 60, TimeUnit.SECONDS);
            }
            return Collections.emptyList();
        }

        redisStringTemplate.setObjects(dbResult);
        return new ArrayList<>(dbResult.values());
    }

    public <T> void updateKeepFit(
            String key,
            Supplier<T> dbQuery
    ) {
        // 加锁，保证同一时刻只有一个线程进行删除缓存和数据库更新
        RLock lock = redissonClient.getLock(key + ":lock");
        boolean lockAcquired = false;
        try {
            lockAcquired = lock.tryLock(0, 60, TimeUnit.SECONDS); // 通过锁保护临界区
            if (!lockAcquired) {
                // 如果没有获取到锁，则可以选择直接返回，或者抛出异常等
                return;
            }
            // 先删缓存
            redisCommonTemplate.deleteKey(key);
            // 更新数据库
            dbQuery.get();
            // 再次删除缓存（可选，适用于确保一致性）
            redisCommonTemplate.deleteKey(key);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 安全释放锁
            if (lockAcquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public <T> void updateKeepFit(
            String key,
            Supplier<T> dbQuery,
            long time,
            TimeUnit timeUnit
    ) {
        // 加锁，保证同一时刻只有一个线程进行删除缓存和数据库更新
        RLock lock = redissonClient.getLock(key + ":lock");
        boolean lockAcquired = false;
        try {
            lockAcquired = lock.tryLock(0, 60, TimeUnit.SECONDS); // 通过锁保护临界区
            if (!lockAcquired) {
                // 如果没有获取到锁，则可以选择直接返回，或者抛出异常等
                return;
            }
            // 先删缓存
            redisCommonTemplate.deleteKey(key);
            // 更新数据库
            dbQuery.get();
            // 改用设置过期时间
            redisCommonTemplate.setExpire(key,time,timeUnit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 安全释放锁
            if (lockAcquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public <T> T queryWithPassThrough(
            String key,
            Class<T> clazz,
            Supplier<T> dbQuery,
            long time,
            TimeUnit timeUnit,
            String lockKey,
            String bloomKey,
            int bloomId
    ) {
        RBloomFilter<Integer> bloomFilter = redissonClient.getBloomFilter(bloomKey);
        if (!bloomFilter.contains(bloomId)) {
            return null;
        }

        // 先查缓存
        T cache = redisStringTemplate.getObject(key, clazz);
        if (cache != null) {
            return cache;
        }

        RLock lock = redissonClient.getLock(lockKey);
        boolean lockSuccess = false;

        try {
            lockSuccess = lock.tryLock(0, 60, TimeUnit.SECONDS);
            if (!lockSuccess) {
                // 拿不到锁，等待其他线程处理完
                Thread.sleep(50); // 短暂等待避免频繁打库
                return redisStringTemplate.getObject(key, clazz);
            }

            // 双重检查缓存
            T cacheAgain = redisStringTemplate.getObject(key, clazz);
            if (cacheAgain != null) {
                return cacheAgain;
            }

            T result = dbQuery.get();
            if (result == null) {
                // 缓存空值防止穿透
                redisStringTemplate.setObject(key, null);
                redisCommonTemplate.setExpire(key, 60, TimeUnit.SECONDS);
                return null;
            }

            // 正常数据缓存
            redisStringTemplate.setObject(key, result);
            redisCommonTemplate.setExpire(key,time,timeUnit);
            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            if (lockSuccess && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public <T> T queryWithPassThrough(
            String key,
            Class<T> clazz,
            Supplier<T> dbQuery,
            String lockKey,
            String bloomKey,
            int bloomId
    ) {
        RBloomFilter<Integer> bloomFilter = redissonClient.getBloomFilter(bloomKey);
        if (!bloomFilter.contains(bloomId)) {
            return null;
        }

        // 先查缓存
        T cache = redisStringTemplate.getObject(key, clazz);
        if (cache != null) {
            return cache;
        }

        RLock lock = redissonClient.getLock(lockKey);
        boolean lockSuccess = false;

        try {
            lockSuccess = lock.tryLock(0, 60, TimeUnit.SECONDS);
            if (!lockSuccess) {
                // 拿不到锁，等待其他线程处理完
                Thread.sleep(50); // 短暂等待避免频繁打库
                return redisStringTemplate.getObject(key, clazz);
            }

            // 双重检查缓存
            T cacheAgain = redisStringTemplate.getObject(key, clazz);
            if (cacheAgain != null) {
                return cacheAgain;
            }

            T result = dbQuery.get();
            if (result == null) {
                // 缓存空值防止穿透
                redisStringTemplate.setObject(key, null);
                redisCommonTemplate.setExpire(key, 5, TimeUnit.MINUTES);
                return null;
            }

            // 正常数据缓存
            redisStringTemplate.setObject(key, result);
            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            if (lockSuccess && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
