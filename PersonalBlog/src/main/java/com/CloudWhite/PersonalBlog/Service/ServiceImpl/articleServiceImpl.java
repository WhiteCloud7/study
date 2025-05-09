package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import com.CloudWhite.PersonalBlog.Dao.article.articleDao;
import com.CloudWhite.PersonalBlog.Dao.article.articleInfoDao;
import com.CloudWhite.PersonalBlog.Dao.mybatisDao;
import com.CloudWhite.PersonalBlog.Entity.DTO.articleDto;
import com.CloudWhite.PersonalBlog.Entity.article.article;
import com.CloudWhite.PersonalBlog.Entity.article.articleInfo;
import com.CloudWhite.PersonalBlog.Entity.role;
import com.CloudWhite.PersonalBlog.Entity.user;
import com.CloudWhite.PersonalBlog.Model.UserContext;
import com.CloudWhite.PersonalBlog.Service.articleService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class articleServiceImpl implements articleService {
    private articleDao articleDao;
    private articleInfoDao articleInfoDao;
    private mybatisDao mybatisDao;
    @Autowired
    public articleServiceImpl(articleDao articleDao, articleInfoDao articleInfoDao,mybatisDao mybatisDao) {
        this.articleDao = articleDao;
        this.articleInfoDao = articleInfoDao;
        this.mybatisDao = mybatisDao;
    }

    public List<articleInfo> getArticleInfo(){
        int userId = UserContext.getCurrentToken().getUserId();
        List<articleInfo> articleInfos = articleInfoDao.findByUserId(userId);
        for(articleInfo articleInfo : articleInfos){
            article article = articleInfo.getArticle();
            user user = article.getUser();
            role role = user.getRole();
            Hibernate.initialize(article);
            Hibernate.initialize(user);
            Hibernate.initialize(role);
        }
        return articleInfos;
    }

    public List<article> initArticle() {
        List<article> articles = articleDao.findAll();
        for (article a : articles) {
            user u = a.getUser();
            Hibernate.initialize(u);
            Hibernate.initialize(u.getRole());
        }
        return articles;
    }

    public void updateArticleVisitCount(int articleId){
        article article = articleDao.findByArticleId(articleId);
        int currentVisitCount = article.getVisitCount();
        article.setVisitCount(currentVisitCount+1);
        articleDao.save(article);
    }

    public void updateLikeCount(int articleId){
        int userId = UserContext.getCurrentToken().getUserId();
        articleInfo articleInfo = articleInfoDao.findByArticleIdAndUserId(articleId,userId);
        boolean isLike = articleInfo.isLike();
        article article = articleDao.findByArticleId(articleId);
        int currentLikeCount = article.getLikeCount();
        if(!isLike){
            article.setLikeCount(currentLikeCount+1);
            articleInfo.setLike(true);
        }else{
            article.setLikeCount(currentLikeCount-1);
            articleInfo.setLike(false);
        }
        articleInfoDao.save(articleInfo);
        articleDao.save(article);
    }

    public void updateArticleLikeCount(int articleId){
        int userId = UserContext.getCurrentToken().getUserId();
        articleInfo articleInfo1 = articleInfoDao.findByArticleIdAndUserId(articleId,userId);
        if(articleInfo1==null)
            mybatisDao.setArticleInfo(userId,articleId);
        updateLikeCount(articleId);
    }

    public void updateStarCount(int articleId){
        int userId = UserContext.getCurrentToken().getUserId();
        articleInfo articleInfo = articleInfoDao.findByArticleIdAndUserId(articleId,userId);
        boolean isStar = articleInfo.isStar();
        article article = articleDao.findByArticleId(articleId);
        int currentStarCount = article.getStarCount();
        if(!isStar){
            article.setStarCount(currentStarCount+1);
            articleInfo.setStar(true);
        }else{
            article.setStarCount(currentStarCount-1);
            articleInfo.setStar(false);
        }
        articleInfo.setArticleId(articleId);
        articleInfoDao.save(articleInfo);
        articleDao.save(article);
    }

    public void updateArticleStarCount(int articleId){
        int userId = UserContext.getCurrentToken().getUserId();
        articleInfo articleInfo1 = articleInfoDao.findByArticleIdAndUserId(articleId,userId);
        if(articleInfo1==null)
            mybatisDao.setArticleInfo(userId,articleId);
        updateStarCount(articleId);
    }

    public articleInfo initCurrentArticleInfo(int articleId){
        int userId = UserContext.getCurrentToken().getUserId();
        return articleInfoDao.findByArticleIdAndUserId(articleId,userId);
    }
}
