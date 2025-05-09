package com.CloudWhite.PersonalBlog.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Schema(name="用户")
@Table(name="user")
public class user {
    @Id
    @Schema(name="用户ID")
    private int userId;
    @Schema(name="用户名")
    private String username;
    @Schema(name="用户密码")
    private String password;
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
    @Schema(name="用户对象角色ID")
    @Column(name = "role_id", insertable = false, updatable = false)
    private int roleId;
    @Schema(name="用户对应角色对象")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private role role;
    @Schema(name="用户头像路径")
    private String avatar_src;

    public user() {
    }
    public user(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public user(int userId, String nikeName, String sex, String birthday, String phone, String qq, String wechat, String school, int roleId,String avatar_src) {
        this.userId = userId;
        this.nikeName = nikeName;
        this.sex = sex;
        this.birthday = birthday;
        this.phone = phone;
        this.qq = qq;
        this.wechat = wechat;
        this.school = school;
        this.roleId = roleId;
        this.avatar_src = avatar_src;
    }

    public user(int userId, String nikeName, String sex, String birthday, String phone, String qq, String wechat, String school, role role,String avatar_src) {
        this.userId = userId;
        this.nikeName = nikeName;
        this.sex = sex;
        this.birthday = birthday;
        this.phone = phone;
        this.qq = qq;
        this.wechat = wechat;
        this.school = school;
        this.role = role;
        this.avatar_src = avatar_src;
    }

    public user(int userId, String username, String password, String nikeName, String sex, String birthday, String phone, String qq, String wechat, String school, int roleId,String avatar_src) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.nikeName = nikeName;
        this.sex = sex;
        this.birthday = birthday;
        this.phone = phone;
        this.qq = qq;
        this.wechat = wechat;
        this.school = school;
        this.roleId = roleId;
        this.avatar_src = avatar_src;
    }
    public user(int userId, String username, String password, String nikeName, String sex, String birthday, String phone, String qq, String wechat, String school, role role,String avatar_src) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.nikeName = nikeName;
        this.sex = sex;
        this.birthday = birthday;
        this.phone = phone;
        this.qq = qq;
        this.wechat = wechat;
        this.school = school;
        this.role = role;
        this.avatar_src = avatar_src;
    }
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public com.CloudWhite.PersonalBlog.Entity.role getRole() {
        return role;
    }

    public void setRole(com.CloudWhite.PersonalBlog.Entity.role role) {
        this.role = role;
    }

    public String getAvatar_src() {
        return avatar_src;
    }

    public void setAvatar_src(String avatar_src) {
        this.avatar_src = avatar_src;
    }
}
