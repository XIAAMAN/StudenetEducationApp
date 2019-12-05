package com.example.db;

import com.example.bean.SysUser;

import org.litepal.LitePal;

public class SysUserDb {

    public SysUserDb() {
        LitePal.getDatabase();
    }

    //删除用户登录信息
    public void deleteUserData() {
        LitePal.deleteAll(SysUser.class);
    }

    //保存用户
    public void saveUser(SysUser user) {
        deleteUserData();
        user.save();
    }



    //查询用户名
    public String getUserName() {
        SysUser user = LitePal.findFirst(SysUser.class);
        return user.getUserName();
    }

    //判断用户是否已经登录
    public boolean getUser() {
        SysUser user = LitePal.findFirst(SysUser.class);
        if(user != null) {
            return true;
        } else {
            return false;
        }
    }

    //获得用户
    public SysUser getSysUser() {
        return LitePal.findFirst(SysUser.class);
    }
}
