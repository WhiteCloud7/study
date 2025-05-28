package com.CloudWhite.PersonalBlog.Entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class starDto {
    @Schema(name="收藏ID")
    private Integer starId;
    @Schema(name="用户ID")
    private Integer userId;
    @Schema(name="文章ID")
    private Integer articleId;
    @Schema(name="用户昵称")
    private String nikeName;
    @Schema(name="用户头像路径")
    private String avatar_src;
    @Schema(name = "文章标题")
    private String title;

    public starDto(Integer userId, Integer articleId, String nikeName, String avatar_src, String title) {
        this.userId = userId;
        this.articleId = articleId;
        this.nikeName = nikeName;
        this.avatar_src = avatar_src;
        this.title = title;
    }

    public starDto(Integer starId, Integer userId, Integer articleId, String nikeName, String avatar_src, String title) {
        this.starId = starId;
        this.userId = userId;
        this.articleId = articleId;
        this.nikeName = nikeName;
        this.avatar_src = avatar_src;
        this.title = title;
    }

    public starDto() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
