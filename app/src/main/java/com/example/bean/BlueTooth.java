package com.example.bean;

import org.litepal.crud.LitePalSupport;

public class BlueTooth extends LitePalSupport {

    private String blueToothId;

    private String userId;

    private String friendId;

    private String userMacAddress;

    private String friendMacAddress;

    private float blueDistance;

    private String blueTime;

    public String getBlueToothId() {
        return blueToothId;
    }

    public void setBlueToothId(String blueToothId) {
        this.blueToothId = blueToothId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getUserMacAddress() {
        return userMacAddress;
    }

    public void setUserMacAddress(String userMacAddress) {
        this.userMacAddress = userMacAddress;
    }

    public String getFriendMacAddress() {
        return friendMacAddress;
    }

    public void setFriendMacAddress(String friendMacAddress) {
        this.friendMacAddress = friendMacAddress;
    }

    public float getBlueDistance() {
        return blueDistance;
    }

    public void setBlueDistance(float blueDistance) {
        this.blueDistance = blueDistance;
    }

    public String getBlueTime() {
        return blueTime;
    }

    public void setBlueTime(String blueTime) {
        this.blueTime = blueTime;
    }
}
