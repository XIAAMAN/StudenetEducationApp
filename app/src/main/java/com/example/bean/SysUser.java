package com.example.bean;

import org.litepal.crud.LitePalSupport;

public class SysUser extends LitePalSupport {
    private String userId;

    private String userName;        //用户账号名称（唯一）

    private String userRealName;

    private String userClass;       //班级

    private String userNumber;      //学号或工号

    private String userPhone;

    private String userPassword;

    private String userEmail;

    private String userRecommendName;       //用户推荐人账号名称，针对于助教用户，需要存取创建这个身份的老师账号名称

    private int userStatus;

    private String userMacAddress;

    public String getUserMacAddress() {
        return userMacAddress;
    }

    public void setUserMacAddress(String userMacAddress) {
        this.userMacAddress = userMacAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserRecommendName() {
        return userRecommendName;
    }

    public void setUserRecommendName(String userRecommendName) {
        this.userRecommendName = userRecommendName;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
}

