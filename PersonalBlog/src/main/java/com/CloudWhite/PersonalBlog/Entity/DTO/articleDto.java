package com.CloudWhite.PersonalBlog.Entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class articleDto {
    @Id
    @Schema(name = "文章ID")
    private int articleId;
    @Schema(name = "文章标题")
    private String title;
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
    private String nikeName;
    private String avatar_src;
    public articleDto() {
    }

    public articleDto(int articleId, String title, String articleContent, int visitCount, int likeCount, int starCount, int commentCount, int userId) {
        this.articleId = articleId;
        this.title = title;
        this.articleContent = articleContent;
        this.visitCount = visitCount;
        this.likeCount = likeCount;
        this.starCount = starCount;
        this.commentCount = commentCount;
        this.userId = userId;
    }

    public articleDto(int articleId, String title, String articleContent, int visitCount, int likeCount, int starCount, int commentCount, int userId, String nikeName, String avatar_src) {
        this.articleId = articleId;
        this.title = title;
        this.articleContent = articleContent;
        this.visitCount = visitCount;
        this.likeCount = likeCount;
        this.starCount = starCount;
        this.commentCount = commentCount;
        this.userId = userId;
        this.nikeName = nikeName;
        this.avatar_src = avatar_src;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getAvatar_src() {
        return avatar_src;
    }

    public void setAvatar_src(String avatar_src) {
        this.avatar_src = avatar_src;
    }
}
