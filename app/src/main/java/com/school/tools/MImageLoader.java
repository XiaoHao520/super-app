package com.school.tools;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;


import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Created by xiaohao on 17-10-28.
 */

public class MImageLoader implements ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, final GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Glide.with(activity).load("file://" + path).placeholder(defaultDrawable).error(defaultDrawable).diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new ImageViewTarget<GlideDrawable>(imageView) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        imageView.setImageDrawable(resource);
                    }
                });
    }

    @Override
    public void clearMemoryCache() {

    }
}
