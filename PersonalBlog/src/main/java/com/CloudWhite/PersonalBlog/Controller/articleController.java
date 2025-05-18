package com.CloudWhite.PersonalBlog.Controller;

import com.CloudWhite.PersonalBlog.Entity.DTO.articleDto;
import com.CloudWhite.PersonalBlog.Entity.article.article;
import com.CloudWhite.PersonalBlog.Entity.article.articleDraft;
import com.CloudWhite.PersonalBlog.Entity.article.articleInfo;
import com.CloudWhite.PersonalBlog.Model.ResponseEntity;
import com.CloudWhite.PersonalBlog.Service.articleService;
import com.CloudWhite.PersonalBlog.Utils.Annotation.LoginRequired;
import com.CloudWhite.PersonalBlog.Utils.Annotation.PermissionRequired;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class articleController {
    private articleService articleService;
    @Autowired
    public articleController(com.CloudWhite.PersonalBlog.Service.articleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/getArticleInfo")
    @LoginRequired
    public ResponseEntity getArticleInfo(){
        return new ResponseEntity("200","请求成功",articleService.getArticleInfo());
    }

    @GetMapping("/initArticle")
    public List<articleDto>  initArticle(){
        return articleService.initArticle();
    }

    @GetMapping("/updateArticleVisitCount")
    public void updateArticleVisitCount(int articleId){
        articleService.updateArticleVisitCount(articleId);
    }

    @GetMapping("/updateArticleLikeCount")
    @LoginRequired
    public ResponseEntity updateArticleLikeCount(int articleId){
        articleService.updateArticleLikeCount(articleId);
        return new ResponseEntity();
    }

    @GetMapping("/updateArticleStarCount")
    @LoginRequired
    public ResponseEntity updateArticleStarCount(int articleId){
        articleService.updateArticleStarCount(articleId);
        return new ResponseEntity();
    }

    @GetMapping("/initCurrentArticleInfo")
    @LoginRequired
    public ResponseEntity initCurrentArticleInfo(int articleId){
        return new ResponseEntity(articleService.initCurrentArticleInfo(articleId));
    }
    @GetMapping("/initCurrentArticle")
    @LoginRequired
    public ResponseEntity initCurrentArticle(int articleId){
        return new ResponseEntity(articleService.initCurrentArticle(articleId));
    }

    @GetMapping("/checkPremession")
    @PermissionRequired(type = "admin")
    public ResponseEntity checkPremession(){
        return new ResponseEntity();
    }

    @PostMapping("/saveArticle")
    @PermissionRequired(type = "admin")
    public ResponseEntity saveArticle(@RequestParam int articleId,@RequestParam String title,@RequestParam String content){
        articleService.saveArticle(articleId,title,content);
        return new ResponseEntity();
    }
    @PostMapping("/saveArticleToDraft")
    @PermissionRequired(type = "admin")
    public ResponseEntity saveArticleToDraft(String title, String content){
        articleService.saveArticleToDraft(title,content);
        return new ResponseEntity();
    }
    @GetMapping("/articleDraft")
    @PermissionRequired(type = "admin")
    public ResponseEntity getDraft(){
        return new ResponseEntity(articleService.getDraft());
    }
    @PostMapping ("/newArticle")
    @PermissionRequired(type = "admin")
    public ResponseEntity newArticle(String title, String content){
        articleService.newArticle(title,content);
        return new ResponseEntity();
    }
    @GetMapping("/deleteArticle")
    @PermissionRequired(type = "admin")
    public ResponseEntity deleteArticle(int articleId){
        articleService.deleteArticle(articleId);
        return new ResponseEntity();
    }
}
