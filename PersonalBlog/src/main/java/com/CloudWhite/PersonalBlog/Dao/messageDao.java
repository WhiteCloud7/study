package com.CloudWhite.PersonalBlog.Dao;

import com.CloudWhite.PersonalBlog.Entity.message;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface messageDao extends JpaRepository<message,Integer> {
    @Query(value = "SELECT * FROM message WHERE sender_id = :senderId AND receiver_id = :receiverId OR sender_id = :receiverId AND receiver_id = :senderId",nativeQuery = true)
    public List<message> getAllMessages(@Param("senderId") int senderId, @Param("receiverId") int receiverId);
    @Query(value = "SELECT message_id,message,send_time FROM message WHERE sender_id = :senderId AND receiver_id = :receiverId",nativeQuery = true)
    public List<String[]> getSentMessages(@Param("senderId") int senderId, @Param("receiverId") int receiverId);

    public message findByMessageId(int messageId);

    @Query(value = "SELECT message_id FROM message WHERE sender_id = :senderId AND receiver_id = :receiverId AND send_time = :sendTime", nativeQuery = true)
    public int getSentMessageId(int senderId,int receiverId,String sendTime);

    @Query(value = "SELECT message_id,message,send_time FROM message WHERE sender_id = :friendId AND receiver_id = :userId;",nativeQuery = true)
    public List<String[]> getReceiveMessages(@Param("friendId") int friendId, @Param("userId") int userId);
}
