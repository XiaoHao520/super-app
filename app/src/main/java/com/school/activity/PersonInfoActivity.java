package com.school.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.school.R;

public class PersonInfoActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        System.out.println("个人信息");
        initView();

    }

    private void initView() {
        webView = (WebView) findViewById(R.id.info_web);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "android");
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        webView.loadUrl("http://www.sinbel.top/ui/web/user/mine.html?id=" + id);
    }
    @JavascriptInterface
    public void back(){
        this.finish();
    }
}
