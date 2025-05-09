package com.CloudWhite.PersonalBlog.Entity.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Schema(name="用户")
@Table(name="user")
public class userInfo {
    @Id
    @Schema(name="用户ID")
    private int userId;
    @Schema(name="用户名")
    private String username;
    @Schema(name="用户昵称")
    private String nikeName;
    @Schema(name="用户性别")
    private String sex;
    @Schema(name="用户出生日期")
    private String birthday;
    @Schema(name="用户电话")
    private String phone;
    @Schema(name="用户QQ")
    private String qq;
    @Schema(name="用户微信")
    private String wechat;
    @Schema(name="用户毕业院校")
    private String school;
    @Schema(name="用户头像路径")
    private String avatar_src;
    public userInfo() {
    }

    public userInfo(int userId, String username, String nikeName, String sex, String birthday, String phone, String qq, String wechat, String school, String avatar_src) {
        this.userId = userId;
        this.username = username;
        this.nikeName = nikeName;
        this.sex = sex;
        this.birthday = birthday;
        this.phone = phone;
        this.qq = qq;
        this.wechat = wechat;
        this.school = school;
        this.avatar_src = avatar_src;
    }

    public userInfo(String username, String nikeName, String sex, String birthday, String phone, String qq, String wechat, String school, String avatar_src) {
        this.username = username;
        this.nikeName = nikeName;
        this.sex = sex;
        this.birthday = birthday;
        this.phone = phone;
        this.qq = qq;
        this.wechat = wechat;
        this.school = school;
        this.avatar_src = avatar_src;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAvatar_src() {
        return avatar_src;
    }

    public void setAvatar_src(String avatar_src) {
        this.avatar_src = avatar_src;
    }
}
