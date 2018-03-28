package com.school.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.school.R;
import com.school.activity.LoginActivity;
import com.school.activity.MyStoresActivity;
import com.school.activity.PersonInfoActivity;
import com.school.entity.UserEntity;

import org.litepal.crud.DataSupport;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by XIAOHAO-PC on 2018-02-10.
 */

public class Mine extends Fragment {
    LinearLayout mine;
    WebView webView;
    private String url = "http://www.sinbel.top/ui/web/user/index.html";
    private static UserEntity user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mine = initLayout(inflater, container, savedInstanceState);
        System.out.println("onCreateView");
        return mine;
    }


    private LinearLayout initLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout mine = (LinearLayout) inflater.inflate(R.layout.mine, container, false);

        return mine;
    }

    @Override
    public void onStart() {
        super.onStart();
        webView = mine.findViewById(R.id.mine_web);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //  webView.loadUrl("file:///android_asset/web/user/index.html");
        webView.loadUrl("http://www.sinbel.top/ui/web/user/index.html");
        webView.addJavascriptInterface(this, "android");

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @JavascriptInterface
    public void checkLogin() {
        user = DataSupport.findLast(UserEntity.class);
        //  System.out.println(user);
        if (user == null) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:login()");
                }
            });
            //  Intent intent = new Intent(getActivity(), LoginActivity.class);
            // startActivity(intent);

        } else {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    if (user.getPassword() == null) {
                        webView.loadUrl("javascript:login()");
                        return;
                    }
                    webView.loadUrl("javascript:login('" + user.getName() + "','" + user.getPassword() + "')");
                }
            });
        }
    }

    @JavascriptInterface
    public void login() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @JavascriptInterface
    public void myStores(String username) {
        Intent intent = new Intent(getActivity(), MyStoresActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @JavascriptInterface
    public void logout(String username) {
        DataSupport.deleteAll(UserEntity.class);
        JMessageClient.logout();
    }

    @JavascriptInterface
    public void info(String userId) {
        System.out.println(userId);
      Intent intent = new Intent(getActivity(), PersonInfoActivity.class);
        intent.putExtra("id",userId);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("*****************************************");
        if (user != null)
            webView.post(new Runnable() {
                @Override
                public void run() {
                    if (user.getPassword() == null) {
                        webView.loadUrl("javascript:login()");
                        return;
                    }
                    webView.loadUrl("javascript:login('" + user.getName() + "','" + user.getPassword() + "')");
                }
            });
    }

}
