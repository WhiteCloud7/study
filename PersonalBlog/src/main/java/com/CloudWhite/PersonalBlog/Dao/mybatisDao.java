package com.CloudWhite.PersonalBlog.Dao;

import com.CloudWhite.PersonalBlog.Entity.DTO.CachedMessage;
import com.CloudWhite.PersonalBlog.Entity.DTO.articleDto;
import com.CloudWhite.PersonalBlog.Entity.DTO.articleInfoDto;
import com.CloudWhite.PersonalBlog.Entity.article.article;
import com.CloudWhite.PersonalBlog.Entity.notice.noticeInfo;
import com.CloudWhite.PersonalBlog.Entity.project;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface mybatisDao {
    @Update("LOCK TABLES message WRITE")
    public void lockMessageTable();

    @Select("call ResetMessageAutoIncrement()")
    public void ResetMessageAutoIncrement();

    @Update("UNLOCK TABLES")
    public void unlockMessageTable();

    @Insert("INSERT INTO articleinfo (`user_id`,article_id) VALUES (#{userId},#{articleId})")
    void setArticleInfo(int userId,int articleId);

    List<CachedMessage> getAllRecentMessagesForAllFriends();

    @Insert("INSERT INTO noticeinfo (user_id, is_like, notice_id) VALUES (#{userId}, #{isLike}, #{noticeId})")
    void saveNoticeInfo(noticeInfo noticeInfo);

    @Insert("INSERT INTO project (file_name,modify_time,TYPE,directory_level,parent_directory_id)VALUES (#{fileName},#{modifyTime},#{type},#{dirLevel},#{parentDirId})")
    void saveProject(project project);

    @Insert("INSERT INTO articleinfo (user_id, is_like, is_star,article_id) VALUES (#{userId}, #{isLike},#{isStar} ,#{articleId})")
    void saveArticleInfo(articleInfoDto article);

    @Insert("INSERT INTO friendlist (user_id,friend_id) VALUES (#{userId},#{friendId})")
    void saveNewFriend(int userId,int friendId);

    @Insert("INSERT INTO article (title,article_content,user_id) VALUES (#{title},#{content},#{userId}) ")
    void saveNewArticle(String title,String content,int userId);
}
