package com.CloudWhite.PersonalBlog.Dao.article;

import com.CloudWhite.PersonalBlog.Entity.article.article;
import com.CloudWhite.PersonalBlog.Entity.article.articleInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface articleDao extends JpaRepository<article,Integer> {
    public article findByArticleId(int articleId);
}
