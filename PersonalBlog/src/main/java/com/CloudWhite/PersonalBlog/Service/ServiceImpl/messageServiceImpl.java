package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import com.CloudWhite.PersonalBlog.Dao.messageDao;
import com.CloudWhite.PersonalBlog.Dao.mybatisDao;
import com.CloudWhite.PersonalBlog.Dao.userDao;
import com.CloudWhite.PersonalBlog.Entity.DTO.CachedMessage;
import com.CloudWhite.PersonalBlog.Entity.DTO.userInfo;
import com.CloudWhite.PersonalBlog.Entity.message;
import com.CloudWhite.PersonalBlog.Model.Redis.redisCommonTemplate;
import com.CloudWhite.PersonalBlog.Model.Redis.redisHashTemplateConfig;
import com.CloudWhite.PersonalBlog.Model.Redis.redisListTemplateConfig;
import com.CloudWhite.PersonalBlog.Model.Redis.redisStringTemplateConfig;
import com.CloudWhite.PersonalBlog.Model.UserContext;
import com.CloudWhite.PersonalBlog.Service.messageService;
import com.CloudWhite.PersonalBlog.Utils.Annotation.NoAop;
import com.CloudWhite.PersonalBlog.Utils.redisUtils;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class messageServiceImpl implements messageService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private messageDao messageDao;
    private userDao userDao;
    private mybatisDao mybatisDao;
    private redisUtils redisUtils;
    private redisStringTemplateConfig redisStringTemplate;
    private redisHashTemplateConfig redisHashTemplate;
    private StringRedisTemplate stringRedisTemplate;
    private redisCommonTemplate redisCommonTemplate;
    private redisListTemplateConfig redisListTemplate;
    @Autowired
    public messageServiceImpl(com.CloudWhite.PersonalBlog.Dao.messageDao messageDao, com.CloudWhite.PersonalBlog.Dao.userDao userDao, com.CloudWhite.PersonalBlog.Dao.mybatisDao mybatisDao, com.CloudWhite.PersonalBlog.Utils.redisUtils redisUtils, redisStringTemplateConfig redisStringTemplate, redisHashTemplateConfig redisHashTemplate, StringRedisTemplate stringRedisTemplate, com.CloudWhite.PersonalBlog.Model.Redis.redisCommonTemplate redisCommonTemplate, redisListTemplateConfig redisListTemplate) {
        this.messageDao = messageDao;
        this.userDao = userDao;
        this.mybatisDao = mybatisDao;
        this.redisUtils = redisUtils;
        this.redisStringTemplate = redisStringTemplate;
        this.redisHashTemplate = redisHashTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisCommonTemplate = redisCommonTemplate;
        this.redisListTemplate = redisListTemplate;
    }

    private String buildRedisKey(Integer userId, Integer friendId) {
        // 保证 key 一致性：userId 和 friendId 排序，避免重复（如 1-2 和 2-1 是同一对话）
        int minId = Math.min(userId, friendId);
        int maxId = Math.max(userId, friendId);
        return "chat:" + minId + ":" + maxId;
    }

    @PostConstruct
    @Order(2)
    public void initAllChatCache() {
        List<CachedMessage> messages = mybatisDao.getAllRecentMessagesForAllFriends();
        Map<String, List<CachedMessage>> grouped = messages.stream()
                .collect(Collectors.groupingBy(msg -> buildRedisKey(msg.getUser_id(), msg.getFriend_id())));

        for (Map.Entry<String, List<CachedMessage>> entry : grouped.entrySet()) {
            String redisKey = entry.getKey();
            redisCommonTemplate.deleteKey(redisKey); // 先删
            List<CachedMessage> messageList = entry.getValue();
            Collections.reverse(messageList); // 保证最旧在左
            for (CachedMessage msg : messageList) {
                redisListTemplate.setObjectRight(redisKey, msg); // 从右加入，右是最新消息
            }
        }
    }

    public List<message> getAllMessages(String friendName){
        String username = UserContext.getCurrentToken().getUsername();
        int userId = UserContext.getCurrentToken().getUserId();
        int friendId = redisHashTemplate.getHashObject("user",friendName, userInfo.class).getUserId();
        String redisKey = buildRedisKey(userId, friendId);

        List<CachedMessage> cachedMessages = redisListTemplate.range(redisKey, 0, -1, CachedMessage.class);

        if (cachedMessages != null && !cachedMessages.isEmpty()) {
            List<message> collect = cachedMessages.stream()
                    .map(msg -> new message(Integer.parseInt(msg.getMessage_id()), msg.getReceiver_name(), msg.getMessage(), msg.getSend_time()))
                    .collect(Collectors.toList());
            return collect;
        }

        // 如果 Redis 缓存为空，查询数据库
        return messageDao.getAllMessages(friendName, username);
    }

    public static String generate9DigitIdFromUUID() {
        UUID uuid = UUID.randomUUID();
        // 计算UUID的哈希值
        int hashCode = uuid.hashCode();
        // 转为正数
        long positiveHash = Math.abs((long) hashCode);
        // 转换为9位数字字符串（不足补零）
        return String.format("%09d", positiveHash % 1_000_000_000L);
    }

    public message sendMessage(message message) {
        String username = UserContext.getCurrentToken().getUsername();
        message.setSenderName(username);
        String formattedTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        message.setSendTime(formattedTime);
        // 写入 Redis
        int senderId = UserContext.getCurrentToken().getUserId();
        int receiverId = redisHashTemplate.getHashObject("user",message.getReceiverName(), userInfo.class).getUserId();
        String redisKey = buildRedisKey(senderId, receiverId);
        String messageId = generate9DigitIdFromUUID();
        CachedMessage cached = new CachedMessage(senderId, receiverId, messageId,message.getSenderName(),
                message.getReceiverName(), message.getMessage(), message.getSendTime());
        redisListTemplate.setObjectRight(redisKey, cached);
        message.setMessageId(Integer.parseInt(messageId));
        return message;
    }
    public List<String[]> getSentMessage(String friendName) {
        String username = UserContext.getCurrentToken().getUsername();
        int userId = UserContext.getCurrentToken().getUserId();
        int friendId = redisHashTemplate.getHashObject("user",friendName, userInfo.class).getUserId();
        String redisKey = buildRedisKey(userId, friendId);

        List<CachedMessage> cached = redisListTemplate.range(redisKey, 0, -1, CachedMessage.class);
        if (cached != null && !cached.isEmpty()) {
            return cached.stream()
                    .filter(msg -> msg.getSender_name().equals(username))
                    .map(msg -> new String[]{msg.getMessage(), msg.getSend_time()})
                    .collect(Collectors.toList());
        }
        return messageDao.getSentMessages(username, friendName);
    }

    public void deleteMessage(int messageId) {
        Set<String> keys = stringRedisTemplate.keys("chat:*");
        for (String key : keys) {
            List<CachedMessage> cachedMessages = redisListTemplate.range(key, 0, -1, CachedMessage.class);
            if (cachedMessages == null || cachedMessages.isEmpty()) continue;
            for (CachedMessage msg : cachedMessages) {
                if (String.valueOf(messageId).equals(msg.getMessage_id())) {
                    redisListTemplate.remove(key, msg,1);
//                    // 删除消息时，同时记录“删除日志”
//                    redisListTemplate.setObjectRight("message");
                    return; // 删除成功后退出
                }
            }
        }
        // 如果未在 Redis 中找到，说明可能已落库，尝试删除数据库
        message message = messageDao.findByMessageId(messageId);
        if (message != null) {
            messageDao.delete(message);
        }
    }


    @NoAop
    public List<String[]> getReceiveMessages(String friendName, String sendTime) {
        if(sendTime==null){return null;}
        String username = UserContext.getCurrentToken().getUsername();
        int userId = UserContext.getCurrentToken().getUserId();
        int friendId = redisHashTemplate.getHashObject("user", friendName, userInfo.class).getUserId();
        String redisKey = buildRedisKey(userId, friendId);

        List<CachedMessage> cached = redisListTemplate.range(redisKey, 0, -1, CachedMessage.class);
        if (cached != null && !cached.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            LocalDateTime targetTime = LocalDateTime.parse(sendTime, formatter);
            List<String[]> collect = cached.stream()
                    .filter(msg -> msg.getSender_name().equals(friendName))
                    .filter(msg -> {
                        LocalDateTime msgTime = LocalDateTime.parse(msg.getSend_time(), formatter);
                        return msgTime.isAfter(targetTime);
                    })
                    .map(msg -> new String[]{String.valueOf(msg.getMessage_id()), msg.getReceiver_name(), msg.getMessage(), msg.getSend_time()})
                    .collect(Collectors.toList());
            return collect;
        }
        return messageDao.getReceiveMessages(friendName, username, sendTime);
    }

    public String getLastNewTime(String friendName){
        String username = UserContext.getCurrentToken().getUsername();
        int userId = UserContext.getCurrentToken().getUserId();
        int friendId = redisHashTemplate.getHashObject("user", friendName, userInfo.class).getUserId();
        String redisKey = buildRedisKey(userId, friendId);

        List<CachedMessage> cachedMessages = redisListTemplate.range(redisKey, -1, -1, CachedMessage.class);
        return cachedMessages.get(0).getSend_time();
    }

    @Scheduled(fixedDelay = (1000*60*60*24))
    public void RestMessageTable(){
//        mybatisDao.lockMessageTable();
//        mybatisDao.ResetMessageAutoIncrement();
//        mybatisDao.unlockMessageTable();
    }

    @Scheduled(fixedDelay = (1000 * 60))
    public void flushRedisToDatabase() {
        Set<String> keys = stringRedisTemplate.keys("chat:*");
        for (String key : keys) {
            List<CachedMessage> cachedMessages = redisListTemplate.range(key, 0, -1, CachedMessage.class);
            if (cachedMessages == null || cachedMessages.isEmpty()) continue;

            for (CachedMessage msg : cachedMessages) {
                // 用业务字段判断是否已存在，避免 message_id 为 null
                boolean exists = messageDao.existsBySenderNameAndReceiverNameAndSendTime(
                        msg.getSender_name(),
                        msg.getReceiver_name(),
                        msg.getSend_time()
                );
                if (!exists) {
                    message message = new message(
                            msg.getSender_name(),
                            msg.getReceiver_name(),
                            msg.getMessage(),
                            msg.getSend_time()
                    );
                    messageDao.save(message);
                }
            }
        }
    }

    // 每天凌晨3点重新构建缓存
    @Scheduled(cron = "0 0 3 * * ?")
    public void scheduledRefreshCache() {
        // 删除旧缓存
        Set<String> keys = stringRedisTemplate.keys("chat:*");
        for (String key : keys) {
            redisCommonTemplate.deleteKey(key);
        }
        // 重新初始化
        initAllChatCache();
    }
}
