package com.school.fragment;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.school.MainActivity;
import com.school.R;
import com.school.activity.StoreDetail;

import java.util.Date;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by XIAOHAO-PC on 2018-02-10.
 */


public class Home extends Fragment implements AMapLocationListener {
    LinearLayout home;
    WebView homeWeb;
    public AMapLocationClient mlocationClient = null;
    public AMapLocationClientOption mLocationOption = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        home = initLayout(inflater, container, savedInstanceState);
        System.out.println("oncreate");
        return home;
    }

    private LinearLayout initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        home = (LinearLayout) inflater.inflate(R.layout.home, container, false);
        return home;
    }

    @Override
    public void onStart() {
        homeWeb = home.findViewById(R.id.homeWeb);
        WebSettings settings = homeWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        homeWeb.loadUrl("http://www.sinbel.top/ui/web/home.html");

        homeWeb.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                homeWeb.loadUrl(request.getUrl().getPath());
                return true;
            }
        });
        homeWeb.addJavascriptInterface(this, "android");

        System.out.println("网页加载完成");
        mLocationOption = new AMapLocationClientOption();
        mlocationClient=new AMapLocationClient(getActivity());
        mlocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
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
    public void show(String id) {
        System.out.println(id);
        Intent intent = new Intent(getActivity(), StoreDetail.class);
        System.out.println(id);
        intent.putExtra("storeID", id);
        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间


                try {
                    Thread.sleep(2000);
                    homeWeb.loadUrl("javascript:setLng('"+aMapLocation.getLatitude()+"','"+aMapLocation.getLongitude()+"')");
                 /*   homeWeb.loadUrl("javascript:init()");*/
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                System.out.println(aMapLocation.toString());
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
}
