package com.example.login;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import me.weishu.reflection.Reflection;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText accountText;
    private EditText passwordText;
    private IntentFilter intentFilter;
    private NetWorkChangeReceiver netWorkChangeReceiver;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //如果用户已经登录则进入主页
        if(isLogin()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        //时google浏览器能够连接访问
//        Stetho.initializeWithDefaults(this);
//        StatusBarUtil.setTransparent(LoginActivity.this);
//        透明状态栏(最顶层)
         getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    getPermissionRequest();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        String mac = getBluetoothAddress();
//
////        String mac = getBluetoothAddress();
//        if(mac == null) {
//            Log.d("蓝牙", "mac地址获取失败");
//        }else {
//            Log.d("蓝牙", mac);
//        }

//        开启蓝牙搜索
//        BluetoothListenerInit bluetoothListenerInit = new BluetoothListenerInit();
//        if(!bluetoothListenerInit.getStatus()) {
//            Log.d("蓝牙", "蓝牙监听未启动");
//            bluetoothListenerInit.setStatus(true);
//        } else {
//            Log.d("蓝牙", "蓝牙监听已启动");
//        }


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

    public void getPermissionRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
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
            Toast.makeText(LoginActivity.this, "登录失败，当前用户类型不能登录", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, "登录失败，用户名或密码错误", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("lgq","onRequestPermissionsResult ....."+requestCode);
        if (requestCode == PERMISSION_REQUEST_COARSE_LOCATION) {
            boolean isAllGranted = true;
            for (int grant : grantResults) {  // 判断是否所有的权限都已经授予了
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) { // 所有的权限都授予了

            } else {// 提示需要权限的原因
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("系统需要使用该权限，否则无法使用，是否再次开启？")
                        .setTitle("提示")
                        .setCancelable(false)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                builder.create().show();
            }
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

    public static String getBluetoothAddress() {
        //区分版本
        if (Build.VERSION.SDK_INT>=22){
            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            } catch (Exception ex) {
            }
            return "";
        }else {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            String macAddr = adapter.getAddress();
            return macAddr;
        }
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Reflection.unseal(base);
    }

    //判断用户是否登录
    public boolean isLogin()
    {
        return new SysUserDb().getUser();
    }
}
