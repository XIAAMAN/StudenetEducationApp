package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.activity.ExerciseDetailActivity;
import com.example.adapter.CollectionAdapter;
import com.example.bean.SysCollection;
import com.example.bean.SysExercise;
import com.example.constant.Constant;
import com.example.db.SysCollectionDb;
import com.example.db.SysExerciseDb;
import com.example.db.SysUserDb;
import com.example.studenteducation.R;
import com.example.util.AsyncTAskUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jue on 2018/6/2.
 */

public class BFragment extends android.support.v4.app.Fragment {

    private List<SysCollection> collectionList = new ArrayList<>();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collection_list, container,false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        SysCollectionDb collectionDb = new SysCollectionDb();
        final String[] collectionUrl = {Constant.URL_GET_COLLECTION, "userName", new SysUserDb().getUserName()};
//        message为网络连接的结果，如果message为“”表示连接失败，否则表示连接成功为返回的json数据
        String message = "";

        try {
            message = new AsyncTAskUtil(){}.execute(collectionUrl).get();
            JSONArray objects = JSON.parseArray(message);
            List<SysCollection> newCollectionlist = new ArrayList<>();
            SysCollection sysCollection;
            for(int i=0; i<objects.size(); i++) {
                sysCollection = new SysCollection();
                sysCollection.setCollectionName(objects.getJSONObject(i).getString("collectionName"));
                sysCollection.setCollectionId(objects.getJSONObject(i).getString("collectionId"));
                sysCollection.setCollectionRate(objects.getJSONObject(i).getInteger("collectionRate"));
                sysCollection.setCollectionStartTime(objects.getJSONObject(i).getString("collectionStartTime"));
                sysCollection.setCollectionCreateUserName(objects.getJSONObject(i).getString("collectionCreateUserName"));
                sysCollection.setCollectionEndTime(objects.getJSONObject(i).getString("collectionEndTime"));
                newCollectionlist.add(sysCollection);
            }

            collectionDb.deleteAll();
            collectionDb.saveCollectionList(newCollectionlist);
            collectionList = collectionDb.getCollectionList();
            CollectionAdapter adapter = new CollectionAdapter(getActivity(), R.layout.collection_menu, collectionList);
            ListView listView = (ListView) getActivity().findViewById(R.id.collection_list_view);
            listView.setAdapter(adapter);

            //设置子项监听事件
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SysCollection collection = collectionList.get(position);
                    String time = sdf.format(new Date());
                    if(time.compareTo(collection.getCollectionEndTime())<=0 && time.compareTo(collection.getCollectionStartTime())>=0) {
//                        Toast.makeText(getActivity(), collection.getCollectionName(), Toast.LENGTH_SHORT).show();
//                        FragmentManager fragmentManager = getSupportFragmentManager();
                        //先根据题目集id查询题目
                        getExerciseListByCollectionId(collection.getCollectionId());

                        //删除本地数据库以前题目，然后将现在的题目存储到数据库中

                        Intent intent = new Intent(getActivity(), ExerciseDetailActivity.class);
                        intent.putExtra("collectionId", collection.getCollectionId());
                        startActivity(intent);
                    }

                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //请求题目集数据
    public void getExerciseListByCollectionId(String collectionId) {
        String[] exerciseUrl = {Constant.URL_GET_EXERCISE, "collectionId", collectionId,"userName",new SysUserDb().getUserName()};
        String message;
        try {
            message = new AsyncTAskUtil(){}.execute(exerciseUrl).get();
            List<SysExercise> exerciseList = JSON.parseObject(message, new TypeReference<List<SysExercise>>(){});
            SysExerciseDb sysExerciseDb = new SysExerciseDb();
            //先删除本地数据库数据
            sysExerciseDb.deleteAll();
            //将现在的题目数据存储到数据库
            sysExerciseDb.saveExerciseList(exerciseList);
//            TextView exerciseName  = (TextView) getActivity().findViewById(R.id.exercise_name);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
