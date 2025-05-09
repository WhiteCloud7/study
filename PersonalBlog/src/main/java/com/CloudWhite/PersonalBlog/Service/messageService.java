package com.CloudWhite.PersonalBlog.Service;

import com.CloudWhite.PersonalBlog.Entity.message;
import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.util.List;

public interface messageService {
    public List<message> getAllMessages(String friendName);
    public message sendMessage(message message);
    public List<String[]> getSentMessage(String friendName);
    public void deleteMessage(int messageId);
    public List<String[]> getReceiveMessages(String friendName,String sendTime);
    public void RestMessageTable();
}
