package com.CloudWhite.PersonalBlog.Entity.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Schema(name="我的通知")
@Table(name = "notice")
public class notice {
    @Id
    @Schema(name="通知ID")
    private int noticeId;
    @Schema(name="通知内容")
    private String noticeMessage;
    @Schema(name="访问次数")
    private int visitCount;
    @Schema(name="点赞数")
    private int likeCount;
    @Schema(name="通知标题")
    private String title;
    public notice() {
    }

    public notice(int noticeId, String noticeMessage, String title) {
        this.noticeId = noticeId;
        this.noticeMessage = noticeMessage;
        this.title = title;
    }

    public notice(int noticeId, String noticeMessage, int visitCount, int likeCount, String title) {
        this.noticeId = noticeId;
        this.noticeMessage = noticeMessage;
        this.visitCount = visitCount;
        this.likeCount = likeCount;
        this.title = title;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeMessage() {
        return noticeMessage;
    }

    public void setNoticeMessage(String noticeMessage) {
        this.noticeMessage = noticeMessage;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
