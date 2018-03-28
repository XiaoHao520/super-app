package com.school.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.school.R;
import com.school.tools.Time;

import java.util.List;
import java.util.Map;

/**
 * Created by XIAOHAO-PC on 2017-11-04.
 */

public class ConversationListAdapter extends BaseAdapter {


    List<Map<String, String>> data;
    Activity activity;

    public ConversationListAdapter(List<Map<String, String>> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Map<String, String> map = data.get(position);
        LinearLayout conversation = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.conversation_item, null);

        final ImageView header = (ImageView) conversation.findViewById(R.id.header);
        TextView username = (TextView) conversation.findViewById(R.id.username);
        TextView last = (TextView) conversation.findViewById(R.id.last);
        TextView date = (TextView) conversation.findViewById(R.id.date);
        username.setText(map.get("nickname"));
        last.setText(map.get("content"));
        date.setText(Time.getTime(map.get("date")));

        Glide.with(activity).load(map.get("header")).placeholder(R.drawable.user).error(R.drawable.user).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new ImageViewTarget<GlideDrawable>(header) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        header.setImageDrawable(resource);
                    }
                });
        return conversation;
    }
}
