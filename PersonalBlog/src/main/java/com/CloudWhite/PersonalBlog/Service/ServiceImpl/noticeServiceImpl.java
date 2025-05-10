package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import com.CloudWhite.PersonalBlog.Dao.mybatisDao;
import com.CloudWhite.PersonalBlog.Dao.notice.noticeDao;
import com.CloudWhite.PersonalBlog.Dao.notice.noticeInfoDao;
import com.CloudWhite.PersonalBlog.Entity.notice.notice;
import com.CloudWhite.PersonalBlog.Entity.notice.noticeInfo;
import com.CloudWhite.PersonalBlog.Model.Redis.redisCommonTemplate;
import com.CloudWhite.PersonalBlog.Model.Redis.redisStringTemplateConfig;
import com.CloudWhite.PersonalBlog.Model.UserContext;
import com.CloudWhite.PersonalBlog.Service.noticeService;
import com.CloudWhite.PersonalBlog.Utils.Annotation.NoAop;
import com.CloudWhite.PersonalBlog.Utils.redisUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class noticeServiceImpl implements noticeService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private redisUtils redisUtils;
    private mybatisDao mybatisDao;
    private RedissonClient redissonClient;
    private noticeDao noticeDao;
    private noticeInfoDao noticeInfoDao;
    private redisStringTemplateConfig redisStringTemplate;
    private redisCommonTemplate redisCommonTemplate;
    @Autowired
    public noticeServiceImpl(StringRedisTemplate stringRedisTemplate, com.CloudWhite.PersonalBlog.Utils.redisUtils redisUtils, com.CloudWhite.PersonalBlog.Dao.mybatisDao mybatisDao, RedissonClient redissonClient, com.CloudWhite.PersonalBlog.Dao.notice.noticeDao noticeDao, com.CloudWhite.PersonalBlog.Dao.notice.noticeInfoDao noticeInfoDao, redisStringTemplateConfig redisStringTemplate, com.CloudWhite.PersonalBlog.Model.Redis.redisCommonTemplate redisCommonTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisUtils = redisUtils;
        this.mybatisDao = mybatisDao;
        this.redissonClient = redissonClient;
        this.noticeDao = noticeDao;
        this.noticeInfoDao = noticeInfoDao;
        this.redisStringTemplate = redisStringTemplate;
        this.redisCommonTemplate = redisCommonTemplate;
    }

    @PostConstruct
    public void initNotice(){
        List<noticeInfo> noticeInfos = noticeInfoDao.findAll();
        for(noticeInfo noticeInfo : noticeInfos){
            redisStringTemplate.setObject("noticeInfo:"+noticeInfo.getNoticeId(),noticeInfo);
        }
        List<notice> notices = noticeDao.findAll();
        for(notice notice : notices){
            redisStringTemplate.setObject("notice:"+notice.getNoticeId(),notice);
        }
    }

    @PostConstruct
    public void initBloomFilter() {
        RBloomFilter<Integer> bloomFilter = redissonClient.getBloomFilter("notice-bloom");
        RBloomFilter<Integer> bloomFilter2 = redissonClient.getBloomFilter("noticeInfo-bloom");
        if (!bloomFilter.isExists()||!bloomFilter2.isExists()) {
            // 初始化布隆过滤器，100000 是预估数量，0.01 是误判率
            bloomFilter.tryInit(100000, 0.01);
            bloomFilter2.tryInit(100000, 0.01);
            // 加载已有 ID
            List<Integer> allNoticeInfoIds = noticeInfoDao.getAllNoticeIds();
            for (Integer id : allNoticeInfoIds) {
                bloomFilter.add(id);
            }
            List<Integer> allNoticeIds = noticeDao.getAllNoticeIds();
            for (Integer id : allNoticeIds) {
                bloomFilter2.add(id);
            }
        }
    }

    public notice getNotice(int noticeId){
        return redisUtils.queryWithPassThrough(
                "notice:" + noticeId,
                notice.class,
                () -> noticeDao.findByNoticeId(noticeId),
                "notice-lock:" + noticeId,
                "notice-bloom",
                noticeId
        );
    }
    public noticeInfo getNoticeInfo(int noticeId){
        int userId = UserContext.getCurrentToken().getUserId();
        return redisUtils.queryWithPassThrough(
                "noticeInfo:" + noticeId,
                noticeInfo.class,
                () -> noticeInfoDao.findByNoticeIdAndUserId(noticeId, userId),
                "noticeInfo-lock:" + noticeId,
                "noticeInfo-bloom",
                noticeId
        );
    }


    public List<notice> getNoticeList(){
        List<notice> cacheNotice = new ArrayList<>();
        Set<String> keys = stringRedisTemplate.keys("notice:*");
        for(String key : keys){
            cacheNotice.add(redisStringTemplate.getObject(key,notice.class));
        }
        return cacheNotice;
    }

    public void addVisitCount(int noticeId){
        String redisKey = "notice:" + noticeId;
        notice cacheNotice = redisStringTemplate.getObject(redisKey, notice.class);
        if (cacheNotice != null) {
            cacheNotice.setVisitCount(cacheNotice.getVisitCount()+1);
            redisStringTemplate.setObject(redisKey,cacheNotice);
        }else{
            notice notice = getNotice(noticeId);
            notice.setVisitCount(notice.getVisitCount()+1);
            noticeDao.save(notice);
            redisStringTemplate.setObject(redisKey,notice);
        }
    }

    public void addLike(int noticeId) {
        int userId = UserContext.getCurrentToken().getUserId();
        String redisNoticeKey = "notice:" + noticeId;
        String redisNoticeInfoKey = "noticeInfo:" + noticeId;

        notice cacheNotice = redisStringTemplate.getObject(redisNoticeKey, notice.class);
        noticeInfo cacheNoticeInfo = redisStringTemplate.getObject(redisNoticeInfoKey, noticeInfo.class);

        // 优先处理缓存
        if (cacheNotice != null && cacheNoticeInfo != null) {
            boolean isLike = cacheNoticeInfo.isLike();
            cacheNoticeInfo.setLike(!isLike);
            cacheNotice.setLikeCount(cacheNotice.getLikeCount() + (isLike ? -1 : 1));
            redisStringTemplate.setObject(redisNoticeKey, cacheNotice);
            redisStringTemplate.setObject(redisNoticeInfoKey, cacheNoticeInfo);
            return;
        }

        // 查询数据库
        notice notice = noticeDao.findByNoticeId(noticeId);
        noticeInfo noticeInfo = noticeInfoDao.findByNoticeIdAndUserId(noticeId, userId);

        if (notice != null && noticeInfo != null) {
            boolean isLike = noticeInfo.isLike();
            noticeInfo.setLike(!isLike);
            notice.setLikeCount(notice.getLikeCount() + (isLike ? -1 : 1));
            noticeDao.save(notice);
            noticeInfoDao.save(noticeInfo);
            redisStringTemplate.setObject(redisNoticeKey, notice);
            redisStringTemplate.setObject(redisNoticeInfoKey, noticeInfo);
        }
    }

    public void addStar(int noticeId) {
        int userId = UserContext.getCurrentToken().getUserId();
        String redisNoticeKey = "notice:" + noticeId;
        String redisNoticeInfoKey = "noticeInfo:" + noticeId;

        // 尝试从缓存读取
        notice cacheNotice = redisStringTemplate.getObject(redisNoticeKey, notice.class);
        noticeInfo cacheNoticeInfo = redisStringTemplate.getObject(redisNoticeInfoKey, noticeInfo.class);

        if (cacheNotice != null && cacheNoticeInfo != null) {
            boolean isStar = cacheNoticeInfo.isStar();
            cacheNoticeInfo.setStar(!isStar);
            cacheNotice.setStarCount(cacheNotice.getStarCount() + (isStar ? -1 : 1));
            redisStringTemplate.setObject(redisNoticeKey, cacheNotice);
            redisStringTemplate.setObject(redisNoticeInfoKey, cacheNoticeInfo);
            return;
        }

        // 查询数据库并处理
        notice notice = noticeDao.findByNoticeId(noticeId);
        noticeInfo noticeInfo = noticeInfoDao.findByNoticeIdAndUserId(noticeId, userId);

        if (notice != null && noticeInfo != null) {
            boolean isStar = noticeInfo.isStar();
            noticeInfo.setStar(!isStar);
            notice.setStarCount(notice.getStarCount() + (isStar ? -1 : 1));
            noticeDao.save(notice);
            noticeInfoDao.save(noticeInfo);
            redisStringTemplate.setObject(redisNoticeKey, notice);
            redisStringTemplate.setObject(redisNoticeInfoKey, noticeInfo);
        }
    }

    public void saveNotice(int noticeId,String title,String newNotice){
        notice notice = new notice();
        notice.setNoticeId(noticeId);
        notice.setTitle(title);
        notice.setNoticeMessage(newNotice);
        noticeDao.save(notice);
    }

    public void addComment(int noticeId){

    }

    @Scheduled(fixedRate = 1000*60)
    @NoAop
    public void updateNoticeDb(){
        List<notice> cacheNotice = new ArrayList<>();
        Set<String> keys = stringRedisTemplate.keys("notice:*");
        for(String key : keys){
            cacheNotice.add(redisStringTemplate.getObject(key,notice.class));
        }
        List<noticeInfo> cacheNoticeInfo = new ArrayList<>();
        Set<String> infoKeys = stringRedisTemplate.keys("noticeInfo:*");
        for(String key : infoKeys){
            cacheNoticeInfo.add(redisStringTemplate.getObject(key,noticeInfo.class));
        }
        noticeDao.saveAll(cacheNotice);
        noticeInfoDao.saveAll(cacheNoticeInfo);
    }
}
