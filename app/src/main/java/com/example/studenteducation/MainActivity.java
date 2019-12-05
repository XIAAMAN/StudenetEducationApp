package com.example.studenteducation;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.fragment.AFragment;
import com.example.fragment.BFragment;
import com.example.fragment.CFragment;
import com.example.fragment.DFragment;
import com.example.listener.BluetoothListenerInit;
import com.example.login.LoginActivity;
import com.example.util.ActivityCollector;
import com.facebook.stetho.Stetho;
import com.jaeger.library.StatusBarUtil;
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String[] tabText = {"首页", "课程","", "成绩", "我的"};
    //未选中icon
    private int[] normalIcon = {R.mipmap.index, R.mipmap.find, R.mipmap.add_image, R.mipmap.message, R.mipmap.me};
    //选中时icon
    private int[] selectIcon = {R.mipmap.index1, R.mipmap.find1, R.mipmap.add_image, R.mipmap.message1, R.mipmap.me1};
    private List<Fragment> fragments = new ArrayList<>();
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private EasyNavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.blue));

        //时google浏览器能够连接访问
//        Stetho.initializeWithDefaults(this);

//        StatusBarUtil.setTransparent(MainActivity.this);
//        透明状态栏(最顶层)
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // Android M Permission check
//            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
//            }
//        }
        ActivityCollector.addActivity(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    //开启蓝牙搜索
                    new BluetoothListenerInit();
//                    Log.d("蓝牙", "新建对象.......................");
//                    if(!bluetoothListenerInit.getStatus()) {
//                        Log.d("蓝牙", "蓝牙监听未启动");
//                        bluetoothListenerInit.setStatus(true);
//                    } else {
//                        Log.d("蓝牙", "蓝牙监听已启动");
//                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        
        //再次判断是否有位置权限
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
        navigationBar = findViewById(R.id.navigationBar);
        fragments.add(new AFragment());
        fragments.add(new BFragment());
        fragments.add(new CFragment());
        fragments.add(new DFragment());
        View view = LayoutInflater.from(this).inflate(R.layout.custom_add_view, null);
        navigationBar.setHintPoint(1, false);
        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .mode(EasyNavigationBar.MODE_ADD)
                .addCustomView(view)
                .addLayoutRule(EasyNavigationBar.RULE_CENTER)
                .fragmentManager(getSupportFragmentManager())
                .build();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.i("lgq","onRequestPermissionsResult ....."+requestCode);
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
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
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

    @Override
    protected void onDestroy() {
//        super.onDestroy();
        ActivityCollector.removeActivity(this);
//        finish();
    }
}
