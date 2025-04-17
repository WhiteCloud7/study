package com.CloudWhite.PersonalBlog.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Schema(name="权限")
@Table(name="permission")
public class permission {
    @Id
    @Schema(name="权限ID")
    private int permissionId;
    @Schema(name="权限明细")
    private String permissionDetail;

    public permission() {
    }

    public permission(int permissionId, String permissionDetail) {
        this.permissionId = permissionId;
        this.permissionDetail = permissionDetail;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionDetail() {
        return permissionDetail;
    }

    public void setPermissionDetail(String permissionDetail) {
        this.permissionDetail = permissionDetail;
    }
}
