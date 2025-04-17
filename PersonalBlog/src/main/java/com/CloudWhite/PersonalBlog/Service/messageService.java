package com.CloudWhite.PersonalBlog.Service;

import com.CloudWhite.PersonalBlog.Entity.message;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface messageService {
    public void sendMessage(message message);
    public List<String[]> getSentMessage(int userId, int friendId);
    public void deleteMessage(int messageId);
    public int getSendMessageId(int userId,int friendId,String sendTime);
    public List<String[]> getReceiveMessages(int friendId,int userId);
}
