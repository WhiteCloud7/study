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
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
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
            redisStringTemplate.setObject("noticeInfo:"+noticeInfo.getNoticeId()+"-"+noticeInfo.getUserId(),noticeInfo);
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
            bloomFilter.tryInit(10, 0.01);
            bloomFilter2.tryInit(10, 0.01);
            // 加载已有 ID
            List<Integer> allNoticeInfoIds = noticeInfoDao.getAllNoticeInfoIds();
            for (Integer id : allNoticeInfoIds) {
                bloomFilter.add(id);
            }
            List<Integer> allNoticeIds = noticeDao.getAllNoticeIds();
            for (Integer id : allNoticeIds) {
                System.out.println(id);
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
                "noticeInfo:" + noticeId+"-"+userId,
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
        String redisNoticeInfoKey = "noticeInfo:" + noticeId + "-" + userId;

        // 查缓存中的 notice，如果不存在就查数据库
        notice cacheNotice = redisStringTemplate.getObject(redisNoticeKey, notice.class);
        if (cacheNotice == null) {
            cacheNotice = noticeDao.findByNoticeId(noticeId);
            if (cacheNotice == null) {
                // 防御式处理：非法 noticeId
                throw new IllegalArgumentException("Notice 不存在: " + noticeId);
            }
            redisStringTemplate.setObject(redisNoticeKey, cacheNotice);
        }

        // 查用户点赞信息（不查数据库）
        noticeInfo cacheNoticeInfo = redisStringTemplate.getObject(redisNoticeInfoKey, noticeInfo.class);

        if (cacheNoticeInfo == null) {
            // 用户首次点赞，构造记录
            cacheNoticeInfo = new noticeInfo();
            cacheNoticeInfo.setNoticeId(noticeId);
            cacheNoticeInfo.setUserId(userId);
            cacheNoticeInfo.setLike(true);
            cacheNotice.setLikeCount(cacheNotice.getLikeCount() + 1);
        } else {
            // 用户已经点过赞，执行点赞状态切换
            boolean isLike = cacheNoticeInfo.isLike();
            cacheNoticeInfo.setLike(!isLike);
            cacheNotice.setLikeCount(cacheNotice.getLikeCount() + (isLike ? -1 : 1));
        }
        // 更新缓存
        redisStringTemplate.setObject(redisNoticeKey, cacheNotice);
        redisStringTemplate.setObject(redisNoticeInfoKey, cacheNoticeInfo);
        redisCommonTemplate.setExpire(redisNoticeInfoKey,2,TimeUnit.DAYS);
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
        try{
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
            for(noticeInfo cache : cacheNoticeInfo){
                if(cache.getNoticeinfoId()==0){
                    mybatisDao.saveNoticeInfo(cache);
                    int noticeInfoId = noticeInfoDao.getNoticeInfoId(cache.getUserId(),cache.getNoticeId());
                    cache.setNoticeinfoId(noticeInfoId);
                    redisStringTemplate.setObject("noticeInfo:" + cache.getNoticeId() + "-" + cache.getUserId(),cache);
                    redisCommonTemplate.setExpire("noticeInfo:" + cache.getNoticeId() + "-" + cache.getUserId(),2,TimeUnit.DAYS);
                }
                else {
                    noticeInfoDao.save(cache);
                }
            }
        }catch (Exception e){}
    }

    @Scheduled( cron = "0 0 3 * * ?")
    public void updateRedis(){
        initNotice();
    }

    @Scheduled(fixedRate = 1000 * 50*10)
    @NoAop
    public void updateBloomFilter() {
        String bloomKey1 = "notice-bloom";
        String bloomKey2 = "noticeInfo-bloom";

        // 删除旧的过滤器（清空数据）
        redissonClient.getBloomFilter(bloomKey1).delete();
        redissonClient.getBloomFilter(bloomKey2).delete();

        // 重新初始化
        RBloomFilter<Integer> bloomFilter = redissonClient.getBloomFilter(bloomKey1);
        RBloomFilter<Integer> bloomFilter2 = redissonClient.getBloomFilter(bloomKey2);
        bloomFilter.tryInit(10, 0.01);
        bloomFilter2.tryInit(10, 0.01);

        // 加载数据
        List<Integer> allNoticeInfoIds = noticeInfoDao.getAllNoticeInfoIds();
        for (Integer id : allNoticeInfoIds) {
            bloomFilter.add(id);
        }
        List<Integer> allNoticeIds = noticeDao.getAllNoticeIds();
        for (Integer id : allNoticeIds) {
            bloomFilter2.add(id);
        }
    }
}
