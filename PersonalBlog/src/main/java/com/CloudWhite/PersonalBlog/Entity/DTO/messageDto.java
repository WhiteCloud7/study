package com.CloudWhite.PersonalBlog.Entity.DTO;

public class messageDto {
    private String messageId;
    private String receiverName;
    private String message;
    private String sendTime;

    public messageDto(String messageId, String receiverName, String message, String sendTime) {
        this.messageId = messageId;
        this.receiverName = receiverName;
        this.message = message;
        this.sendTime = sendTime;
    }

    public messageDto() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
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
