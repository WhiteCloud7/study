package com.WhiteCloud.PersonalBlog.Entity;

import jakarta.persistence.*;
@Entity
@Table(name = "userinfo")
public class userInfo {
    @Id
    @Column(name = "userId", updatable = false, nullable = false)
    private int userId;
    private String username;
    private String password;
    private String nikename;
    private String sex;
    private String phone;
    private String email;
    @OneToOne
    @JoinColumn(name = "roleId")
    private role role;

    public userInfo(String username, String nikename, String sex, String phone, String email) {
        this.username = username;
        this.nikename = nikename;
        this.sex = sex;
        this.phone = phone;
        this.email = email;
    }

    public userInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public userInfo(int userId, String username, String password, role role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public role getRole() {
        return role;
    }

    public void setRole(role role) {
        this.role = role;
    }

    public userInfo() {
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
    public String getPassword() {
        return password;
    }

    public String getNikename() {
        return nikename;
    }

    public void setNikename(String nikename) {
        this.nikename = nikename;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}