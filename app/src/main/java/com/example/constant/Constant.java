package com.example.constant;

public class Constant {
    public static final String IP = "10.13.1.246";
    public static final String URL = "http://"+ IP +":8848/"; // IP地址请改为你自己的IP
    public static final String URL_Login = URL + "appLogin";           //登录
    public static final String URL_GET_COLLECTION = URL + "appGetCollection"; //获取题目集
    public static final String URL_GET_EXERCISE = URL + "appGetExercise";      //根据题目集获得题目
    public static final String URL_UPLOAD_FILE = URL + "appUploadFile";  //上传文件
    public static final String URL_SUBMIT_EXERCISE = URL + "appCompile/submit";  //提交题目
    public static final String URL_TEST_EXERCISE = URL + "appCompile/test";  //在线运行题目
    public static final String URL_SEND_BLUETOOTH = URL + "blue/save";  //发送蓝牙信息
    public static final String URL_SEND_CHAT_MESSAGE = URL + "sendChatMessage";  //发送聊天消息
}
