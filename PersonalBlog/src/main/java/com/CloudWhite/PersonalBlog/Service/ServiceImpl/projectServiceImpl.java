package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import com.CloudWhite.PersonalBlog.Dao.projectDao;
import com.CloudWhite.PersonalBlog.Entity.project;
import com.CloudWhite.PersonalBlog.Service.projectService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class projectServiceImpl implements projectService {
    private projectDao projectDao;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    public projectServiceImpl(projectDao projectDao) {
        this.projectDao = projectDao;
    }

    public List<project> getTheFirstDirectory(){
        return projectDao.getTheFirstDirectory();
    };

    public List<project> findAllByCurrentDirId(int currentDirId){
        return projectDao.findAllByParentDirId(currentDirId);
    }

    public int getCurrentFileId(String[] filePathArray,int loopTimes){
        int currentFileId = 0;
        if (loopTimes > 0) {
            currentFileId = projectDao.getFileIdByFileName(filePathArray[0]);
        }
        for (int i = 1; i < loopTimes; i++) {
            currentFileId = projectDao.getNextFileIdByParentIdAndFileName(currentFileId, filePathArray[i]);
        }
        return currentFileId;
    }

    public List<project> getFilesByRouter(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }
        String[] filePathArray = filePath.split("/");
        int currentFileId = getCurrentFileId(filePathArray,filePathArray.length);
        return findAllByCurrentDirId(currentFileId);
    }

    public project addFile(String fileName,String modifyTime,String type,String filePath){
        filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
        String[] filePathArray = filePath.split(",");
        byte dirLevel = (byte)(filePathArray.length+1);

        project project = new project();
        project.setFileName(fileName);
        project.setModifyTime(modifyTime);
        project.setType(type);
        project.setDirLevel(dirLevel);
        int parentDirId = filePathArray[0].isEmpty()?0:
                getCurrentFileId(filePathArray,filePathArray.length);
        project.setParentDirId(parentDirId);
        projectDao.save(project);
        project newProject = projectDao.getNextDirsByParentIdAndFileName(parentDirId,fileName);
        return newProject;
    }

    public void deleteChildrenFile(int deleteFile){
        List<project> children = projectDao.findAllByParentDirId(deleteFile);
        if(children!=null){
            for(project child : children){
                projectDao.deleteById(child.getFileId());
                deleteChildrenFile(child.getFileId());
            }
        }
    }

    public void rename(String newFileName,int fileId){
        project project = projectDao.findByFileId(fileId);
        project.setFileName(newFileName);
        projectDao.save(project);
    }

    public void deleteFile(int[] deleteFiles){
        List<Integer> fileIdList = Arrays.stream(deleteFiles).boxed().toList();
        projectDao.deleteAllById(fileIdList);
        for(int deleteFile : deleteFiles)
            deleteChildrenFile(deleteFile);
    }

    public project saveNewProject(int fileId,byte dirLevel,int parentDirId){
        project project = projectDao.findByFileId(fileId);
        project newProject = new project();
        newProject.setDirLevel(dirLevel);
        newProject.setFileName(project.getFileName());
        newProject.setType(project.getType());
        LocalDateTime dateTime = LocalDateTime.now();
        newProject.setModifyTime(dateTime.toString());
        newProject.setParentDirId(parentDirId);
        entityManager.clear();
        projectDao.save(newProject);
        return projectDao.getNextDirsByParentIdAndFileName(parentDirId,project.getFileName());
    }

    public List<project> copyPaste(int []fileIds,String filePath) {
        List<project> result = new ArrayList<>();
        filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
        String[] filePathArray = filePath.split(",");
        byte dirLevel = filePathArray[0].equals("")?1:(byte)(filePathArray.length+1);
        int parentDirId = filePathArray[0].equals("")?0:
                getCurrentFileId(filePathArray,filePathArray.length);

        for (int fileId : fileIds) {
            project newProject = saveNewProject(fileId, dirLevel, parentDirId);
            result.add(newProject);
            result.addAll(copyChildrenDir(fileId, (byte)(dirLevel + 1), newProject.getFileId()));
        }
        return result;
    }

    public List<project> copyChildrenDir(int fileId,byte dirLevel,int parentId) {
        List<project> result = new ArrayList<>();
        List<project> children = projectDao.findAllByParentDirId(fileId);

        if (children != null) {
            for (project child : children) {
                project newProject = saveNewProject(child.getFileId(), dirLevel, parentId);
                result.add(newProject);
                // 递归添加子孙目录
                result.addAll(copyChildrenDir(child.getFileId(), (byte)(dirLevel + 1), newProject.getFileId()));
            }
        }
        return result;
    }

    public List<project> cutPaste(int []fileIds,String filePath){
        List<project> projectList = copyPaste(fileIds,filePath);
        deleteFile(fileIds);
        return projectList;
    }
}
