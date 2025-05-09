package com.CloudWhite.PersonalBlog.Dao;

import com.CloudWhite.PersonalBlog.Entity.message;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface messageDao extends JpaRepository<message,Integer> {
    @Query(value = "SELECT * FROM message WHERE sender_name = :senderName AND receiver_name = :receiverName OR sender_name = :receiverName AND receiver_name = :senderName",nativeQuery = true)
    public List<message> getAllMessages(@Param("senderName") String senderName, @Param("receiverName") String receiverName);
    @Query(value = "SELECT message_id,message,send_time FROM message WHERE sender_name = :senderName AND receiver_name = :receiverName",nativeQuery = true)
    public List<String[]> getSentMessages(@Param("senderName") String senderName, @Param("receiverName") String receiverName);

    public message findByMessageId(int messageId);

    public message findBySenderNameAndReceiverNameAndSendTime(String senderName,String receiveName,String sendTime);

    @Query(value = "SELECT message_id,message,send_time FROM message WHERE sender_name = :friendName AND receiver_name = :username AND send_Time > :sendTime;",nativeQuery = true)
    public List<String[]> getReceiveMessages(@Param("friendName") String friendName, @Param("username") String username,@Param("sendTime") String sendTime );
}
