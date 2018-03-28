package com.school.tools;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by xiaohao on 17-10-19.
 */

public class LruImageCache implements ImageLoader.ImageCache {

    private static LruCache<String,Bitmap> mMemoryCache;

    private  static LruImageCache lruImageCache;

    public LruImageCache() {
        int maxMemory= (int) Runtime.getRuntime().maxMemory();
        int cacheSize=maxMemory/8;
        mMemoryCache=new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }
    public static LruImageCache instance(){
        if(lruImageCache==null){
            lruImageCache=new LruImageCache();
        }
        return lruImageCache;
    }

    @Override
    public Bitmap getBitmap(String s) {
        return mMemoryCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {

        if(getBitmap(s)==null){
            mMemoryCache.put(s,bitmap);
        }
    }
}
