package com.CloudWhite.PersonalBlog.Dao.notice;

import com.CloudWhite.PersonalBlog.Entity.notice.notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface noticeDao extends JpaRepository<notice,Integer> {
    public notice findByNoticeId(int noticeId);
}
