package com.CloudWhite.PersonalBlog.Dao.article;

import com.CloudWhite.PersonalBlog.Entity.article.articleInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface articleInfoDao extends JpaRepository<articleInfo,Integer> {

    public List<articleInfo> findByUserId(int userId);
    public articleInfo findByArticleIdAndUserId(int articleId,int userId);

    public boolean existsByArticleIdAndUserId(int articleId,int userId);
}
