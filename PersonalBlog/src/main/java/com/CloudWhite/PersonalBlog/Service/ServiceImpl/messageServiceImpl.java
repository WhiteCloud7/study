package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import com.CloudWhite.PersonalBlog.Dao.messageDao;
import com.CloudWhite.PersonalBlog.Dao.mybatisDao;
import com.CloudWhite.PersonalBlog.Dao.userDao;
import com.CloudWhite.PersonalBlog.Entity.DTO.CachedMessage;
import com.CloudWhite.PersonalBlog.Entity.DTO.messageDto;
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
        Set<String> keys = stringRedisTemplate.keys("chat:*");
        for (String key : keys) {
            redisCommonTemplate.deleteKey(key);
        }
        List<CachedMessage> messages = mybatisDao.getAllRecentMessagesForAllFriends();
        Map<String, List<CachedMessage>> grouped = messages.stream()
                .collect(Collectors.groupingBy(msg -> buildRedisKey(msg.getUser_id(), msg.getFriend_id())));

        for (Map.Entry<String, List<CachedMessage>> entry : grouped.entrySet()) {
            String redisKey = entry.getKey();
            List<CachedMessage> messageList = entry.getValue();
            Collections.reverse(messageList);
            for (CachedMessage msg : messageList) {
                redisListTemplate.setObjectRight(redisKey, msg); // 从右加入，右是最新消息
            }
        }
    }

    public List<messageDto> getAllMessages(String friendName){
        String username = UserContext.getCurrentToken().getUsername();
        int userId = UserContext.getCurrentToken().getUserId();
        int friendId = redisHashTemplate.getHashObject("user",friendName, userInfo.class).getUserId();
        String redisKey = buildRedisKey(userId, friendId);
        List<CachedMessage> cachedMessages = redisListTemplate.range(redisKey, 0, -1, CachedMessage.class);
        if (cachedMessages != null && !cachedMessages.isEmpty()) {
            List<messageDto> collect = cachedMessages.stream()
                    .map(msg -> new messageDto(msg.getMessage_id(), msg.getReceiver_name(), msg.getMessage(), msg.getSend_time()))
                    .collect(Collectors.toList());
            return collect;
        }
        List<message> messages = messageDao.getAllMessages(friendName, username);
        List<messageDto> messageDtos = new ArrayList<>();
        for(message message : messages){
            messageDto messageDto = new messageDto(String.valueOf(message.getMessageId()),message.getReceiverName(),message.getMessage(),message.getSendTime());
            messageDtos.add(messageDto);
        }
        return messageDtos;
    }

    public List<messageDto> getOlderMessages(String friendName, String beforeTime){
        int friendId = userDao.findUserIdByUserName(friendName);
        int userId = UserContext.getCurrentToken().getUserId();
        String username = UserContext.getCurrentToken().getUsername();
        List<message> messages = messageDao.getOlderMessage(username,friendName,beforeTime);
        List<messageDto> messageDtos = new ArrayList<>();
        for(message message : messages){
            messageDto messageDto = new messageDto(String.valueOf(message.getMessageId()),message.getReceiverName(),message.getMessage(),message.getSendTime());
            messageDtos.add(messageDto);
            CachedMessage cm = new CachedMessage(userId, friendId, String.valueOf(message.getMessageId()),message.getSenderName(), message.getReceiverName(), message.getMessage(), message.getSendTime());
            redisListTemplate.setObjectRight(buildRedisKey(userId, friendId), cm);
        }
        return messageDtos;
    }

    public messageDto sendMessage(messageDto messageDto) {
        String username = UserContext.getCurrentToken().getUsername();
        String formattedTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        messageDto.setSendTime(formattedTime);
        // 写入 Redis
        int senderId = UserContext.getCurrentToken().getUserId();
        int receiverId = redisHashTemplate.getHashObject("user",messageDto.getReceiverName(), userInfo.class).getUserId();
        String redisKey = buildRedisKey(senderId, receiverId);
        String messageId = UUID.randomUUID().toString();
        CachedMessage cached = new CachedMessage(senderId, receiverId, messageId,username,
                messageDto.getReceiverName(), messageDto.getMessage(), messageDto.getSendTime());
        redisListTemplate.setObjectRight(redisKey, cached);
        messageDto.setMessageId(messageId);
        return messageDto;
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
                    .map(msg -> new String[]{msg.getMessage_id(),msg.getReceiver_name(),msg.getMessage(), msg.getSend_time()})
                    .collect(Collectors.toList());
        }
        return messageDao.getSentMessages(username, friendName);
    }

    public void deleteMessage(String messageId,String receiveName,String sendTime) {
        String username = UserContext.getCurrentToken().getUsername();
        Set<String> keys = stringRedisTemplate.keys("chat:*");
        for (String key : keys) {
            List<CachedMessage> cachedMessages = redisListTemplate.range(key, 0, -1, CachedMessage.class);
            if (cachedMessages == null || cachedMessages.isEmpty()) continue;
            for (CachedMessage msg : cachedMessages) {
                if (messageId.equals(msg.getMessage_id())) {
                    redisListTemplate.remove(key, msg,1);
//                    // 删除消息时，同时记录“删除日志”
//                    redisListTemplate.setObjectRight("message");
                    return; // 删除成功后退出
                }
            }
        }
        // 如果未在 Redis 中找到，说明可能已落库，尝试删除数据库
        message message = messageDao.getDeleteMessage(username,receiveName,sendTime);
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

    @NoAop
    public String getLastNewTime(String friendName) {
        int userId = UserContext.getCurrentToken().getUserId();
        int friendId = redisHashTemplate.getHashObject("user", friendName, userInfo.class).getUserId();
        String redisKey = buildRedisKey(userId, friendId);

        List<CachedMessage> cached = redisListTemplate.range(redisKey, -1, -1, CachedMessage.class);
        if (cached == null || cached.isEmpty()) {
            return null;
        }
        return cached.get(0).getSend_time();
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
