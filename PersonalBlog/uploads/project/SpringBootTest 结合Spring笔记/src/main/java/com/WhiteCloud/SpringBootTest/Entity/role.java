package com.WhiteCloud.SpringBootTest.Entity;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

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
    public com.WhiteCloud.SpringBootTest.Entity.premission getPremission() {
        return premission;
    }

    public void setPremission(com.WhiteCloud.SpringBootTest.Entity.premission premission) {
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
