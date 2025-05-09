package com.CloudWhite.PersonalBlog.Controller;

import com.CloudWhite.PersonalBlog.Entity.DTO.articleDto;
import com.CloudWhite.PersonalBlog.Entity.article.article;
import com.CloudWhite.PersonalBlog.Entity.article.articleInfo;
import com.CloudWhite.PersonalBlog.Model.ResponseEntity;
import com.CloudWhite.PersonalBlog.Service.articleService;
import com.CloudWhite.PersonalBlog.Utils.Annotation.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class articleController {
    private articleService articleService;
    @Autowired
    public articleController(com.CloudWhite.PersonalBlog.Service.articleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("getArticleInfo")
    @LoginRequired
    public ResponseEntity getArticleInfo(){
        return new ResponseEntity("200","请求成功",articleService.getArticleInfo());
    }

    @GetMapping("initArticle")
    public List<article>  initArticle(){
        return articleService.initArticle();
    }

    @GetMapping("updateArticleVisitCount")
    public void updateArticleVisitCount(int articleId){
        articleService.updateArticleVisitCount(articleId);
    }

    @GetMapping("updateArticleLikeCount")
    @LoginRequired
    public ResponseEntity updateArticleLikeCount(int articleId){
        articleService.updateArticleLikeCount(articleId);
        return new ResponseEntity();
    }

    @GetMapping("updateArticleStarCount")
    @LoginRequired
    public ResponseEntity updateArticleStarCount(int articleId){
        articleService.updateArticleStarCount(articleId);
        return new ResponseEntity();
    }

    @GetMapping("initCurrentArticleInfo")
    @LoginRequired
    public ResponseEntity initCurrentArticleInfo(int articleId){
        return new ResponseEntity(articleService.initCurrentArticleInfo(articleId));
    }
}
