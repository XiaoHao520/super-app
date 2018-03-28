package com.school.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.school.R;

public class ApplyActivity extends AppCompatActivity {
     WebView apply;
     String url="http://www.sinbel.top/ui/web/user/apply.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        initView();
    }

    private void initView() {
        apply= (WebView) findViewById(R.id.apply);
        WebSettings setting=apply.getSettings();
        setting.setJavaScriptEnabled(true);
        apply.addJavascriptInterface(this,"android");
        apply.loadUrl(url);
    }
    @JavascriptInterface
    public void back(){
         this.finish();
    }
}
