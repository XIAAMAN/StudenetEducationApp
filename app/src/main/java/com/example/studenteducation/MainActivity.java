package com.example.studenteducation;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.fragment.AFragment;
import com.example.fragment.BFragment;
import com.example.fragment.CFragment;
import com.example.fragment.DFragment;
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
    private EasyNavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.blue));

        //时google浏览器能够连接访问
        Stetho.initializeWithDefaults(this);
//        StatusBarUtil.setTransparent(MainActivity.this);
//        透明状态栏(最顶层)
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

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

}
