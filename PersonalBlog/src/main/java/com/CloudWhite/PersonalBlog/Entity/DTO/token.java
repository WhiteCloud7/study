package com.CloudWhite.PersonalBlog.Entity.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
public class token {
    @Id
    @Schema(name="用户ID")
    private int userId;
    @Schema(name="用户名")
    private String username;
    @Schema(name="用户密码")
    private String password;
    @Schema(name="用户对象角色ID")
    @Column(name = "role_id", insertable = false, updatable = false)
    private int roleId;
    @Schema(name="用户对应角色对象")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private com.CloudWhite.PersonalBlog.Entity.role role;

    public token() {
    }

    public token(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public token(int userId, String username, String password, int roleId, com.CloudWhite.PersonalBlog.Entity.role role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.role = role;
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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public com.CloudWhite.PersonalBlog.Entity.role getRole() {
        return role;
    }

    public void setRole(com.CloudWhite.PersonalBlog.Entity.role role) {
        this.role = role;
    }
}
