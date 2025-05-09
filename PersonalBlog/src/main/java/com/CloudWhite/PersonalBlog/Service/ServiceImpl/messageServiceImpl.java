package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import com.CloudWhite.PersonalBlog.Dao.messageDao;
import com.CloudWhite.PersonalBlog.Dao.mybatisDao;
import com.CloudWhite.PersonalBlog.Dao.userDao;
import com.CloudWhite.PersonalBlog.Entity.message;
import com.CloudWhite.PersonalBlog.Model.UserContext;
import com.CloudWhite.PersonalBlog.Service.messageService;
import com.CloudWhite.PersonalBlog.Utils.Annotation.NoAop;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class messageServiceImpl implements messageService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private messageDao messageDao;
    private userDao userDao;
    private com.CloudWhite.PersonalBlog.Dao.mybatisDao mybatisDao;
    @Autowired
    public messageServiceImpl(com.CloudWhite.PersonalBlog.Dao.messageDao messageDao, com.CloudWhite.PersonalBlog.Dao.userDao userDao, com.CloudWhite.PersonalBlog.Dao.mybatisDao mybatisDao) {
        this.messageDao = messageDao;
        this.userDao = userDao;
        this.mybatisDao = mybatisDao;
    }

    public List<message> getAllMessages(String friendName){
        String username = UserContext.getCurrentToken().getUsername();
        return messageDao.getAllMessages(username,friendName);
    }

    public message sendMessage(message message) {
        String username = UserContext.getCurrentToken().getUsername();
        message.setSenderName(username);
        String formattedTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        message.setSendTime(formattedTime);
        messageDao.save(message);
        message newMessage = messageDao.findBySenderNameAndReceiverNameAndSendTime(message.getSenderName(),message.getReceiverName(),message.getSendTime());
        return newMessage;
    }

    public List<String[]> getSentMessage(String friendName) {
        String username = UserContext.getCurrentToken().getUsername();
        return messageDao.getSentMessages(username,friendName);
    }

    public void deleteMessage(int messageId) {
        message message = messageDao.findByMessageId(messageId);
        messageDao.delete(message);
    }

    @NoAop
    public List<String[]> getReceiveMessages(String friendName,String sendTime){
        String username = UserContext.getCurrentToken().getUsername();
        return  messageDao.getReceiveMessages(friendName,username,sendTime);
    }

    @Scheduled(fixedDelay = (1000*60*60*24))
    public void RestMessageTable(){
        mybatisDao.lockMessageTable();
        mybatisDao.ResetMessageAutoIncrement();
        mybatisDao.unlockMessageTable();
    }
}
