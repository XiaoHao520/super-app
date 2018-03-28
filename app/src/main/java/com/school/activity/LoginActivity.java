package com.school.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.school.R;

import com.school.entity.UserEntity;
import com.school.tools.Ok;

import org.litepal.crud.DataSupport;

import java.io.IOException;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class LoginActivity extends AppCompatActivity {
    WebView loginWeb;
    SharedPreferences sharedPreferences;
    private String url = "http://super.sinbel.top/user/info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences=getSharedPreferences("localUser",MODE_PRIVATE);
        initView();
    }

    private void initView() {
        loginWeb = (WebView) findViewById(R.id.loginWeb);
        loginWeb.addJavascriptInterface(this, "android");
        WebSettings setting = loginWeb.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        loginWeb.loadUrl("http://www.sinbel.top/ui/web/user/login.html");
        loginWeb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        loginWeb.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                new AlertDialog.Builder(LoginActivity.this).
                        setTitle("提示").setMessage(message).setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //TODO
                            }
                        }).create().show();
                result.confirm();
                return true;
            }
        });

    }

    @JavascriptInterface
    public void saveUserInfo() {

    }


    @JavascriptInterface
    public void login(final String name, String password,String id){
        //登录到极光 和 服务器
        System.out.println(name+"____________"+password);

        class MThread implements Runnable {

            Handler handler;
            String url;


            public MThread(Handler handler, String url) {

                this.handler = handler;
                this.url = url;
            }

            @Override
            public void run() {

                android.os.Message msg = new android.os.Message();
                String res = null;
                try {
                    res = Ok.getUserInfo(this.url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject obj = JSON.parseObject(res);
                if (obj != null) {

                    msg.obj = obj;
                }
                handler.sendMessage(msg);
            }
        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                JSONObject obj = (JSONObject) msg.obj;
                System.out.println(obj.toString());

                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString("username",obj.get("username").toString());
                editor.putString("userheader",obj.get("user_big_header").toString());
                editor.putString("nickname",obj.get("user_nickname").toString());
                editor.commit();
            }
        };
        System.out.println(url + "?id=" + id);
        new Thread(new MThread(handler, url + "?id=" + id)).start();


        final UserEntity user=new UserEntity();
        user.setName(name);
        user.setPassword(password);
        UserEntity userEntity= DataSupport.where("username=?",name).where("password=?",password).findFirst(UserEntity.class);
         if(userEntity==null){

             JMessageClient.login(name, password, new BasicCallback() {
                 @Override
                 public void gotResult(int i, String s) {
                     if(i==0){
                         user.save();

                        SharedPreferences.Editor editor=  sharedPreferences.edit();
                         editor.putString("username",name);

                         editor.putBoolean("isLogin",true);
                         editor.commit();
                         Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                         LoginActivity.this.finish();
                     }else {
                         Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_LONG).show();
                     }
                 }
             });

         }else{


         }


    }
    @JavascriptInterface
    public void register(final String username, final String password){
        System.out.println(username);
        JMessageClient.register(username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(i==0){
                    UserEntity user=new UserEntity();
                    user.setName(username);
                    user.setPassword(password);
                    if(user.save()){
                        Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,"注册失败，回撤注册信息",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
