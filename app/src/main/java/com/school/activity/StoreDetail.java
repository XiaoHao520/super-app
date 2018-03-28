package com.school.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.school.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.school.R.id.store_detail;

public class StoreDetail extends AppCompatActivity {
    WebView detailWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_store_detail);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        detailWeb = (WebView) findViewById(store_detail);
        detailWeb.loadUrl("http://www.sinbel.top/ui/web/store/detail.html?id=" + intent.getStringExtra("storeID"));
        detailWeb.addJavascriptInterface(this, "android");
        WebSettings setting = detailWeb.getSettings();
        setting.setJavaScriptEnabled(true);
    }

    @JavascriptInterface
    public void contact(String id) {


        Intent intent = new Intent(this, ConversationActivity.class);

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);

        intent.putExtra("data", (Serializable) map);
        startActivity(intent);
    }

    @JavascriptInterface
    public void back() {
        this.finish();
    }


}
