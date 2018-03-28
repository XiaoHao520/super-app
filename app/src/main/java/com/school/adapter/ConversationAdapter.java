package com.school.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.school.R;

import java.util.List;
import java.util.Map;

/**
 * Created by XIAOHAO-PC on 2017-11-05.
 */

public class ConversationAdapter extends BaseAdapter {
    Activity activity;
    List<Map<String,String>>data;

    public ConversationAdapter(Activity activity, List<Map<String, String>> data) {
        this.activity = activity;
        this.data = data;
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
        Map<String,String>map=data.get(position);
        RelativeLayout layout;
        if(map.get("status").equals("u")){
             layout= (RelativeLayout) LayoutInflater.from(activity).inflate(R.layout.conversation_u,null);
        }else {
            layout= (RelativeLayout) LayoutInflater.from(activity).inflate(R.layout.conversation_m,null);

        }
        TextView content= (TextView) layout.findViewById(R.id.content);
        content.setText(map.get("content"));
        return layout;
    }
}
