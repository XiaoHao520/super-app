package com.school.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.school.R;
import com.school.entity.UserEntity;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class RegisterActivity extends AppCompatActivity {

    private WebView webView;
    private String url="http://www.sinbel.top/ui/web/user/register.html";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        webView= (WebView) findViewById(R.id.registerWeb);
        WebSettings setting=webView.getSettings();
        setting.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this,"android");
        webView.loadUrl(url);
    }
    @JavascriptInterface
    public void registerToJm(final String username, final String password){
        JMessageClient.register(username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(i==0){
                    UserEntity user=new UserEntity();
                    user.setName(username);
                    user.setPassword(password);
                    if(user.save()){
                        Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


}
