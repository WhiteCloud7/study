package com.CloudWhite.PersonalBlog.Entity.notice;

import com.CloudWhite.PersonalBlog.Entity.notice.notice;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name="noticeinfo")
public class noticeInfo {
    @Id
    @Schema(name="通知信息ID")
    private int noticeinfoId;
    @Schema(name="通知ID")
    @Column(name="notice_id",insertable = false,updatable = false)
    private int noticeId;
    @Schema(name="当前用户ID")
    private int userId;
    @Schema(name="当前用户是否点赞")
    private boolean isLike;

    public noticeInfo() {
    }

    public noticeInfo(int noticeinfoId, int noticeId, int userId, boolean isLike) {
        this.noticeinfoId = noticeinfoId;
        this.noticeId = noticeId;
        this.userId = userId;
        this.isLike = isLike;
    }

    public noticeInfo(int noticeinfoId, int noticeId, com.CloudWhite.PersonalBlog.Entity.notice.notice notice, boolean isLike) {
        this.noticeinfoId = noticeinfoId;
        this.noticeId = noticeId;
        this.isLike = isLike;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int useId) {
        this.userId = useId;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public int getNoticeinfoId() {
        return noticeinfoId;
    }

    public void setNoticeinfoId(int noticeinfoId) {
        this.noticeinfoId = noticeinfoId;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }
}
