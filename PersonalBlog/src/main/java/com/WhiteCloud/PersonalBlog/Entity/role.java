package com.WhiteCloud.PersonalBlog.Entity;

import jakarta.persistence.*;

@Entity
public class role {
    @Id
    @Column(name = "roleId")
    private int roleId;
    @Column(name = "roleName")
    private String roleName;
    @PrimaryKeyJoinColumn(name = "premissionId")
    @OneToOne
    @JoinColumn(name = "premissionId")
    private premission premission;
    public role() {
    }

    public role(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
    public role(int roleId) {
        this.roleId = roleId;
    }
    public premission getPremission() {
        return premission;
    }

    public void setPremission(premission premission) {
        this.premission = premission;
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
}
