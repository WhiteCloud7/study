package com.WhiteCloud.PersonalBlog.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
public class premission {
    @Id
    @PrimaryKeyJoinColumn(name = "premissionId")
    private int premissionId;
    @Column(name = "premissionInfo")
    private String premissionInfo;

    public premission() {
    }

    public premission(int premissionId, String premissionInfo) {
        this.premissionId = premissionId;
        this.premissionInfo = premissionInfo;
    }

    public int getPremissionId() {
        return premissionId;
    }

    public void setPremissionId(int premissionId) {
        this.premissionId = premissionId;
    }

    public String getPremissionInfo() {
        return premissionInfo;
    }

    public void setPremissionInfo(String premissionInfo) {
        this.premissionInfo = premissionInfo;
    }
}
