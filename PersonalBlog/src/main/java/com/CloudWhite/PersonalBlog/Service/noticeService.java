package com.CloudWhite.PersonalBlog.Service;

import com.CloudWhite.PersonalBlog.Entity.notice.notice;
import com.CloudWhite.PersonalBlog.Entity.notice.noticeInfo;

import java.util.List;

public interface noticeService {
    public noticeInfo getNoticeInfo(int noticeId,int userId);
    public List<notice> getNoticeList();
    public void addVisitCount(int noticeId);
    public void addLike(int noticeId,int userId);
    public void addStar(int noticeId,int userId);
    public void addComment(int noticeId);
    public void saveNotice(int noticeId,String title,String newNotice);
}
