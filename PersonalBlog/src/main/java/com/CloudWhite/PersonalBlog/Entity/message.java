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
    @Schema(name="发送者名")
    @JoinColumn(name = "sender_name")
    private String senderName;
    @Schema(name = "接收者名")
    @JoinColumn(name = "receiver_name")
    private String receiverName;
    @Schema(name = "消息内容")
    private String message;
    @Schema(name = "发送时间")
    private String sendTime;

    public message() {
    }

    public message(int messageId, String senderName, String receiverName, String message, String sendTime) {
        this.messageId = messageId;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.message = message;
        this.sendTime = sendTime;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
