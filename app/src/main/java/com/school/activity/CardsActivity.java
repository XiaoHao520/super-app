package com.school.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;
import android.test.mock.MockApplication;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.school.R;
import com.school.adapter.GridAdapter;
import com.school.tools.MImageLoader;

import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class CardsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private GridView gridView;
    List<HashMap<String, Object>> imgList;
 private List<PhotoInfo> photoInfoList;
    private EditText content;
    private String imgPath = null;
   private MHandler mHandler;
    private SimpleAdapter simpleAdapter;
   GridAdapter adapter;
    private TextView send;
    private static final int MAKE_NOTE_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
         initView();
        photoInfoList = new ArrayList<PhotoInfo>();

        PhotoInfo add = new PhotoInfo();

         add.setPhotoPath("add");
         photoInfoList.add(add);
        adapter = new GridAdapter(photoInfoList, getApplicationContext(), CardsActivity.this);
         gridView.setAdapter(adapter);
         mHandler = new MHandler();
        System.out.println("***********here**************");
    }

    private void initView() {
       send = (TextView) findViewById(R.id.send);
        gridView = (GridView) this.findViewById(R.id.grid);
        content = (EditText) this.findViewById(R.id.content);
        gridView.setOnItemClickListener(this);
        send.setOnClickListener(this);
    }



    public void pickImages(int sum) {
        ThemeConfig theme = new ThemeConfig.Builder().build();
        FunctionConfig function = new FunctionConfig.Builder().setEnableCamera(true).setEnableEdit(false).setEnablePreview(true).build();
        MImageLoader imageLoader = new MImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(getApplicationContext(), imageLoader, theme).setDebug(BuildConfig.DEBUG).setFunctionConfig(function).build();
        GalleryFinal.init(coreConfig);
        GalleryFinal.openGalleryMuti(1, 10 - sum, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (reqeustCode == 1) {

                    // photoInfoList = resultList;
                    Message msg = new Message();
                    msg.obj = resultList;
                    mHandler.sendMessage(msg);
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });


    }

    @Override
    public void onClick(View view) {

        if (send == view) {
                  if(!photoInfoList.isEmpty()){
                      ArrayList<String>photos=new ArrayList<String>();
                      for (PhotoInfo photo:photoInfoList) {
                          photos.add(photo.getPhotoPath());
                      }
                      Intent intent=this.getIntent();
                      intent.putStringArrayListExtra("photos",photos);
                      intent.putExtra("content",content.getText().toString());
                      this.setResult(1,intent);
                      CardsActivity.this.finish();
                  }


        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        i = i + 1;
        System.out.println(view.getId());

        if (i == photoInfoList.size()) {
            if (i == 9) {
                photoInfoList.remove(8);
            }
            int sum = photoInfoList.size();
            if (sum == 9) {
                Toast.makeText(getApplicationContext(), "已达到最大数量", Toast.LENGTH_LONG).show();
                return;
            }
            pickImages(sum);
        }
    }
    class MHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            List<PhotoInfo> photos = (List<PhotoInfo>) msg.obj;

            if (photos.size() > 0) {
                for (PhotoInfo photo : photos) {
                    photoInfoList.add(photoInfoList.size() - 1, photo);
                }
                adapter.notifyDataSetChanged();
            }
            System.out.println(photoInfoList.size());
            if (photoInfoList.size() == 10) {
                photoInfoList.remove(9);
            }
            super.handleMessage(msg);
        }
    }
}
