package com.CloudWhite.PersonalBlog.Dao.notice;

import com.CloudWhite.PersonalBlog.Entity.notice.noticeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface noticeInfoDao extends JpaRepository<noticeInfo,Integer> {
    public noticeInfo findByNoticeIdAndUserId(int noticeId,int userId);
    @Query(value = "SELECT notice_id FROM noticeinfo",nativeQuery = true)
    public List<Integer> getAllNoticeIds();
}
