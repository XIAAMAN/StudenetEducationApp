package com.example.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.bean.SysUser;
import com.example.constant.Constant;
import com.example.db.SysUserDb;
import com.example.studenteducation.MainActivity;
import com.example.studenteducation.R;
import com.example.util.AsyncTAskUtil;
import com.facebook.stetho.Stetho;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.jaeger.library.StatusBarUtil;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText accountText;
    private EditText passwordText;
    private IntentFilter intentFilter;
    private NetWorkChangeReceiver netWorkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //时google浏览器能够连接访问
//        Stetho.initializeWithDefaults(this);
//        StatusBarUtil.setTransparent(LoginActivity.this);
//        透明状态栏(最顶层)
         getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkChangeReceiver = new NetWorkChangeReceiver();
        //注册广播接收器
        registerReceiver(netWorkChangeReceiver, intentFilter);
        accountText = (EditText) findViewById(R.id.userAccount);
        passwordText = (EditText) findViewById(R.id.userPassword);
        Button loginBtn = (Button) findViewById(R.id.userLogin);        //登录按钮
        loginBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击登录按钮，进行用户验证
            case R.id.userLogin:
                //账和密码都不为空,进行登录请求验证
                String userName = accountText.getText().toString();
                String password = passwordText.getText().toString();
                if ("".equals(userName) || "".equals(password)) {
                    Toast.makeText(LoginActivity.this, R.string.accountOrPasswordError, Toast.LENGTH_SHORT).show();

                } else {
                    checkLogin(userName, password);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消广播注册
        unregisterReceiver(netWorkChangeReceiver);
    }

    //进行登录验证
    private void checkLogin(final String userName, final String password){
        String[] loginUrl = {Constant.URL_Login, "userName", userName, "userPassword", password};
//        message为网络连接的结果，如果message为“”表示连接失败，否则表示连接成功为返回的json数据
        String message = "";

        try {
            message = new AsyncTAskUtil(){}.execute(loginUrl).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        Log.d("登录",message);
        if (message.length() > 100) {
            //存储登录用户
            SysUser user = JSONObject.parseObject(message, new TypeReference<SysUser>(){});
            new SysUserDb().saveUser(user);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if("400".equals(message)){
            Toast.makeText(LoginActivity.this, "登录失败，手机端只允许学生用户登录", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, "登录失败，用户名或密码错误", Toast.LENGTH_SHORT).show();

        }

    }

    //广播接收器
    class NetWorkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                Toast.makeText(context, "网络可用", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "网络不可用，请切换网络", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
