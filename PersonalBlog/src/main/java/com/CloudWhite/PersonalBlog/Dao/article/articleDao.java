package com.CloudWhite.PersonalBlog.Dao;

import com.CloudWhite.PersonalBlog.Entity.article.article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface articleDao extends JpaRepository<article,Integer> {
    public article findByArticleId(int articleId);
}
