package com.CloudWhite.PersonalBlog.Controller;

import com.CloudWhite.PersonalBlog.Entity.friendList;
import com.CloudWhite.PersonalBlog.Model.ResponseEntity;
import com.CloudWhite.PersonalBlog.Service.friendListService;
import com.CloudWhite.PersonalBlog.Utils.Annotation.LoginRequired;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name="好友列表控制器")
public class friendListController {
    private friendListService friendListService;
    @Autowired
    public friendListController(friendListService friendListService) {
        this.friendListService = friendListService;
    }

    @GetMapping("/getFriendBasicInfo")
    @LoginRequired
    @Description("得到好友基础信息，即头像和昵称")
    public ResponseEntity getFriendBasicInfo(){
        return new ResponseEntity(friendListService.getFriendBasicInfo());
    }

    @GetMapping("/getFriendBasicInfoByUsername")
    @LoginRequired
    public ResponseEntity getFriendBasicInfoByUsername(String friendName){
        return new ResponseEntity(friendListService.getFriendBasicInfoByUsername(friendName));
    }
}
