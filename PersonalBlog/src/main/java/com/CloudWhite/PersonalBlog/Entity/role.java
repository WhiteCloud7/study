package com.CloudWhite.PersonalBlog.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Schema(name="角色")
@Table(name = "role")
public class role {
    @Id
    @Schema(name="角色ID")
    private int roleId;
    @Schema(name="角色名")
    private String roleName;
    @Schema(name="角色对应权限ID")
    @Column(name = "permission_id", insertable = false, updatable = false)
    private int permissionId;
    @Schema(name="角色对应权限对象")
    @OneToOne
    @JoinColumn(name = "permission_id")
    private permission permission;

    public role() {
    }

    public role(int roleId, String roleName, int permissionId) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.permissionId = permissionId;
    }

    public role(int roleId, String roleName, com.CloudWhite.PersonalBlog.Entity.permission permission) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.permission = permission;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public com.CloudWhite.PersonalBlog.Entity.permission getPermission() {
        return permission;
    }

    public void setPermission(com.CloudWhite.PersonalBlog.Entity.permission permission) {
        this.permission = permission;
    }
}
