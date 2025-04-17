package com.CloudWhite.PersonalBlog.Dao.notice;

import com.CloudWhite.PersonalBlog.Entity.notice.noticeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface noticeInfoDao extends JpaRepository<noticeInfo,Integer> {
    public noticeInfo findByNoticeIdAndUserId(int noticeId,int userId);
}
