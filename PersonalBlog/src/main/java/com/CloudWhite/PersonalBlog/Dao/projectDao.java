package com.CloudWhite.PersonalBlog.Dao;

import com.CloudWhite.PersonalBlog.Entity.project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface projectDao extends JpaRepository<project,Integer> {
    @Query("select p from project p where p.dirLevel = 1")
    public List<project> getTheFirstDirectory();

    public List<project> findAllByParentDirId(int parentDirId);

    @Query("select p.fileId from project p where p.dirLevel = :dirLevel and p.fileName = :fileName")
    public int getFileIdByLevelAndName(byte dirLevel,String fileName);
    public project findByFileId(int fileId);

    public List<project> findByFileIdIn(int[] fileIds);

    @Query("select p.fileId from project p where p.fileName = :fileName")
    public int getFileIdByFileName(String fileName);

    @Query("select p.fileId from project p where p.parentDirId = :parentId and p.fileName = :fileName")
    public int getNextFileIdByParentIdAndFileName(int parentId,String fileName);

    @Query ("select p from project p where p.parentDirId = :parentId and p.fileName = :fileName")
    public project getNextDirsByParentIdAndFileName(int parentId,String fileName);
}
