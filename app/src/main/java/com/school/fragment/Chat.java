package com.school.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.school.R;
import com.school.activity.ConversationActivity;
import com.school.adapter.ConversationListAdapter;
import com.school.entity.ConversationRecording;
import com.school.tools.Time;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by XIAOHAO-PC on 2017-11-04.
 */

public class Chat extends Fragment implements AdapterView.OnItemClickListener {
    View view;
    private static ListView conversationList;
    private static List<Map<String, String>> data;
    private static final int CONVERSATION = 404;
    private static ConversationListAdapter adapter;
    private static int dataSize = 0;
    private static SharedPreferences shared;
    private static String localUser;
    private static boolean isLogin;
    private static final String TAG = "F3";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chat, container, false);
        shared = getActivity().getSharedPreferences("localUser", Context.MODE_PRIVATE);

        localUser = shared.getString("username", null);
        isLogin = shared.getBoolean("isLogin", false);

        System.out.println("**********************localuser***********************");
        System.out.println(localUser);
        initView();
        if (isLogin) {
            initData(localUser);
        }
        return view;
    }

    public static void initData(String localUser) {
        Log.i(TAG, "localuser: "+localUser);
        ConversationRecording recording = new ConversationRecording();
        if (localUser != null) {
            List<ConversationRecording> conversationList = recording.where("localUser=?", localUser).find(ConversationRecording.class);
            if (conversationList != null) {
                for (ConversationRecording record : conversationList) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("from", record.getChatUser());
                    map.put("content", record.getLast());
                    map.put("date", record.getDate());
                    map.put("header", record.getHeader());
                    map.put("nickname", record.getNickname());
                    map.put("username", record.getChatUser());
                    map.put("status", record.getStatus());
                    updateConversationList(map);
                }

            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {

        Log.i(TAG, "onStart: ");


        super.onStart();
    }

    private void initView() {
        conversationList = (ListView) view.findViewById(R.id.conversationList);

        if (data == null) {
            data = new ArrayList<Map<String, String>>();
        }

        adapter = new ConversationListAdapter(data, getActivity());
        conversationList.setAdapter(adapter);
        conversationList.setOnItemClickListener(this);


    }


    public static void show() {
        System.out.println("广播发送");
    }

    public static void updateConversationList(Map<String, String> map) {
        System.out.println("消息进来");


        String username = map.get("username");
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> temp = data.get(i);
            if (username.equals(temp.get("username").toString())) {
                data.remove(i);
                data.add(0, map);
                adapter.notifyDataSetChanged();
                return;
            }
        }
        data.add(0, map);
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getActivity(), ConversationActivity.class);
        intent.putExtra("data", (Serializable) data.get(position));
        startActivityForResult(intent, CONVERSATION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CONVERSATION) {

            System.out.println(requestCode);

            System.out.println(resultCode);
            if (resultCode == 929) {
                Map<String, String> map = (Map<String, String>) data.getSerializableExtra("data");
                this.data.set(0, map);
                adapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void itemTotop(Map<String, String> map) {


    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        adapter.notifyDataSetChanged();

        super.onResume();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }
    public static void dataClear(){

        data.clear();
        adapter.notifyDataSetChanged();
    }
}
