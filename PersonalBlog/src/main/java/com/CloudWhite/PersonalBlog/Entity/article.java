package com.CloudWhite.PersonalBlog.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Schema(name = "文章")
@Table(name = "article")
public class article {
    @Id
    @Schema(name = "文章ID")
    private int articleId;
    @Schema(name = "文章内容")
    private String articleContent;
    @Schema(name = "文章访问数")
    private int visitCount;
    @Schema(name = "文章点赞数")
    private int likeCount;
    @Schema(name = "文章收藏数")
    private int starCount;
    @Schema(name = "文章评论数")
    private int commentCount;
    @Schema(name = "作者用户ID")
    @Column(name = "user_id")
    private int userId;

    @Schema(name = "作者用户的对象")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private user user;

    public article() {
    }

    public article(int articleId, String articleContent, int visitCount, int likeCount, int starCount, int commentCount, int userId) {
        this.articleId = articleId;
        this.articleContent = articleContent;
        this.visitCount = visitCount;
        this.likeCount = likeCount;
        this.starCount = starCount;
        this.commentCount = commentCount;
        this.userId = userId;
    }

    public article(int articleId, String articleContent, int visitCount, int likeCount, int starCount, int commentCount, com.CloudWhite.PersonalBlog.Entity.user user) {
        this.articleId = articleId;
        this.articleContent = articleContent;
        this.visitCount = visitCount;
        this.likeCount = likeCount;
        this.starCount = starCount;
        this.commentCount = commentCount;
        this.user = user;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
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

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public user getUser() {
        return user;
    }

    public void setUser(user user) {
        this.user = user;
    }
}