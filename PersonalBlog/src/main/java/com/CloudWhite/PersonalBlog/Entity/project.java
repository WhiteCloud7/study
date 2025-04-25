package com.CloudWhite.PersonalBlog.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class directory {
    @Id
    @Schema("目录ID")
    private int dirId;
    @Schema("目录名")
    private String dirName;
    @Schema("修改时间")
    private String modifyTime;
    @Schema("目录类型")
    private String type;
    @Schema("目录等级")
    private String dirLevel;
}
