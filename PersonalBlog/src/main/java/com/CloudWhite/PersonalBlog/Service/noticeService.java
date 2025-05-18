package com.CloudWhite.PersonalBlog.Service;

import com.CloudWhite.PersonalBlog.Entity.notice.notice;
import com.CloudWhite.PersonalBlog.Entity.notice.noticeInfo;

import java.util.List;

public interface noticeService {
    public noticeInfo getNoticeInfo(int noticeId);
    public List<notice> getNoticeList();
    public void addVisitCount(int noticeId);
    public void addLike(int noticeId);
    public void addComment(int noticeId);
    public void saveNotice(int noticeId,String title,String newNotice);
    public void newNotice(String title,String messageText);
    public void deleteNotice(int noticeId);
}
