package com.CloudWhite.PersonalBlog.Entity.article;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

public class articleDraft {
    @Schema(name = "文章标题")
    private String title;
    @Schema(name = "文章内容")
    @Column(columnDefinition = "TEXT")
    private String articleContent;

    public articleDraft(String title, String articleContent) {
        this.title = title;
        this.articleContent = articleContent;
    }

    public articleDraft() {
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
}
