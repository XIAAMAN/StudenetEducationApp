package com.example.bean;

import org.litepal.crud.LitePalSupport;

public class SysCollection extends LitePalSupport {
    private String collectionId;

    private String collectionName;

    private float collectionRate;       //每次试验在整个实验课程中的占比

    private String collectionCreateUserName;       //创建人

    private String collectionStartTime;             //本次试验开始时间

    private String collectionEndTime;               //本次试验结束时间



    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public float getCollectionRate() {
        return collectionRate;
    }

    public void setCollectionRate(float collectionRate) {
        this.collectionRate = collectionRate;
    }

    public String getCollectionCreateUserName() {
        return collectionCreateUserName;
    }

    public void setCollectionCreateUserName(String collectionCreateUserName) {
        this.collectionCreateUserName = collectionCreateUserName;
    }

    public String getCollectionStartTime() {
        return collectionStartTime;
    }

    public void setCollectionStartTime(String collectionStartTime) {
        this.collectionStartTime = collectionStartTime;
    }

    public String getCollectionEndTime() {
        return collectionEndTime;
    }

    public void setCollectionEndTime(String collectionEndTime) {
        this.collectionEndTime = collectionEndTime;
    }
}
