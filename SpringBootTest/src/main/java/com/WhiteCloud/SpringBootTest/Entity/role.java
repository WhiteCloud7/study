package com.WhiteCloud.SpringBootTest.Entity;

public class role {
    private int roleId;
    private String roleName;
    private int premissionId;

    public role() {
    }

    public role(int roleId, String roleName, int premissionId) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.premissionId = premissionId;
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

    public int getPremissionId() {
        return premissionId;
    }

    public void setPremissionId(int premissionId) {
        this.premissionId = premissionId;
    }
}
