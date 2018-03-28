package com.school.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.school.MainActivity;
import com.school.R;
import com.school.activity.StoreDetail;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by XIAOHAO-PC on 2018-02-10.
 */




public class Home extends Fragment{
    LinearLayout home;
    WebView homeWeb;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        home=initLayout(inflater,container,savedInstanceState);
        System.out.println("oncreate");
        return home;
    }

    private LinearLayout initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        home= (LinearLayout) inflater.inflate(R.layout.home,container,false);
        return home;
    }

    @Override
    public void onStart() {
        homeWeb=home.findViewById(R.id.homeWeb);
        WebSettings settings=homeWeb.getSettings();
        settings.setJavaScriptEnabled(true);
      //  homeWeb.loadUrl("file:///android_asset/web/home.html");

        homeWeb.loadUrl("http://www.sinbel.top/ui/web/home.html");
        homeWeb.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                homeWeb.loadUrl(request.getUrl().getPath());
                return true;
            }
        });
        homeWeb.addJavascriptInterface(this,"android");
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        homeWeb.onPause();
    }
    @JavascriptInterface
    public  void show(String id){
        System.out.println(id);
        Intent intent =new Intent(getActivity(), StoreDetail.class);
        intent.putExtra("storeID",id);
        startActivity(intent);

    }


}
