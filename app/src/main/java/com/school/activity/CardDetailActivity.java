package com.school.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Xml;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.school.R;

import java.util.HashMap;
import java.util.Map;

public class CardDetailActivity extends AppCompatActivity {

    WebView cardDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        cardDetail = (WebView) findViewById(R.id.cardDetail);
        cardDetail.addJavascriptInterface(this, "android");
        WebSettings setting = cardDetail.getSettings();
        setting.setJavaScriptEnabled(true);
        cardDetail.loadUrl("http://www.sinbel.top/ui/web/card/detail.html?id=" + intent.getStringExtra("id"));
        cardDetail.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                new AlertDialog.Builder(CardDetailActivity.this).
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
    public void back(){

          this.finish();

    }


}
