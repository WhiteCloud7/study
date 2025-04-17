package com.CloudWhite.PersonalBlog.Controller;

import com.CloudWhite.PersonalBlog.Service.userService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="管理员控制器")
public class adminController {
    userService userService;
    @Autowired
    public adminController(userService userService) {
        this.userService = userService;
    }

    @GetMapping("/adminAvatar")
    @Description("得到管理员年龄")
    public String getAdminAge(int userId) {
        return userService.getUserByUserId(userId).getAvatar_src();
    }
}
