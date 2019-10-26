package com.example.bean;

import org.litepal.crud.LitePalSupport;

public class SysExercise extends LitePalSupport {
    private String exerciseId;

    private String exerciseUploadUserName;          //上传题目人

    private String exerciseCheckUserName;          //上传审查人

    private String exerciseName;        //题目名称

    private String exerciseTime;        //题目上传时间

    private String exerciseDescription;     //题目描述

    private String exerciseInputExample;        //输入样例

    private String exerciseOutputExample;       //输出样例

    private String exerciseErrorExample;       //输出样例

    private String exerciseWarning;     //提示（警告）信息

    private String exerciseCode;        //题目参考代码

    private float exerciseScore;        //题目分值,默认为5.0

    private String exerciseFileName;

    private String exerciseFileUrl;

    private int exerciseState;          //0表示未审核，1表示审核通过，默认为1

    private int exerciseClassifyCause;      //题目分类依据，1表示按照题目标签，2表示按照章节，3表示其他，默认为1

    private String exerciseLabel;           //题目标签，多个标签用空格隔开

    private String exerciseDifficultValue;      //题目难度，分为简单、适中、难三种

    private int exerciseType;               //1表示编程题，2表示选择题，3表示判断题、4表示填空题，默认为1

    private int exerciseStatus;             //0表示不可见，1表示可见，默认为1.当删除时，才将状态设置为0

    private String exerciseLanguage;

    private int exerciseFree;       //是否开放给学生，1表示是，0表示否，默认为1


    public int getExerciseFree() {
        return exerciseFree;
    }

    public void setExerciseFree(int exerciseFree) {
        this.exerciseFree = exerciseFree;
    }

    public String getExerciseLanguage() {
        return exerciseLanguage;
    }

    public void setExerciseLanguage(String exerciseLanguage) {
        this.exerciseLanguage = exerciseLanguage;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseUploadUserName() {
        return exerciseUploadUserName;
    }

    public void setExerciseUploadUserName(String exerciseUploadUserName) {
        this.exerciseUploadUserName = exerciseUploadUserName;
    }

    public String getExerciseCheckUserName() {
        return exerciseCheckUserName;
    }

    public void setExerciseCheckUserName(String exerciseCheckUserName) {
        this.exerciseCheckUserName = exerciseCheckUserName;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseTime() {
        return exerciseTime;
    }

    public void setExerciseTime(String exerciseTime) {
        this.exerciseTime = exerciseTime;
    }

    public String getExerciseDescription() {
        return exerciseDescription;
    }

    public void setExerciseDescription(String exerciseDescription) {
        this.exerciseDescription = exerciseDescription;
    }

    public String getExerciseInputExample() {
        return exerciseInputExample;
    }

    public void setExerciseInputExample(String exerciseInputExample) {
        this.exerciseInputExample = exerciseInputExample;
    }

    public String getExerciseOutputExample() {
        return exerciseOutputExample;
    }

    public void setExerciseOutputExample(String exerciseOutputExample) {
        this.exerciseOutputExample = exerciseOutputExample;
    }

    public String getExerciseWarning() {
        return exerciseWarning;
    }

    public void setExerciseWarning(String exerciseWarning) {
        this.exerciseWarning = exerciseWarning;
    }

    public String getExerciseCode() {
        return exerciseCode;
    }

    public void setExerciseCode(String exerciseCode) {
        this.exerciseCode = exerciseCode;
    }

    public float getExerciseScore() {
        return exerciseScore;
    }

    public void setExerciseScore(float exerciseScore) {
        this.exerciseScore = exerciseScore;
    }

    public String getExerciseFileName() {
        return exerciseFileName;
    }

    public void setExerciseFileName(String exerciseFileName) {
        this.exerciseFileName = exerciseFileName;
    }

    public String getExerciseFileUrl() {
        return exerciseFileUrl;
    }

    public void setExerciseFileUrl(String exerciseFileUrl) {
        this.exerciseFileUrl = exerciseFileUrl;
    }

    public int getExerciseState() {
        return exerciseState;
    }

    public void setExerciseState(int exerciseState) {
        this.exerciseState = exerciseState;
    }

    public int getExerciseClassifyCause() {
        return exerciseClassifyCause;
    }

    public void setExerciseClassifyCause(int exerciseClassifyCause) {
        this.exerciseClassifyCause = exerciseClassifyCause;
    }

    public String getExerciseLabel() {
        return exerciseLabel;
    }

    public void setExerciseLabel(String exerciseLabel) {
        this.exerciseLabel = exerciseLabel;
    }

    public String getExerciseDifficultValue() {
        return exerciseDifficultValue;
    }

    public void setExerciseDifficultValue(String exerciseDifficultValue) {
        this.exerciseDifficultValue = exerciseDifficultValue;
    }

    public int getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(int exerciseType) {
        this.exerciseType = exerciseType;
    }

    public int getExerciseStatus() {
        return exerciseStatus;
    }

    public void setExerciseStatus(int exerciseStatus) {
        this.exerciseStatus = exerciseStatus;
    }

    public String getExerciseErrorExample() {
        return exerciseErrorExample;
    }

    public void setExerciseErrorExample(String exerciseErrorExample) {
        this.exerciseErrorExample = exerciseErrorExample;
    }

}

