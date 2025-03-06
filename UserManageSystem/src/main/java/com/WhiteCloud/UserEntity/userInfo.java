package com.WhiteCloud.UserEntity;

public class userInfo {
    private int userId;
    private String username;
    private String sex;
    private String phone;
    private String email;
    public userInfo(){
        super();
    }

    public userInfo(String username,String sex,String phone,String email){
        this.username=username;
        this.sex=sex;
        this.phone=phone;
        this.email=email;
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
}
