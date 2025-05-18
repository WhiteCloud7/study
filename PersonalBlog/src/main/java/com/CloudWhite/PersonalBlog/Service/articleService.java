package com.CloudWhite.PersonalBlog.Service;

import com.CloudWhite.PersonalBlog.Entity.DTO.articleDto;
import com.CloudWhite.PersonalBlog.Entity.DTO.articleInfoDto;
import com.CloudWhite.PersonalBlog.Entity.article.article;
import com.CloudWhite.PersonalBlog.Entity.article.articleDraft;
import com.CloudWhite.PersonalBlog.Entity.article.articleInfo;
import com.CloudWhite.PersonalBlog.Model.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface articleService {
    public List<articleInfoDto> getArticleInfo();
    public List<articleDto>  initArticle();
    public void updateArticleVisitCount(int articleId);
    public void updateArticleLikeCount(int articleId);
    public void updateArticleStarCount(int articleId);
    public articleInfoDto initCurrentArticleInfo(int articleId);
    public articleDto initCurrentArticle(int articleId);
    public void saveArticle(int articleId, String title, String content);
    public void saveArticleToDraft(String title, String content);
    public articleDraft getDraft();
    public void newArticle(String title, String content);
    public void deleteArticle(int articleId);
}
