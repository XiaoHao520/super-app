package com.school.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.school.R;

public class MyStoresActivity extends AppCompatActivity {
    WebView myStore;
    String url = "http://www.sinbel.top/ui/web/user/mystore.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stores);
        initView();

    }

    private void initView() {
        Intent intent = getIntent();
        myStore = (WebView) findViewById(R.id.myStore);
        WebSettings setting = myStore.getSettings();
        setting.setJavaScriptEnabled(true);
        myStore.addJavascriptInterface(this, "android");
        System.out.println(url + "?username=" + intent.getStringExtra("username"));
        myStore.loadUrl(url + "?username=" + intent.getStringExtra("username"));
        myStore.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println(url);
                view.loadUrl(url);
                return true;
            }
        });
    }
    @JavascriptInterface
    public void back(){
        this.finish();
    }
     @JavascriptInterface
    public void loadUrl(String url){



     }

}
