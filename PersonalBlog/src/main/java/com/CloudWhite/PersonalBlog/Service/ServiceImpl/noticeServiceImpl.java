package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import com.CloudWhite.PersonalBlog.Dao.notice.noticeDao;
import com.CloudWhite.PersonalBlog.Dao.notice.noticeInfoDao;
import com.CloudWhite.PersonalBlog.Entity.notice.notice;
import com.CloudWhite.PersonalBlog.Entity.notice.noticeInfo;
import com.CloudWhite.PersonalBlog.Service.noticeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class noticeServiceImpl implements noticeService {
    noticeDao noticeDao;
    noticeInfoDao noticeInfoDao;
    @Autowired
    public noticeServiceImpl(noticeDao noticeDao,noticeInfoDao noticeInfoDao) {
        this.noticeDao = noticeDao;
        this.noticeInfoDao = noticeInfoDao;
    }

    public notice getNotice(int noticeId){
        return noticeDao.findByNoticeId(noticeId);
    }

    public noticeInfo getNoticeInfo(int noticeId,int userId){
        noticeInfo noticeInfo = noticeInfoDao.findByNoticeIdAndUserId(noticeId,userId);
        if (noticeInfo != null) {
            return noticeInfo;
        }else{
            noticeInfo noticeInfo2 = new noticeInfo();
            noticeInfo2.setNoticeId(noticeId);
            noticeInfo2.setUserId(userId);
            noticeInfoDao.save(noticeInfo2);
            return noticeInfo2;
        }
    }

    public List<notice> getNoticeList(){
        return noticeDao.findAll();
    }

    public void addVisitCount(int noticeId){
        notice notice = getNotice(noticeId);
        notice.setVisitCount(notice.getVisitCount()+1);
        noticeDao.save(notice);
        notice.getVisitCount();
    }

    public void addLike(int noticeId,int userId){
        noticeInfo noticeInfo = noticeInfoDao.findByNoticeIdAndUserId(noticeId,userId);
        if(noticeInfo!=null){
            boolean isLike = noticeInfo.isLike();
            if(!isLike) {
                noticeInfo.setLike(true);
                noticeInfo.getNotice().setLikeCount(noticeInfo.getNotice().getLikeCount()+1);
                noticeInfoDao.save(noticeInfo);
            }else{
                noticeInfo.setLike(false);
                noticeInfo.getNotice().setLikeCount(noticeInfo.getNotice().getLikeCount()-1);
                noticeInfoDao.save(noticeInfo);
            }
        }
    }

    public void addStar(int noticeId,int userId){
        noticeInfo noticeInfo = noticeInfoDao.findByNoticeIdAndUserId(noticeId,userId);
        if(noticeInfo!=null){
            boolean isStar = noticeInfo.isStar();
            if(!isStar) {
                noticeInfo.setStar(true);
                noticeInfo.getNotice().setStarCount(noticeInfo.getNotice().getStarCount()+1);
                noticeInfoDao.save(noticeInfo);
            }else{
                noticeInfo.setStar(false);
                noticeInfo.getNotice().setStarCount(noticeInfo.getNotice().getStarCount()-1);
                noticeInfoDao.save(noticeInfo);
            }
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
}
