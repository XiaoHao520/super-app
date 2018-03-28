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
import android.widget.Toast;

import com.school.R;
import com.school.activity.CardDetailActivity;
import com.school.activity.CardsActivity;
import com.school.entity.UserEntity;
import com.school.tools.Ok;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIAOHAO-PC on 2018-02-10.
 */

public class Blog extends Fragment {
    LinearLayout blog;
    WebView blogWeb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        blog = initLayout(inflater, container, savedInstanceState);

        return blog;
    }

    private LinearLayout initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UserEntity user = DataSupport.findLast(UserEntity.class);

        blog = (LinearLayout) inflater.inflate(R.layout.blog, container, false);
        blogWeb = blog.findViewById(R.id.blogWeb);
        WebSettings settings = blogWeb.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        blogWeb.addJavascriptInterface(this, "android");
        if (user != null) {
            blogWeb.loadUrl("http://www.sinbel.top/ui/web/card/index.html?school=" + user.getSchool());
        } else {
            blogWeb.loadUrl("http://www.sinbel.top/ui/web/card/index.html");
        }
        return blog;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @JavascriptInterface
    public void detail(String val) {
        Intent intent = new Intent(getActivity(), CardDetailActivity.class);
        intent.putExtra("id", val);
        startActivity(intent);
    }

    @JavascriptInterface
    public void add() {
        UserEntity userEntity = DataSupport.findLast(UserEntity.class);
        if (userEntity == null) {
            Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(getActivity(), CardsActivity.class);
        startActivityForResult(intent, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            final UserEntity userEntity = DataSupport.findLast(UserEntity.class);
          if(userEntity==null) {
              System.out.println("空的");
              return;
          }

            final ArrayList<String> photos = data.getStringArrayListExtra("photos");
            final String content = data.getStringExtra("content");
            if (photos.size() > 0 && photos.size() < 9) {
                photos.remove(photos.size()-1);
            }
            System.out.println("照片的长度：：：：：：：：：：：：：：：：" + photos.size());
            if (photos.size() != 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Ok.uploadCards(photos, content, userEntity.getName(), "http://super.sinbel.top/card/send");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }

    }
}
