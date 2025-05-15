package com.CloudWhite.PersonalBlog.Service;

import com.CloudWhite.PersonalBlog.Entity.DTO.userInfo;
import com.CloudWhite.PersonalBlog.Entity.user;
import com.CloudWhite.PersonalBlog.Model.ResponseEntity;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface userService {
    public void permissionCheck();
    public List<user> findAll();
    public userInfo getUserByUserId();
    public ResponseEntity login(String username, String password);
    public String register(String username,String password);
    public void saveProfile(user user);
    public String refreshToken(String refreshToken);
    public String updateAvatar(MultipartFile multipartFile);

    public void logout(String username);
}
