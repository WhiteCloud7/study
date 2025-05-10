package com.CloudWhite.PersonalBlog.Controller;

import com.CloudWhite.PersonalBlog.Entity.message;
import com.CloudWhite.PersonalBlog.Model.ResponseEntity;
import com.CloudWhite.PersonalBlog.Service.messageService;
import com.CloudWhite.PersonalBlog.Utils.Annotation.LoginRequired;
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
    @LoginRequired
    public ResponseEntity sendMessage(@RequestBody message message){
        message newMessage = messageService.sendMessage(message);
        if(newMessage!=null)
            return new ResponseEntity(newMessage);
        else
            return new ResponseEntity("500","发送失败",null);
    }

    @GetMapping("/getSentMessage")
    @Description("得到发送消息")
    @LoginRequired
    public ResponseEntity getSentMessage(@RequestParam String friendName){
        return new ResponseEntity(messageService.getSentMessage(friendName));
    }

    @GetMapping("/getAllMessages")
    @LoginRequired
    public ResponseEntity getAllMessages(String friendName){
        return new ResponseEntity(messageService.getAllMessages(friendName));
    }

    @GetMapping("/deleteMessage")
    @LoginRequired
    public ResponseEntity deleteMessage(@RequestParam int messageId){
        messageService.deleteMessage(messageId);
        return new ResponseEntity();
    }

    @GetMapping("/getReceiveMessage")
    @LoginRequired
    public ResponseEntity getReceiveMessage(String friendName,String currentNewMessageTime){
        return new ResponseEntity(messageService.getReceiveMessages(friendName,currentNewMessageTime));
    }

    @GetMapping("/getLastNewTime")
    @LoginRequired
    public ResponseEntity getLastNewTime(String friendName){
        return new ResponseEntity(messageService.getLastNewTime(friendName));
    }
}
