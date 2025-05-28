package com.CloudWhite.PersonalBlog.Dao;

import com.CloudWhite.PersonalBlog.Entity.star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface starDao extends JpaRepository<star,Integer> {
    @Query(value = "SELECT article_id FROM star WHERE user_id = :userId",nativeQuery = true)
    public List<Integer> getArticleIdByUserId(int userId);
    public star findByUserIdAndArticleId(int userId,int articleId);
}
