package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import com.CloudWhite.PersonalBlog.Dao.article.articleDao;
import com.CloudWhite.PersonalBlog.Dao.article.articleInfoDao;
import com.CloudWhite.PersonalBlog.Dao.mybatisDao;
import com.CloudWhite.PersonalBlog.Entity.DTO.articleDto;
import com.CloudWhite.PersonalBlog.Entity.DTO.articleInfoDto;
import com.CloudWhite.PersonalBlog.Entity.article.article;
import com.CloudWhite.PersonalBlog.Entity.article.articleDraft;
import com.CloudWhite.PersonalBlog.Entity.article.articleInfo;
import com.CloudWhite.PersonalBlog.Entity.user;
import com.CloudWhite.PersonalBlog.Model.Redis.redisHashTemplateConfig;
import com.CloudWhite.PersonalBlog.Model.ResponseEntity;
import com.CloudWhite.PersonalBlog.Model.UserContext;
import com.CloudWhite.PersonalBlog.Service.articleService;
import com.CloudWhite.PersonalBlog.Utils.Annotation.NoAop;
import com.CloudWhite.PersonalBlog.Utils.redisUtils;
import io.lettuce.core.RedisClient;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class articleServiceImpl implements articleService {
    private RedissonClient redissonClient;
    private articleDao articleDao;
    private articleInfoDao articleInfoDao;
    private mybatisDao mybatisDao;
    private redisHashTemplateConfig redisHashTemplateConfig;
    private redisUtils redisUtils;
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    public articleServiceImpl(RedissonClient redissonClient, com.CloudWhite.PersonalBlog.Dao.article.articleDao articleDao, com.CloudWhite.PersonalBlog.Dao.article.articleInfoDao articleInfoDao, com.CloudWhite.PersonalBlog.Dao.mybatisDao mybatisDao, com.CloudWhite.PersonalBlog.Model.Redis.redisHashTemplateConfig redisHashTemplateConfig, com.CloudWhite.PersonalBlog.Utils.redisUtils redisUtils, StringRedisTemplate stringRedisTemplate) {
        this.redissonClient = redissonClient;
        this.articleDao = articleDao;
        this.articleInfoDao = articleInfoDao;
        this.mybatisDao = mybatisDao;
        this.redisHashTemplateConfig = redisHashTemplateConfig;
        this.redisUtils = redisUtils;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    protected void initArticles() {
        List<article> articles = articleDao.findAll();
        for (article article : articles) {
            articleDto dto = new articleDto();
            Hibernate.initialize(article.getUser());
            BeanUtils.copyProperties(article, dto);
            dto.setNikeName(article.getUser().getNikeName());
            dto.setAvatar_src(article.getUser().getAvatar_src());
            redisHashTemplateConfig.setHashObject("article" ,"article:"+article.getArticleId(), dto);
        }
    }

    @PostConstruct
    private void initArticleInfo(){
        List<articleInfo> articleInfos = articleInfoDao.findAll();
        for(articleInfo articleInfo : articleInfos){
            articleInfoDto dto = new articleInfoDto();
            BeanUtils.copyProperties(articleInfo, dto);
            redisHashTemplateConfig.setHashObject("articleInfo", "articleInfo:" +articleInfo.getUserId()+":"+articleInfo.getArticleId(),dto);
        }
    }

    public List<articleInfoDto> getArticleInfo() {
        int userId = UserContext.getCurrentToken().getUserId();
        Set<Object> keys = stringRedisTemplate.opsForHash().keys("articleInfo");
        List<articleInfoDto> result = new ArrayList<>();
        for (Object key : keys) {
            Pattern pattern = Pattern.compile("articleInfo:"+userId+":.+");
            Matcher matcher = pattern.matcher((String)key);
            articleInfoDto dto = null;
            if(matcher.matches()){
                dto = redisHashTemplateConfig.getHashObject("articleInfo", key, articleInfoDto.class);
            }
            if (dto != null) {
                result.add(dto);
            }
        }
        if (result.isEmpty()) {
            List<articleInfo> articleInfos = articleInfoDao.findByUserId(userId);
            for (articleInfo a : articleInfos) {
                articleInfoDto dto = new articleInfoDto();
                BeanUtils.copyProperties(a, dto);
                String cacheKey = "articleInfo:" + userId + ":" + a.getArticleId();
                redisHashTemplateConfig.setHashObject("articleInfo", cacheKey, dto); // 设置缓存
                result.add(dto);
            }
        }
        return result;
    }

    public List<articleInfoDto> getAllArticleInfo() {
        Set<Object> keys = stringRedisTemplate.opsForHash().keys("articleInfo");
        List<articleInfoDto> result = new ArrayList<>();
        for (Object key : keys) {
            articleInfoDto dto = redisHashTemplateConfig.getHashObject("articleInfo", key, articleInfoDto.class);
            if (dto != null) {
                result.add(dto);
            }
        }
        if (result.isEmpty()) {
            List<articleInfo> articleInfos = articleInfoDao.findAll();
            for (articleInfo a : articleInfos) {
                articleInfoDto dto = new articleInfoDto();
                BeanUtils.copyProperties(a, dto);
                String cacheKey = "articleInfo:" + a.getUserId() + ":" + a.getArticleId();
                redisHashTemplateConfig.setHashObject("articleInfo", cacheKey, dto); // 设置缓存
                result.add(dto);
            }
        }
        return result;
    }

    public List<articleDto> initArticle() {
        Set<Object> keys = stringRedisTemplate.opsForHash().keys("article");
        List<articleDto> articleDtos = redisHashTemplateConfig.getHashObjectList("article",keys,articleDto.class);
        if(articleDtos != null && !articleDtos.isEmpty()) {
            return articleDtos;
        }
        List<article> articles = articleDao.findAll();
        List<articleDto> articleDto = new ArrayList<>();
        for (article a : articles) {
            articleDto articleDto1 = new articleDto();
            BeanUtils.copyProperties(a,articleDto1);
            Hibernate.initialize(a.getUser());
            articleDto1.setNikeName(a.getUser().getNikeName());
            articleDto1.setAvatar_src(a.getUser().getAvatar_src());
            articleDto.add(articleDto1);
            redisHashTemplateConfig.setHashObject("article","article:"+a.getArticleId(),articleDto1);
        }
        return articleDto;
    }

    public void updateArticleVisitCount(int articleId){
        RLock lock = redissonClient.getLock("lock:articleVisit:"+articleId);
        try{
            if(lock.tryLock(10, 5, TimeUnit.SECONDS)){
                articleDto articleDto = redisHashTemplateConfig.getHashObject("article","article:"+articleId,articleDto.class);
                articleDto.setVisitCount(articleDto.getVisitCount()+1);
                redisHashTemplateConfig.setHashObject("article","article:"+articleId,articleDto);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }

    public void updateArticleLikeCount(int articleId){
        int userId = UserContext.getCurrentToken().getUserId();
        RLock lock = redissonClient.getLock("lock:articleLike:"+articleId);
        try{
            if(lock.tryLock(10, 5, TimeUnit.SECONDS)){
                articleDto articleDto = redisHashTemplateConfig.getHashObject("article","article:"+articleId,articleDto.class);
                articleInfoDto articleInfoDto = redisHashTemplateConfig.getHashObject("articleInfo","articleInfo:"+userId+":"+articleId,articleInfoDto.class);
                if(articleInfoDto==null){
                    articleInfoDto = new articleInfoDto(articleId,userId,false,false);
                }
                articleDto.setLikeCount(articleDto.getLikeCount()+(articleInfoDto.isLike()?-1:1));
                articleInfoDto.setLike(!articleInfoDto.isLike());
                redisHashTemplateConfig.setHashObject("article","article:"+articleId,articleDto);
                redisHashTemplateConfig.setHashObject("articleInfo","articleInfo:"+userId+":"+articleId,articleInfoDto);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void updateArticleStarCount(int articleId){
        int userId = UserContext.getCurrentToken().getUserId();
        RLock lock = redissonClient.getLock("lock:articleStar:"+articleId);
        try{
            if(lock.tryLock(10, 5, TimeUnit.SECONDS)) {
                articleDto articleDto = redisHashTemplateConfig.getHashObject("article", "article:" + articleId, articleDto.class);
                articleInfoDto articleInfoDto = redisHashTemplateConfig.getHashObject("articleInfo", "articleInfo:"+userId+":"+articleId, articleInfoDto.class);
                if(articleInfoDto==null){
                    articleInfoDto = new articleInfoDto(articleId,userId,false,false);
                }
                articleDto.setStarCount(articleDto.getStarCount() + (articleInfoDto.isStar() ? -1 : 1));
                articleInfoDto.setStar(!articleInfoDto.isStar());
                redisHashTemplateConfig.setHashObject("article", "article:" + articleId, articleDto);
                redisHashTemplateConfig.setHashObject("articleInfo", "articleInfo:"+userId+":"+articleId, articleInfoDto);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public articleDto initCurrentArticle(int articleId){
        articleDto articleDto = redisHashTemplateConfig.getHashObject("article","article:"+articleId,articleDto.class);
        if(articleDto!=null)
            return articleDto;
        article article = articleDao.findByArticleId(articleId);
        articleDto articleDto1 = new articleDto();
        BeanUtils.copyProperties(article,articleDto1);
        redisHashTemplateConfig.setHashObject("article","article:"+articleId,articleDto1);
        return articleDto1;
    }
    public articleInfoDto initCurrentArticleInfo(int articleId){
        int userId = UserContext.getCurrentToken().getUserId();
        articleInfoDto articleInfoDto = redisHashTemplateConfig.getHashObject("articleInfo","articleInfo:"+userId+":"+articleId,articleInfoDto.class);
        if(articleInfoDto!=null)
            return articleInfoDto;
        articleInfo articleInfo = articleInfoDao.findByArticleIdAndUserId(articleId,userId);
        articleInfoDto articleInfoDto1 = new articleInfoDto();
        BeanUtils.copyProperties(articleInfo,articleInfoDto1);
        redisHashTemplateConfig.setHashObject("articleInfo","articleInfo:"+userId+":"+articleId,articleInfoDto1);
        return articleInfoDto1;
    }

    public void saveArticle(int articleId, String title, String content){
        article article = articleDao.findByArticleId(articleId);
        article.setTitle(title);
        article.setArticleContent(content);
        articleDao.save(article);
        Hibernate.initialize(article.getUser());
        articleDto articleDto = new articleDto();
        BeanUtils.copyProperties(article,articleDto);
        redisHashTemplateConfig.setHashObject("article","article:"+article.getArticleId(),articleDto);
    }

    public void saveArticleToDraft(String title, String content){
        int userId = UserContext.getCurrentToken().getUserId();
        articleDraft articleDraft = new articleDraft(title,content);
        redisHashTemplateConfig.setHashObject("articleDraft",String.valueOf(userId),articleDraft);
    }

    public articleDraft getDraft(){
        int userId = UserContext.getCurrentToken().getUserId();
        articleDraft articleDraft =redisHashTemplateConfig.getHashObject("articleDraft",String.valueOf(userId),articleDraft.class);
        return articleDraft;
    }

    public void newArticle(String title, String content){
        int userId = UserContext.getCurrentToken().getUserId();
        mybatisDao.saveNewArticle(title,content,userId);
        initArticles();
        stringRedisTemplate.opsForHash().delete("articleDraft",String.valueOf(userId));
    }

    public void deleteArticle(int articleId){
        article article = articleDao.findByArticleId(articleId);
        articleDao.delete(article);
        stringRedisTemplate.opsForHash().delete("article","article:"+article.getArticleId());
    }

    @Scheduled(cron = "0 0 3 * * ?")
    @NoAop
    public void updateArticleToRedis(){
        stringRedisTemplate.delete("article");
        stringRedisTemplate.delete("articleInfo");
        initArticles();
        initArticleInfo();
    }
    @Scheduled(fixedRate = 1000*60)
    public void updateArticleToDb(){
        List<articleDto> articleDtos = initArticle();
        List<articleInfoDto> articleInfoDtos = getAllArticleInfo();
        List<article> articles = new ArrayList<>();
        for (articleDto articleDto : articleDtos) {
            article article = new article();
            BeanUtils.copyProperties(articleDto,article);
            articles.add(article);
        }
        for (articleInfoDto articleInfoDto : articleInfoDtos) {
            if(!articleInfoDao.existsByArticleIdAndUserId(articleInfoDto.getArticleId(),articleInfoDto.getUserId()))
                mybatisDao.saveArticleInfo(articleInfoDto);
        }
        articleDao.saveAll(articles);
    }
}
