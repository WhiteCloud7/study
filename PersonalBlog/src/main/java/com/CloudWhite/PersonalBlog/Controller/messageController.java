package com.CloudWhite.PersonalBlog.Controller;

import com.CloudWhite.PersonalBlog.Entity.DTO.messageDto;
import com.CloudWhite.PersonalBlog.Entity.message;
import com.CloudWhite.PersonalBlog.Model.ResponseEntity;
import com.CloudWhite.PersonalBlog.Service.messageService;
import com.CloudWhite.PersonalBlog.Utils.Annotation.LoginRequired;
import com.CloudWhite.PersonalBlog.Utils.Annotation.RateLimitForAll;
import com.CloudWhite.PersonalBlog.Utils.Annotation.RateLimitForCurrent;
import com.CloudWhite.PersonalBlog.Utils.Annotation.RateLimitForUnlogin;
import io.swagger.v3.oas.annotations.Operation;
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

//    @PostMapping("/sendMessage")
//    @Description("发送消息")
//    @LoginRequired
//    public ResponseEntity sendMessage(@RequestBody messageDto messageDto){
//        messageDto newMessage = messageService.sendMessage(messageDto);
//        if(newMessage!=null)
//            return new ResponseEntity(newMessage);
//        else
//            return new ResponseEntity("500","发送失败",null);
//    }

//    @GetMapping("/getSentMessage")
//    @Description("得到发送消息")
//    @LoginRequired
//    public ResponseEntity getSentMessage(@RequestParam String friendName){
//        return new ResponseEntity(messageService.getSentMessage(friendName));
//    }

    @GetMapping("/getAllMessages")
    @LoginRequired
    @RateLimitForUnlogin(maxRequests = 10,seconds = 10)
    @RateLimitForAll(maxRequests = 1000)
    @RateLimitForCurrent(maxRequests = 3 , seconds = 5)
    @Operation(description = "得到所有信息")
    public ResponseEntity getAllMessages(String friendName){
        return new ResponseEntity(messageService.getAllMessages(friendName));
    }

    @GetMapping("/deleteMessage")
    @LoginRequired
    @RateLimitForCurrent(maxRequests = 60)
    @Operation(description = "删除信息")
    public ResponseEntity deleteMessage(@RequestParam String messageId,@RequestParam String receiveName,@RequestParam String sendTime){
        messageService.deleteMessage(messageId, receiveName,sendTime);
        return new ResponseEntity();
    }

//    @GetMapping("/getReceiveMessage")
//    @LoginRequired
//    public ResponseEntity getReceiveMessage(String friendName,String currentNewMessageTime){
//        return new ResponseEntity(messageService.getReceiveMessages(friendName,currentNewMessageTime));
//    }
//
//    @GetMapping("/getLastNewTime")
//    @LoginRequired
//    public ResponseEntity getLastNewTime(String friendName){
//        return new ResponseEntity(messageService.getLastNewTime(friendName));
//    }
    @GetMapping("/getOlderMessages")
    @LoginRequired
    @RateLimitForAll(maxRequests = 1000)
    @RateLimitForCurrent(maxRequests = 60)
    @Operation(description = "得到过去消息")
    public ResponseEntity getOlderMessages(String friendName,String beforeTime){
        return new ResponseEntity(messageService.getOlderMessages(friendName,beforeTime));
    }
}
