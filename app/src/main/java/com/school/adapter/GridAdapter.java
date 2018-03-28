package com.school.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.school.R;
import com.school.tools.LruImageCache;
import com.school.tools.MImageLoader;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Created by xiaohao on 17-10-24.
 */

public class GridAdapter extends BaseAdapter {

    private List<PhotoInfo> photoInfoList;

    ImageView iv;
    private Context context;
    Activity activity;

    public GridAdapter(List<PhotoInfo> photoInfoList, Context context, Activity activity) {
        this.photoInfoList = photoInfoList;
        this.context = context;
        this.activity=activity;
        System.out.println(photoInfoList.size());
    }

    @Override
    public int getCount() {
        return photoInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {



       GFImageView iv = (GFImageView) LayoutInflater.from(activity).inflate(R.layout.grid_item, null).findViewById(R.id.iv);
      /*  Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(photoInfoList.get(i).getPhotoPath()), 200, 200, false);
        iv = (ImageView) LayoutInflater.from(context).inflate(R.layout.grid_item, null).findViewById(R.id.iv);
        iv.setImageBitmap(bitmap);*/

        MImageLoader loader=new MImageLoader();

        if(photoInfoList.get(i).getPhotoPath().equals("add")){
            iv.setImageResource(R.drawable.add);

        }else {
            loader.displayImage(activity, photoInfoList.get(i).getPhotoPath(), iv, null, 200, 200);
        }
        return iv;
    }
}
