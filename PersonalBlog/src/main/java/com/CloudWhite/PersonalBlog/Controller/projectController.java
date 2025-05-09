package com.CloudWhite.PersonalBlog.Controller;

import com.CloudWhite.PersonalBlog.Dao.projectDao;
import com.CloudWhite.PersonalBlog.Entity.project;
import com.CloudWhite.PersonalBlog.Service.projectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Operation(description = "得到一级目录")
    public List<project> getTheFirstDirectory(){
        return projectService.getTheFirstDirectory();
    }

    @GetMapping("/project")
    public List<project> reset(){
        return projectService.getTheFirstDirectory();
    }

    @GetMapping("/getNextDirs")
    public List<project> getNextDirs(int fileId){
        return projectService.findAllByCurrentDirId(fileId);
    }

    @GetMapping("/project/**")
    public List<project> getFilesByRouter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String encodedPath = uri.replaceFirst("/project/?", "");
        //解决中文乱码
        String filePath = URLDecoder.decode(encodedPath, StandardCharsets.UTF_8);
        return projectService.getFilesByRouter(filePath);
    }

    @PostMapping("/addFile")
    public project addFile(@RequestParam String fileName,@RequestParam  String modifyTime,@RequestParam  String type,@RequestParam(required = false)  String filePath){
        filePath = filePath == null || filePath.isEmpty()? "" : filePath;
        return projectService.addFile(fileName,modifyTime,type,filePath);
    }

    @GetMapping("deleteFile")
    public void deleteFile(int[] deleteFiles){
        projectService.deleteFile(deleteFiles);
    }

    @GetMapping("rename")
    public void rename(@RequestParam  String newFileName,@RequestParam int fileId){
        projectService.rename(newFileName,fileId);
    }

    @PostMapping("copyPaste")
    public List<project> copyPaste(@RequestParam int []fileIds,@RequestParam(required = false) String filePath){
        filePath = filePath == null || filePath.isEmpty()? "" : filePath;
        return projectService.copyPaste(fileIds,filePath);
    }

    @PostMapping("cutPaste")
    public List<project> cutPaste(@RequestParam int []fileIds,@RequestParam(required = false) String filePath){
        filePath = filePath == null || filePath.isEmpty()? "" : filePath;
        return projectService.cutPaste(fileIds,filePath);
    }
}
