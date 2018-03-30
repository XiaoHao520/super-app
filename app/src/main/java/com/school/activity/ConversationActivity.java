package com.school.activity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.ProviderInfo;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.school.R;
import com.school.adapter.ConversationAdapter;
import com.school.entity.ConversationRecording;
import com.school.entity.UserEntity;
import com.school.fragment.Blog;
import com.school.tools.Ok;
import com.school.tools.Time;


import org.json.JSONArray;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;


public class ConversationActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView conversationList;
    private static List<Map<String, String>> data;
    static ConversationAdapter adapter;
    private TextView back;
    private TextView nickname;
    private ImageView header;
    private ImageView send;
    private EditText input;
    Map<String, String> from;
    Intent intent;
    private SharedPreferences sharedPreferences;
    private static final int CONVERSATION = 929;
    private static String returnNickname;
    private static String returnUsername;
    private static String userHeader;
    List<Message> msgList;
    private static String localUser;
    private static boolean isMe = false;
    private static String msgTime;
    private UserEntity user;
    private String url = "http://super.sinbel.top/user/info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        JMessageClient.registerEventReceiver(this);
        sharedPreferences = getSharedPreferences("localUser", MODE_PRIVATE);

        // localUser = sharedPreferences.getString("username", null);
        user = DataSupport.findLast(UserEntity.class);
        if (user != null) {
            localUser = user.getName();
        }
        initView();
        try {
            initData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //enterConversation();
    }

    private void initView() {
        conversationList = (ListView) findViewById(R.id.conversationList);
        header = (ImageView) findViewById(R.id.userHeader);
        back = (TextView) findViewById(R.id.back);
        nickname = (TextView) findViewById(R.id.nickname);
        send = (ImageView) findViewById(R.id.send);
        input = (EditText) findViewById(R.id.input);
        send.setOnClickListener(this);
    }

    class MThread implements Runnable {

        Handler handler;
        String url;


        public MThread(Handler handler, String url) {

            this.handler = handler;
            this.url = url;
        }

        @Override
        public void run() {

            android.os.Message msg = new android.os.Message();
            String res = null;
            try {
                res = Ok.getUserInfo(this.url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject obj = JSON.parseObject(res);
            if (obj != null) {

                msg.obj = obj;
            }
            handler.sendMessage(msg);
        }
    }

    private void initData() throws IOException {

        data = new ArrayList<Map<String, String>>();
        intent = getIntent();
        from = (Map<String, String>) intent.getSerializableExtra("data");

        System.out.println("*******************************");
        System.out.println(from.toString());
        if (from.get("nickname") == null) {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(android.os.Message msg) {
                    super.handleMessage(msg);
                    JSONObject obj = (JSONObject) msg.obj;
                    from.put("header", obj.get("user_big_header").toString());
                    from.put("username", obj.get("username").toString());
                    from.put("nickname", obj.get("user_nickname").toString());
                    adapter = new ConversationAdapter(ConversationActivity.this, data);
                    conversationList.setAdapter(adapter);
                    userHeader = from.get("header");
                    returnNickname = from.get("nickname");
                    returnUsername = from.get("username");
                    nickname.setText(from.get("nickname"));
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putString("userheader",userHeader);
                    editor.putString("nickname",returnNickname);
                    editor.commit();



                    Glide.with(ConversationActivity.this).load(from.get("header")).placeholder(R.drawable.user).error(R.drawable.user).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(new ImageViewTarget<GlideDrawable>(header) {
                                @Override
                                protected void setResource(GlideDrawable resource) {
                                    header.setImageDrawable(resource);
                                }
                            });
                }
            };
            new Thread(new MThread(handler, url + "?id=" + from.get("id"))).start();
        } else {

            adapter = new ConversationAdapter(this, data);
            conversationList.setAdapter(adapter);
            userHeader = from.get("header");
            returnNickname = from.get("nickname");
            returnUsername = from.get("username");
            nickname.setText(from.get("nickname"));
            Glide.with(this).load(from.get("header")).placeholder(R.drawable.user).error(R.drawable.user).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new ImageViewTarget<GlideDrawable>(header) {
                        @Override
                        protected void setResource(GlideDrawable resource) {
                            header.setImageDrawable(resource);
                        }
                    });

        }

    }

    @Override
    public void onClick(View v) {
        if (v == send) {
            msgTime = Time.getNow();
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "m");
            String content = input.getText().toString();
            if (!content.equals("")) {
                map.put("content", content);
            }
            data.add(map);
            map.put("type", "text");
            map.put("content", input.getText().toString());
            map.put("date", msgTime);

            map.put("header", sharedPreferences.getString("userheader", null));
            map.put("nickname", sharedPreferences.getString("nickname", null));

            System.out.println(from.get("username"));

            System.out.println(map.toString());
            Message message = JMessageClient.createSingleCustomMessage(from.get("username"), map);
            JMessageClient.sendMessage(message);
            input.setText("");
            adapter.notifyDataSetChanged();
        }
    }


    private void enterConversation() {
        String username = from.get("username");
        if (username == null) {
            System.out.println("user is null");
        } else {


            if (username.equals(localUser)) {

                System.out.println("can not talk to self");
                isMe = true;
                return;

            }
            JMessageClient.enterSingleConversation(username);
            int start = 0;
            Conversation conversation = JMessageClient.getSingleConversation(username);

            try {
                msgList = conversation.getAllMessage();
            } catch (Exception ex) {


            }
            if (msgList == null) {
                data.add(from);
                adapter.notifyDataSetChanged();
                return;
            }
            if (msgList.size() == 0) {
                data.add(from);
                adapter.notifyDataSetChanged();
                return;
            }
            if (msgList.size() > 10) {
                start = msgList.size() - 10;
            }
            msgList = conversation.getMessagesFromOldest(start, 10);


            for (Message message : msgList) {
                ContentType type = message.getContentType();
                switch (type) {//现只有custom；
                    case custom: {

                        CustomContent content = (CustomContent) message.getContent();
                        if ("text".equals(content.getStringValue("type"))) {
                            Map<String, String> map = new HashMap<String, String>();
                            if (message.getFromUser().getUserName().equals(localUser)) {
                                map.put("status", "m");
                            } else {
                                map.put("status", "u");
                            }
                            msgTime = content.getStringValue("date");
                            map.put("from", message.getFromUser().getUserName());
                            map.put("content", content.getStringValue("content"));
                            map.put("date", msgTime);
                            map.put("header", content.getStringValue("header"));
                            map.put("nickname", content.getStringValue("nickname"));
                            map.put("username", content.getStringValue("username"));
                            data.add(map);
                        }
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    protected void onDestroy() {

        System.out.println("f3 destory");
        super.onDestroy();
        if (!isMe) {
            back();
        }


    }


    @Override
    public void onBackPressed() {
        if (isMe) {
            this.finish();
        } else {
            back();
        }


    }

    private void back() {
        JMessageClient.exitConversation();
        JMessageClient.unRegisterEventReceiver(this);
        Map<String, String> map = data.get(data.size() - 1);
        map.put("username", returnUsername);
        map.put("nickname", returnNickname);
        map.put("date", Time.getNow());
        intent.putExtra("data", (Serializable) map);
        ConversationRecording recording = new ConversationRecording();
        localUser = sharedPreferences.getString("username", null).toString();
        List<ConversationRecording> current = recording.where("localUser=? and chatUser=?",
                localUser, returnUsername).find(ConversationRecording.class);
        if (current != null) {
            recording.deleteAll(ConversationRecording.class, "localUser=? and chatUser=?", localUser, returnUsername);
        }
        recording.setLocalUser(localUser);
        recording.setChatUser(returnUsername);
        recording.setNickname(returnNickname);
        recording.setDate(msgTime);
        recording.setHeader(userHeader);
        recording.setStatus(map.get("status"));
        recording.setLast(map.get("content"));
        if (recording.save()) {
            Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
            System.out.println("保存成功");
        } else {
            Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_LONG).show();
            System.out.println("保存失败");
        }
        this.setResult(CONVERSATION, intent);
        this.finish();
    }


    public void onEvent(MessageEvent event) {
        Message message = event.getMessage();
        ContentType type = message.getContentType();
        switch (type) {
            case text: {
                TextContent content = (TextContent) message.getContent();
                Map<String, String> map = new HashMap<String, String>();
                map.put("status", "u");
                map.put("content", content.getText());
                data.add(map);
                adapter.notifyDataSetChanged();
                break;
            }
            case custom: {
                CustomContent content = (CustomContent) message.getContent();
                if ("text".equals(content.getStringValue("type"))) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("status", "u");
                    map.put("from", message.getFromUser().getUserName());
                    map.put("content", content.getStringValue("content"));
                    map.put("date", content.getStringValue("date"));
                    map.put("header", content.getStringValue("header"));
                    map.put("nickname", content.getStringValue("nickname"));
                    map.put("username", content.getStringValue("username"));
                    System.out.println("收到text消息");
                    data.add(map);
                    adapter.notifyDataSetChanged();

                }
            }
            case image: {
                break;
            }
        }
    }
}
