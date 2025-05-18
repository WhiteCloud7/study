package com.CloudWhite.PersonalBlog.Entity.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name="articleinfo")
public class articleInfo {
    @Id
    @Schema(name="文章信息ID")
    private int articleinfoId;
    @Schema(name="通知ID")
    @Column(name="article_id",insertable = false,updatable = false)
    private int articleId;
    @Schema(name="文章ID的对象")
    @ManyToOne
    @JoinColumn(name="article_id")
    private article article;
    @Schema(name="当前用户ID")
    private int userId;
    @Schema(name="当前用户是否点赞")
    private boolean isLike;
    @Schema(name="当前用户是否收藏")
    private boolean isStar;

    public articleInfo() {
    }

    public articleInfo(int articleinfoId, int articleId, int userId, boolean isLike, boolean isStar) {
        this.articleinfoId = articleinfoId;
        this.articleId = articleId;
        this.userId = userId;
        this.isLike = isLike;
        this.isStar = isStar;
    }



    public articleInfo(int articleinfoId, com.CloudWhite.PersonalBlog.Entity.article.article article, int userId, boolean isLike, boolean isStar) {
        this.articleinfoId = articleinfoId;
        this.article = article;
        this.userId = userId;
        this.isLike = isLike;
        this.isStar = isStar;
    }

    public int getArticleinfoId() {
        return articleinfoId;
    }

    public void setArticleinfoId(int articleinfoId) {
        this.articleinfoId = articleinfoId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public com.CloudWhite.PersonalBlog.Entity.article.article getArticle() {
        return article;
    }

    public void setArticle(com.CloudWhite.PersonalBlog.Entity.article.article article) {
        this.article = article;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
}
