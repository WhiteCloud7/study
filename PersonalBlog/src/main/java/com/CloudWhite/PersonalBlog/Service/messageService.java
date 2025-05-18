package com.CloudWhite.PersonalBlog.Service;

import com.CloudWhite.PersonalBlog.Entity.DTO.messageDto;
import com.CloudWhite.PersonalBlog.Entity.message;
import com.CloudWhite.PersonalBlog.Model.ResponseEntity;

import java.util.List;

public interface messageService {
    List<messageDto> getAllMessages(String friendName);
    public messageDto sendMessage(messageDto messageDto);
    public List<String[]> getSentMessage(String friendName);
    public void deleteMessage(String messageId,String receiveName,String sendTime);
    public List<String[]> getReceiveMessages(String friendName, String sendTime);
    public void RestMessageTable();

    public String getLastNewTime(String friendName);
    public List<messageDto> getOlderMessages(String friendName, String beforeTime);
}
