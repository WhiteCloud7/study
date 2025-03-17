package com.cloudwhite.springboottest.Dao;

public class UserInfo {
    private int userId;
    private String username;
    private String sex;
    private String phone;
    private String email;

    public UserInfo() {}

    public UserInfo(int userId, String username, String sex, String phone, String email) {
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
