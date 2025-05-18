package com.CloudWhite.PersonalBlog.Controller;
import com.CloudWhite.PersonalBlog.Entity.notice.notice;
import com.CloudWhite.PersonalBlog.Model.ResponseEntity;
import com.CloudWhite.PersonalBlog.Service.noticeService;
import com.CloudWhite.PersonalBlog.Utils.Annotation.LoginRequired;
import com.CloudWhite.PersonalBlog.Utils.Annotation.PermissionRequired;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name="通知控制器")
public class noticeController {
    noticeService noticeService;
    @Autowired
    public noticeController(noticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/getNoticeInfo")
    @Description("得到通知是否点赞或收藏的信息")
    @LoginRequired
    public ResponseEntity getNoticeInfo(int noticeId){
        return new ResponseEntity(noticeService.getNoticeInfo(noticeId));
    }

    @GetMapping("/getNotice")
    @Description("得到通知信息")
    public List<notice> getNotice(){
        return noticeService.getNoticeList();
    }

    @GetMapping("/updateVisitCount")
    @Description("更新访问数")
    public void updateVisitCount(int noticeId){
        noticeService.addVisitCount(noticeId);
    }

    @GetMapping("/updateLikeCount")
    @LoginRequired
    @Description("更新点赞数")
    public ResponseEntity updateLikeCount(int noticeId){
        noticeService.addLike(noticeId);
        return new ResponseEntity();
    }

    @PostMapping("/saveNotice")
    @Description("保存更改的通知")
    @PermissionRequired(type = "admin")
    public ResponseEntity updateNotice(@RequestParam int noticeId,@RequestParam String title,@RequestParam String messageText){
        try {
            noticeService.saveNotice(noticeId,title,messageText);
            return new ResponseEntity("200","保存成功",null);
        }catch (Exception e){
            return new ResponseEntity("200","保存失败",null);
        }
    }

    @PostMapping("/newNotice")
    @PermissionRequired(type = "admin")
    public ResponseEntity newNotice(@RequestParam String title,@RequestParam String noticeContent){
        try {
            noticeService.newNotice(title,noticeContent);
            return new ResponseEntity("200","发布成功",null);
        }catch (Exception e){
            return new ResponseEntity("200","发布失败",null);
        }
    }
    @GetMapping("/deleteNotice")
    @PermissionRequired(type = "admin")
    public ResponseEntity deleteNotice(int noticeId){
        noticeService.deleteNotice(noticeId);
        return new ResponseEntity();
    }
}
