package com.CloudWhite.PersonalBlog.Model.Redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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

    public <T> void saveObjectWithSort(String hashKey,
                                       String objKey,
                                       T obj,
                                       String zsetKey,
                                       double score) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            // 1) 写 Hash（存完整对象）
            stringRedisTemplate.opsForHash().put(hashKey, objKey, json);
            // 2) 写 ZSET（用 score 排序，value 放 objKey 或 json）
            stringRedisTemplate.opsForZSet().add(zsetKey, objKey, score);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化失败", e);
        }
    }
    public <T> List<T> listByScoreAsc(String zsetKey,
                                      String hashKey,
                                      long start,
                                      long end,
                                      Class<T> clazz) {

        // 1) 先从 ZSET 按 score 取 objKey 列表
        Set<String> objKeys = stringRedisTemplate.opsForZSet()
                .range(zsetKey, start, end);
        if (objKeys == null || objKeys.isEmpty()) {
            return Collections.emptyList();
        }
        // 2) 再批量到 Hash 中取对象详情
        List<Object> jsonList =
                stringRedisTemplate.opsForHash().multiGet(hashKey, new ArrayList<>(objKeys));
        List<T> result = new ArrayList<>();
        for (Object json : jsonList) {
            if (json != null) {
                try {
                    result.add(objectMapper.readValue(json.toString(), clazz));
                } catch (JsonProcessingException ignored) {}
            }
        }
        return result;
    }
    public <T> List<T> listByScoreDesc(String zsetKey,
                                      String hashKey,
                                      long start,
                                      long end,
                                      Class<T> clazz) {

        // 1) 先从 ZSET 按 score 取 objKey 列表
        Set<String> objKeys = stringRedisTemplate.opsForZSet()
                .reverseRange(zsetKey, start, end);
        if (objKeys == null || objKeys.isEmpty()) {
            return Collections.emptyList();
        }
        // 2) 再批量到 Hash 中取对象详情
        List<Object> jsonList =
                stringRedisTemplate.opsForHash().multiGet(hashKey, new ArrayList<>(objKeys));
        List<T> result = new ArrayList<>();
        for (Object json : jsonList) {
            if (json != null) {
                try {
                    result.add(objectMapper.readValue(json.toString(), clazz));
                } catch (JsonProcessingException ignored) {}
            }
        }
        return result;
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

    public <T> void setHashObjectList(String key, List<T> list, Function<T, Object> hashKeyMapper) {
        if (list == null || list.isEmpty()) return;

        Map<Object, String> map = new HashMap<>();
        for (T obj : list) {
            try {
                Object hashKey = hashKeyMapper.apply(obj);
                String json = objectMapper.writeValueAsString(obj);
                map.put(hashKey, json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("序列化失败", e);
            }
        }

        stringRedisTemplate.opsForHash().putAll(key, map);
    }

    public <T> List<T> getHashObjectList(String key, Collection<?> hashKeys, Class<T> clazz) {
        if (hashKeys == null || hashKeys.isEmpty()) return Collections.emptyList();

        List<Object> values = stringRedisTemplate.opsForHash().multiGet(key, new ArrayList<>(hashKeys));
        List<T> resultList = new ArrayList<>();

        for (Object value : values) {
            if (value == null) continue;
            try {
                T obj = objectMapper.readValue(value.toString(), clazz);
                resultList.add(obj);
            } catch (IOException e) {
                throw new RuntimeException("反序列化失败", e);
            }
        }
        return resultList;
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

    public Set<String> scanKeys(String pattern) {
        Set<String> keys = new HashSet<>();
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(1000).build();
        Cursor<byte[]> cursor = stringRedisTemplate.getConnectionFactory().getConnection().scan(options);
        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));
        }
        cursor.close();
        return keys;
    }
}
