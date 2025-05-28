package com.CloudWhite.PersonalBlog.Dao.article;

import com.CloudWhite.PersonalBlog.Entity.article.article;
import com.CloudWhite.PersonalBlog.Entity.article.articleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface articleDao extends JpaRepository<article,Integer> {
    public article findByArticleId(int articleId);
    @Query(value = "SELECT title FROM article WHERE article_id = :articleId",nativeQuery = true)
    public String getArticleTitleByArticleId(int articleId);
    @Query(value = "SELECT user_id FROM article WHERE article_id = :articleId",nativeQuery = true)
    public int getUserIdByArticleId(int articleId);
}
