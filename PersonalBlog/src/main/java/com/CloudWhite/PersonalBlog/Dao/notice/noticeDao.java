package com.CloudWhite.PersonalBlog.Dao.notice;

import com.CloudWhite.PersonalBlog.Entity.notice.notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface noticeDao extends JpaRepository<notice,Integer> {
    public notice findByNoticeId(int noticeId);
    @Query(value = "SELECT notice_id FROM notice",nativeQuery = true)
    public List<Integer> getAllNoticeIds();
}
