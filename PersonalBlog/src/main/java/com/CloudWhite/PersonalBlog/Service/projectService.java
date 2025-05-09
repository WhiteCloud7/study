package com.CloudWhite.PersonalBlog.Service;

import com.CloudWhite.PersonalBlog.Entity.project;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface projectService {
    public List<project> getTheFirstDirectory();
    public List<project> findAllByCurrentDirId(int currentDirId);
    public List<project> getFilesByRouter(String filePath);
    public project addFile(String fileName,String modifyTime,String type,String filePath);
    public void rename(String newFileName,int fileId);
    public void deleteFile(int[] deleteFiles);
    public List<project> copyPaste(int []fileIds,String filePath);
    public List<project> cutPaste(int []fileIds,String filePath);
}
