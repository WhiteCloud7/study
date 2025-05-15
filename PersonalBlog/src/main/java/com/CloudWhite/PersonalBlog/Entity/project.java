package com.CloudWhite.PersonalBlog.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "project")
public class project {
    @Id
    @Schema(name="文件ID")
    private int fileId;
    @Schema(name="文件名")
    private String fileName;
    @Schema(name="修改时间")
    private String modifyTime;
    @Schema(name="文件类型")
    private String type;
    @Schema(name="目录等级")
    @Column(name="directory_level")
    private byte dirLevel;
    @Schema(name="上一级目录ID")
    @Column(name="parent_directory_id")
    private int parentDirId;

    public project() {
    }

    public project(int fileId, String fileName, String modifyTime, String type, byte dirLevel, int parentDirId) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.modifyTime = modifyTime;
        this.type = type;
        this.dirLevel = dirLevel;
        this.parentDirId = parentDirId;
    }

    public project(String fileName, String modifyTime, String type, byte dirLevel, int parentDirId) {
        this.fileName = fileName;
        this.modifyTime = modifyTime;
        this.type = type;
        this.dirLevel = dirLevel;
        this.parentDirId = parentDirId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte getDirLevel() {
        return dirLevel;
    }

    public void setDirLevel(byte dirLevel) {
        this.dirLevel = dirLevel;
    }

    public int getParentDirId() {
        return parentDirId;
    }

    public void setParentDirId(int parentDirId) {
        this.parentDirId = parentDirId;
    }
}
