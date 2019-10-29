package com.example.util;

import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.example.constant.Constant;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkhttpTestUtil extends AsyncTask<Object, Integer, String> {
    public static final MediaType mediaType = MediaType.parse("application/json;charset=utf-8");

    @Override
    protected String doInBackground(Object... objects) {
        String url = Constant.URL_TEST_EXERCISE;
        String result = "";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(mediaType, JSON.toJSONString(objects));
        Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            result = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        // 如果在doInBackground方法，那么就会立刻执行本方法
        // 本方法在UI线程中执行，可以更新UI元素，典型的就是更新进度条进度，一般是在下载时候使用
    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);
    }
}