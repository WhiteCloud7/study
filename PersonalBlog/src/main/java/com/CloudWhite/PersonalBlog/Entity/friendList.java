package com.CloudWhite.PersonalBlog.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "friendlist")
public class friendList{
    @Id
    @Schema(name="好友列表")
    private int friendListId;
    @Schema(name="好友ID")
    private int friendId;
    @Schema(name="好友对象")
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private user user;

    public friendList() {
    }

    public friendList(int friendListId, int friendId) {
        this.friendListId = friendListId;
        this.friendId = friendId;
    }

    public friendList(int friendListId, int friendId, com.CloudWhite.PersonalBlog.Entity.user user) {
        this.friendListId = friendListId;
        this.friendId = friendId;
        this.user = user;
    }

    public int getFriendListId() {
        return friendListId;
    }

    public void setFriendListId(int friendListId) {
        this.friendListId = friendListId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public com.CloudWhite.PersonalBlog.Entity.user getUser() {
        return user;
    }

    public void setUser(com.CloudWhite.PersonalBlog.Entity.user user) {
        this.user = user;
    }

    public int getUserId() {
        return user != null ? user.getUserId() : 0;
    }
    public void setUserId(int userId) {
        if(user != null) user.setUserId(userId);
    }
}
