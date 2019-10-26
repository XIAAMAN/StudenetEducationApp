package com.example.db;

import com.example.bean.SysUser;

import org.litepal.LitePal;

public class DeleteAllData {

    public DeleteAllData() {
        LitePal.getDatabase();
    }

    //删除所有数据
    public void deleteAll() {

    }

    //删除用户登录信息
    public void deleteUserData() {
        LitePal.deleteAll(SysUser.class);
    }

}
