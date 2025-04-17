package com.CloudWhite.PersonalBlog.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name="message")
public class message {
    @Id
    @Schema(name="消息ID")
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageId;
    @Schema(name="发送者ID")
    @JoinColumn(name = "sender_id")
    private int senderId;
    @Schema(name = "接收者ID")
    @JoinColumn(name = "receiver_id")
    private int receiverId;
    @Schema(name = "消息内容")
    private String message;
    @Schema(name = "发送时间")
    private String sendTime;

    public message() {
    }

    public message(int messageId, int senderId, int receiverId, String message,String sendTime) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.sendTime = sendTime;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
