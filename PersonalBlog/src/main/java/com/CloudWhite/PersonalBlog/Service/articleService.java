package com.CloudWhite.PersonalBlog.Service;

import com.CloudWhite.PersonalBlog.Entity.DTO.articleDto;
import com.CloudWhite.PersonalBlog.Entity.article.article;
import com.CloudWhite.PersonalBlog.Entity.article.articleInfo;

import java.util.List;

public interface articleService {
    public List<articleInfo> getArticleInfo();
    public List<article>  initArticle();
    public void updateArticleVisitCount(int articleId);
    public void updateArticleLikeCount(int articleId);
    public void updateArticleStarCount(int articleId);
    public articleInfo initCurrentArticleInfo(int articleId);
}
