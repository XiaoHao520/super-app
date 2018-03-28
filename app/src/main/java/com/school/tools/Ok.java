package com.school.tools;

import android.os.Environment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by XIAOHAO-PC on 2018-03-10.
 */

public class Ok {
    private static String url;

    public static void uploadCards(ArrayList<String> imgs, String content,String username,String url) throws IOException {
        MultipartBody.Builder builder = null;
        OkHttpClient okHttpClient = new OkHttpClient();

        if (imgs.size() == 0) {
            builder=new MultipartBody.Builder();
        } else {
           for (int i = 0; i < imgs.size(); i++) {
                File file = new File(imgs.get(i));
                if(file.exists()){
                    System.out.println("文件存在");
                }else {
                    System.out.println("文件不存在");
                }
                builder = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("img", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
           }
        }
        RequestBody body = builder.addFormDataPart("size",String.valueOf(imgs.size())).addFormDataPart("username",username).addFormDataPart("content",content).build();
        Request request=new Request.Builder().url(url).post(body).build();
        Call call=okHttpClient.newCall(request);
        Response res=call.execute();
        System.out.println(res.body().string());
    }
    public static String getUserInfo(String url) throws IOException {
        System.out.println(url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if(response.body()!=null){


               return response.body().string();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
