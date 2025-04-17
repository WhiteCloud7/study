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
    @Schema(name="通知ID的对象")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="notice_id")
    @JsonIgnore
    private notice notice;
    @Schema(name="当前用户ID")
    private int userId;
    @Schema(name="当前用户是否点赞")
    private boolean isLike;
    @Schema(name="当前用户是否收藏")
    private boolean isStar;

    public noticeInfo() {
    }

    public noticeInfo(int noticeinfoId, int noticeId, int userId, boolean isLike, boolean isStar) {
        this.noticeinfoId = noticeinfoId;
        this.noticeId = noticeId;
        this.userId = userId;
        this.isLike = isLike;
        this.isStar = isStar;
    }

    public noticeInfo(int noticeinfoId, int noticeId, com.CloudWhite.PersonalBlog.Entity.notice.notice notice, boolean isLike, boolean isStar) {
        this.noticeinfoId = noticeinfoId;
        this.noticeId = noticeId;
        this.notice = notice;
        this.isLike = isLike;
        this.isStar = isStar;
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

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }

    public int getNoticeinfoId() {
        return noticeinfoId;
    }

    public void setNoticeinfoId(int noticeinfoId) {
        this.noticeinfoId = noticeinfoId;
    }

    public com.CloudWhite.PersonalBlog.Entity.notice.notice getNotice() {
        return notice;
    }

    public void setNotice(com.CloudWhite.PersonalBlog.Entity.notice.notice notice) {
        this.notice = notice;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }
}
