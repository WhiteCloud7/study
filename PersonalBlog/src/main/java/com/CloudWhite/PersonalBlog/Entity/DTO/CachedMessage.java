package com.CloudWhite.PersonalBlog.Entity.DTO;

public class CachedMessage {
    private Integer user_id;
    private Integer friend_id;
    private String message_id;
    private String sender_name;
    private String receiver_name;
    private String message;
    private String send_time;

    public CachedMessage() {
    }

    public CachedMessage(Integer user_id, Integer friend_id, String message_id, String sender_name, String receiver_name, String message, String send_time) {
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.message_id = message_id;
        this.sender_name = sender_name;
        this.receiver_name = receiver_name;
        this.message = message;
        this.send_time = send_time;
    }

    public CachedMessage(Integer user_id, Integer friend_id, String sender_name, String receiver_name, String message, String send_time) {
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.sender_name = sender_name;
        this.receiver_name = receiver_name;
        this.message = message;
        this.send_time = send_time;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(Integer friend_id) {
        this.friend_id = friend_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }
}

