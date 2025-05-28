package com.CloudWhite.PersonalBlog.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Schema(name="收藏表")
@Table(name = "star")
public class star {
    @Id
    @Schema(name="收藏ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer starId;
    @Schema(name="用户ID")
    private Integer userId;
    @Schema(name="文章ID")
    private Integer articleId;

    public star() {
    }

    public star(Integer starId, Integer userId, Integer articleId) {
        this.starId = starId;
        this.userId = userId;
        this.articleId = articleId;
    }

    public star(Integer userId, Integer articleId) {
        this.userId = userId;
        this.articleId = articleId;
    }

    public Integer getStarId() {
        return starId;
    }

    public void setStarId(Integer starId) {
        this.starId = starId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
}
