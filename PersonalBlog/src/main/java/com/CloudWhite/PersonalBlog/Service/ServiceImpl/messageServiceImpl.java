package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import com.CloudWhite.PersonalBlog.Dao.messageDao;
import com.CloudWhite.PersonalBlog.Entity.message;
import com.CloudWhite.PersonalBlog.Service.messageService;
import com.CloudWhite.PersonalBlog.Utils.utils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class messageServiceImpl implements messageService {
    @Autowired
    private utils utils;
    private messageDao messageDao;
    @Autowired
    public messageServiceImpl(messageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Transactional
    public void sendMessage(message message) {
        messageDao.save(message);
    }

    public List<String[]> getSentMessage(int userId, int friendId) {
        return messageDao.getSentMessages(userId,friendId);
    }

    public void deleteMessage(int messageId) {
        message message = messageDao.findByMessageId(messageId);
        messageDao.delete(message);
    }

    public int getSendMessageId(int userId, int friendId, String sendTime) {
        return messageDao.getSentMessageId(userId,friendId,sendTime);
    }

    public List<String[]> getReceiveMessages(int friendId,int userId){
        return  messageDao.getReceiveMessages(friendId,userId);
    }
}
