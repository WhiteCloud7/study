package com.WhiteCloud.SpringBootTest.Entity;

public class premission {
    private int premissionId;
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
