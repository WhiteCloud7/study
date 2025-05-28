package com.CloudWhite.PersonalBlog.Controller;

import com.CloudWhite.PersonalBlog.Dao.projectDao;
import com.CloudWhite.PersonalBlog.Entity.project;
import com.CloudWhite.PersonalBlog.Model.ResponseEntity;
import com.CloudWhite.PersonalBlog.Service.projectService;
import com.CloudWhite.PersonalBlog.Utils.Annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@Tag(name = "项目控制器")
public class projectController {
    private projectService projectService;
    @Autowired
    projectDao projectDao;
    @Autowired
    public projectController(com.CloudWhite.PersonalBlog.Service.projectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/getTheFirstDirectory")
    @RateLimitForUnlogin(maxRequests = 10,seconds = 10)
    @RateLimitForAll(maxRequests = 1000)
    @Operation(description = "得到一级目录")
    public List<project> getTheFirstDirectory(){
        return projectService.getTheFirstDirectory();
    }

    @GetMapping("/project")
    @RateLimitForUnlogin(maxRequests = 10,seconds = 10)
    @RateLimitForAll(maxRequests = 1000)
    @Operation(description = "回到一级目录")
    public List<project> reset(){
        return projectService.getTheFirstDirectory();
    }

    @GetMapping("/getNextDirs")
    @RateLimitForAll(maxRequests = 2000)
    @Operation(description = "前往下一目录")
    public List<project> getNextDirs(int fileId){
        return projectService.findAllByCurrentDirId(fileId);
    }

    @GetMapping("/project/**")
    @Operation(description = "通过路由跳转")
    public List<project> getFilesByRouter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String encodedPath = uri.replaceFirst("/project/?", "");
        //解决中文乱码
        String filePath = URLDecoder.decode(encodedPath, StandardCharsets.UTF_8);
        return projectService.getFilesByRouter(filePath);
    }

    @PostMapping("/addFile")
    @PermissionRequired(type = "admin")
    @Operation(description = "添加文件")
    public project addFile(@RequestParam String fileName,@RequestParam  String modifyTime,@RequestParam  String type,@RequestParam(required = false)  String filePath) throws IOException {
        filePath = filePath == null || filePath.isEmpty()? "" : filePath;
        return projectService.addFile(fileName,modifyTime,type,filePath);
    }

    @GetMapping("deleteFile")
    @PermissionRequired(type = "admin")
    @Operation(description = "删除文件")
    public void deleteFile(int[] deleteFiles,String filePath){
        filePath = filePath == null || filePath.isEmpty()? "" : filePath;
        projectService.deleteFile(deleteFiles,filePath);
    }

    @GetMapping("rename")
    @PermissionRequired(type = "admin")
    @Operation(description = "重命名文件")
    public void rename(@RequestParam  String newFileName,@RequestParam int fileId,@RequestParam String fPath){
        newFileName = URLDecoder.decode(newFileName, StandardCharsets.UTF_8);
        fPath = URLDecoder.decode(fPath, StandardCharsets.UTF_8);
        projectService.rename(newFileName,fileId,fPath);
    }

    @PostMapping("copyPaste")
    @PermissionRequired(type = "admin")
    @Operation(description = "复制粘贴")
    public List<project> copyPaste(@RequestParam int []fileIds,@RequestParam(required = false) String filePath,@RequestParam(required = false) String shearPath) throws IOException {
        filePath = filePath == null || filePath.isEmpty()? "" : filePath;
        return projectService.copyPaste(fileIds,filePath,shearPath);
    }

    @PostMapping("cutPaste")
    @PermissionRequired(type = "admin")
    @Operation(description = "剪切粘贴")
    public List<project> cutPaste(@RequestParam int []fileIds,@RequestParam(required = false) String filePath,@RequestParam(required = false) String shearPath) throws IOException {
        filePath = filePath == null || filePath.isEmpty()? "" : filePath;
        return projectService.cutPaste(fileIds,filePath,shearPath);
    }

    @PostMapping("uploadFile")
    @PermissionRequired(type = "admin")
    @Operation(description = "上传文件")
    public ResponseEntity updateFile(MultipartFile file,@RequestParam("filePath") String filePath){
        filePath = filePath == null || filePath.isEmpty()? "" : filePath;
        return new ResponseEntity(projectService.uploadFile(file,filePath));
    }

    @GetMapping("download")
    @Operation(description = "下载文件")
    @RateLimitForAll(maxRequests = 1000)
    @LoginRequired
    public org.springframework.http.ResponseEntity<Resource> download(String filePath, int fileId) throws IOException {
        filePath = filePath == null || filePath.isEmpty()? "" : filePath;
        filePath = URLDecoder.decode(filePath,StandardCharsets.UTF_8);
        System.out.println(filePath+" "+fileId);
        return projectService.download(filePath,fileId);
    }
}
