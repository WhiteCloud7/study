package com.WhiteCloud.SpringBootTest.Entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="用户实体类",description = "")
public class userInfo {
    @Schema(name="用户唯一标识符")
    private int userId;
    @Schema(name = "用户名")
    private String username;
    @Schema(name = "用户性别")
    private String sex;
    @Schema(name = "用户电话")
    private String phone;
    @Schema(name = "用户邮箱")
    private String email;

    public userInfo() {
    }

    public userInfo(int userId) {
        this.userId = userId;
    }

    public userInfo(String username, String sex, String phone, String email) {
        this.userId = userId;
    }

    public userInfo(int userId, String username, String sex, String phone, String email) {
        this.userId = userId;
        this.username = username;
        this.sex = sex;
        this.phone = phone;
        this.email = email;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
