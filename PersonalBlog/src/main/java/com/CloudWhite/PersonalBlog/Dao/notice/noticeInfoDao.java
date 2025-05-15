package com.CloudWhite.PersonalBlog.Dao.notice;

import com.CloudWhite.PersonalBlog.Entity.notice.noticeInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface noticeInfoDao extends JpaRepository<noticeInfo,Integer> {
    public noticeInfo findByNoticeIdAndUserId(int noticeId,int userId);
    @Query(value = "SELECT noticeinfo_id FROM noticeinfo",nativeQuery = true)
    public List<Integer> getAllNoticeInfoIds();

    @Query(value = "SELECT noticeinfo_id FROM noticeinfo WHERE user_id=:userId AND notice_id = :noticeId",nativeQuery = true)
    public int getNoticeInfoId(int userId,int noticeId);
}
