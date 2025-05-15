package com.CloudWhite.PersonalBlog.Service;

import com.CloudWhite.PersonalBlog.Entity.project;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface projectService {
    public List<project> getTheFirstDirectory();
    public List<project> findAllByCurrentDirId(int currentDirId);
    public List<project> getFilesByRouter(String filePath);
    public project addFile(String fileName,String modifyTime,String type,String filePath) throws IOException;
    public void rename(String newFileName,int fileId,String fPath);
    public void deleteFile(int[] deleteFiles,String filePath);
    public List<project> copyPaste(int []fileIds,String filePath,String shearPath) throws IOException;
    public List<project> cutPaste(int []fileIds,String filePath,String shearPth) throws IOException;

    public String uploadFile(MultipartFile file,String filePath);
}
