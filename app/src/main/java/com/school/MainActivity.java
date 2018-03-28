package com.school;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.school.activity.LoginActivity;
import com.school.adapter.FragmentAdapter;
import com.school.entity.UserEntity;
import com.school.fragment.Blog;
import com.school.fragment.Chat;
import com.school.fragment.Home;
import com.school.fragment.Mine;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    Mine mine;
    Home home;
    Blog blog;
    Chat chat;
    ViewPager pages;
    List<Fragment> fragments;
    FragmentAdapter fragmentAdapter;
    LinearLayout f1, f2, f3, f4;
    ImageView home_btn_image, blog_btn_image, chat_btn_image, mine_btn_image;
    TextView home_text, blog_text, chat_text, mine_text;

    Context context;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
         SQLiteDatabase db = Connector.getDatabase();
        context = getApplicationContext();
        JMessageClient.init(context);

        JMessageClient.setDebugMode(true);
        JMessageClient.registerEventReceiver(this);

        UserEntity user = DataSupport.find(UserEntity.class, 1);
        if (user != null) {

            System.out.println("*****************************");
            System.out.println(user.toString());
            JMessageClient.login(user.getName(), user.getPassword(), new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0) {
                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "未登录", Toast.LENGTH_LONG).show();
        }
        System.out.println(user);


        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        f1 = (LinearLayout) findViewById(R.id.f1);
        f2 = (LinearLayout) findViewById(R.id.f2);
        f3 = (LinearLayout) findViewById(R.id.f3);
        f4 = (LinearLayout) findViewById(R.id.f4);
        home_btn_image = (ImageView) findViewById(R.id.home_btn_img);
        blog_btn_image = (ImageView) findViewById(R.id.blog_btn_img);
        chat_btn_image = (ImageView) findViewById(R.id.chat_btn_img);
        mine_btn_image = (ImageView) findViewById(R.id.mine_btn_img);
        home_text = (TextView) findViewById(R.id.home_text);
        blog_text = (TextView) findViewById(R.id.blog_text);
        chat_text = (TextView) findViewById(R.id.chat_text);
        mine_text = (TextView) findViewById(R.id.mine_text);
        mine = new Mine();
        home = new Home();
        blog = new Blog();
        chat = new Chat();
        fragments = new ArrayList<Fragment>();
        fragments.add(home);
        fragments.add(blog);
        fragments.add(chat);
        fragments.add(mine);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        pages = (ViewPager) this.findViewById(R.id.viewPager);
        pages.setAdapter(fragmentAdapter);
        pages.setOnPageChangeListener(this);
        navbarEvents();
    }

    private void navbarEvents() {
        f1.setOnClickListener(this);
        f2.setOnClickListener(this);
        f3.setOnClickListener(this);
        f4.setOnClickListener(this);

    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.f1: {
                pages.setCurrentItem(0);
                home_btn_image.setImageResource(R.drawable.mapactived);
                home_text.setTextColor(context.getColor(R.color.colorAccent));
                blog_btn_image.setImageResource(R.drawable.note);
                blog_text.setTextColor(context.getColor(R.color.navDefault));
                chat_btn_image.setImageResource(R.drawable.chat);
                chat_text.setTextColor(context.getColor(R.color.navDefault));
                mine_btn_image.setImageResource(R.drawable.me);
                mine_text.setTextColor(context.getColor(R.color.navDefault));


                break;
            }
            case R.id.f2: {
                pages.setCurrentItem(1);
                blog_btn_image.setImageResource(R.drawable.noteactived);
                blog_text.setTextColor(context.getColor(R.color.colorAccent));
                home_btn_image.setImageResource(R.drawable.map);
                home_text.setTextColor(context.getColor(R.color.navDefault));
                chat_btn_image.setImageResource(R.drawable.chat);
                chat_text.setTextColor(context.getColor(R.color.navDefault));
                mine_btn_image.setImageResource(R.drawable.me);
                mine_text.setTextColor(context.getColor(R.color.navDefault));
                break;
            }
            case R.id.f3: {
                pages.setCurrentItem(2);
                chat_btn_image.setImageResource(R.drawable.chatactived);
                chat_text.setTextColor(context.getColor(R.color.colorAccent));
                home_btn_image.setImageResource(R.drawable.map);
                home_text.setTextColor(context.getColor(R.color.navDefault));
                blog_btn_image.setImageResource(R.drawable.note);
                blog_text.setTextColor(context.getColor(R.color.navDefault));
                mine_btn_image.setImageResource(R.drawable.me);
                mine_text.setTextColor(context.getColor(R.color.navDefault));

                break;
            }
            case R.id.f4: {
                pages.setCurrentItem(3);
                mine_btn_image.setImageResource(R.drawable.meactived);
                mine_text.setTextColor(context.getColor(R.color.colorAccent));
                chat_btn_image.setImageResource(R.drawable.chat);
                chat_text.setTextColor(context.getColor(R.color.navDefault));
                home_btn_image.setImageResource(R.drawable.map);
                home_text.setTextColor(context.getColor(R.color.navDefault));
                blog_btn_image.setImageResource(R.drawable.note);
                blog_text.setTextColor(context.getColor(R.color.navDefault));
                break;
            }
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0: {
                home_btn_image.setImageResource(R.drawable.mapactived);
                home_text.setTextColor(context.getColor(R.color.colorAccent));
                blog_btn_image.setImageResource(R.drawable.note);
                blog_text.setTextColor(context.getColor(R.color.navDefault));
                chat_btn_image.setImageResource(R.drawable.chat);
                chat_text.setTextColor(context.getColor(R.color.navDefault));
                mine_btn_image.setImageResource(R.drawable.me);
                mine_text.setTextColor(context.getColor(R.color.navDefault));
                break;
            }
            case 1: {
                blog_btn_image.setImageResource(R.drawable.noteactived);
                blog_text.setTextColor(context.getColor(R.color.colorAccent));
                home_btn_image.setImageResource(R.drawable.map);
                home_text.setTextColor(context.getColor(R.color.navDefault));
                chat_btn_image.setImageResource(R.drawable.chat);
                chat_text.setTextColor(context.getColor(R.color.navDefault));
                mine_btn_image.setImageResource(R.drawable.me);
                mine_text.setTextColor(context.getColor(R.color.navDefault));
                break;
            }
            case 2: {
                chat_btn_image.setImageResource(R.drawable.chatactived);
                chat_text.setTextColor(context.getColor(R.color.colorAccent));
                home_btn_image.setImageResource(R.drawable.map);
                home_text.setTextColor(context.getColor(R.color.navDefault));
                blog_btn_image.setImageResource(R.drawable.note);
                blog_text.setTextColor(context.getColor(R.color.navDefault));
                mine_btn_image.setImageResource(R.drawable.me);
                mine_text.setTextColor(context.getColor(R.color.navDefault));
                break;
            }
            case 3: {
                mine_btn_image.setImageResource(R.drawable.meactived);
                mine_text.setTextColor(context.getColor(R.color.colorAccent));
                chat_btn_image.setImageResource(R.drawable.chat);
                chat_text.setTextColor(context.getColor(R.color.navDefault));
                home_btn_image.setImageResource(R.drawable.map);
                home_text.setTextColor(context.getColor(R.color.navDefault));
                blog_btn_image.setImageResource(R.drawable.note);
                blog_text.setTextColor(context.getColor(R.color.navDefault));
                break;
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void onEvent(MessageEvent event) {
        Message message = event.getMessage();
        if (message != null) {
            System.out.println(message.toString());
            switch (message.getContentType()) {

                case custom: {

                    CustomContent content = (CustomContent) message.getContent();

                    if ("text".equals(content.getStringValue("type"))) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("from", message.getFromUser().getUserName());
                        map.put("content", content.getStringValue("content"));
                        map.put("date", content.getStringValue("date"));
                        map.put("header", content.getStringValue("header"));
                        map.put("nickname", content.getStringValue("nickname"));
                        map.put("username", message.getFromUser().getUserName());
                        map.put("status", "u");
                        Chat.updateConversationList(map);
                    }

                    if ("conversation".equals(content.getStringValue("type"))) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("from", content.getStringValue("from"));
                        map.put("content", content.getStringValue("content"));
                        map.put("date", content.getStringValue("date"));
                        map.put("header", content.getStringValue("header"));
                        map.put("nickname", content.getStringValue("nickname"));
                        map.put("username", content.getStringValue("username"));

                        Chat.updateConversationList(map);


                    }
                    break;
                }

            }


        }

    }
}
