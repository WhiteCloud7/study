package com.CloudWhite.PersonalBlog.Controller;
import com.CloudWhite.PersonalBlog.Entity.notice.notice;
import com.CloudWhite.PersonalBlog.Entity.notice.noticeInfo;
import com.CloudWhite.PersonalBlog.Service.noticeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Primary;
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
    public noticeInfo getNoticeInfo(@RequestParam int noticeId,@RequestParam int userId){
        return noticeService.getNoticeInfo(noticeId,userId);
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
    @Description("更新点赞数")
    public void updateLikeCount(int noticeId,int userId){
        noticeService.addLike(noticeId,userId);
    }

    @GetMapping("/updateStarCount")
    @Description("更新收藏数")
    public void updateStarCount(int noticeId,int userId){
        noticeService.addStar(noticeId,userId);
    }

    @PostMapping("/saveNotice")
    @Description("保存更改的通知")
    public String updateNotice(@RequestParam int noticeId,@RequestParam String title,@RequestParam String messageText){
        try {
            noticeService.saveNotice(noticeId,title,messageText);
            return "保存成功";
        }catch (Exception e){
            return "保存失败";
        }
    }
}
