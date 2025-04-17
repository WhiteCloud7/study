package com.CloudWhite.PersonalBlog.Controller;

import com.CloudWhite.PersonalBlog.Entity.message;
import com.CloudWhite.PersonalBlog.Service.messageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="消息控制器")
@RestController
public class messageController {
    private messageService messageService;
    @Autowired
    public messageController(messageService messgaeService) {
        this.messageService = messgaeService;
    }

    @PostMapping("/sendMessage")
    @Description("发送消息")
    public String sendMessage(@RequestBody message message){
        try {
            messageService.sendMessage(message);
            return "发送成功";
        }catch (Exception e){
            e.printStackTrace();
            return "发送失败";
        }
    }

    @GetMapping("/getSentMessage")
    @Description("得到发送消息")
    public List<String[]> getSentMessage(@RequestParam int userId, @RequestParam int friendId){
        return messageService.getSentMessage(userId,friendId);
    }

    @GetMapping("/deleteMessage")
    public void deleteMessage(@RequestParam int messageId){
        messageService.deleteMessage(messageId);
    }

    @GetMapping("/getSendMessageId")
    public int getSendMessageId(@RequestParam int userId,@RequestParam int friendId,@RequestParam String Time){
        return messageService.getSendMessageId(userId,friendId,Time);
    }

    @GetMapping("/getReceiveMessage")
    public List<String[]> getReceiveMessage(@RequestParam int friendId,@RequestParam int userId){
        return messageService.getReceiveMessages(friendId,userId);
    }
}
