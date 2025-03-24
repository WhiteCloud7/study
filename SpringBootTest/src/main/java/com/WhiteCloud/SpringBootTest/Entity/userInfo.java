package com.WhiteCloud.SpringBootTest.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Schema(name="用户实体类",description = "")
@Entity
@Table(name = "userinfo")
public class userInfo {
    @Schema(name="用户唯一标识符")
    @Id
    @Column(name = "userId")
    private int userId;
    @Schema(name = "用户名")
    private String username;
    @Schema(name="密码")
    private String password;
    @Schema(name="昵称")
    private String nikename;
    @Schema(name = "用户性别")
    private String sex;
    @Schema(name = "用户电话")
    private String phone;
    @Schema(name = "用户邮箱")
    private String email;

    @Schema(name = "角色Id")
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

    public userInfo(int userId, String username, String password, com.WhiteCloud.SpringBootTest.Entity.role role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public com.WhiteCloud.SpringBootTest.Entity.role getRole() {
        return role;
    }

    public void setRole(com.WhiteCloud.SpringBootTest.Entity.role role) {
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
