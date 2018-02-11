package com.school.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.school.R;

/**
 * Created by XIAOHAO-PC on 2018-02-10.
 */

public class Blog extends Fragment {
    LinearLayout blog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        blog = initLayout(inflater, container, savedInstanceState);

        return blog;
    }

    private LinearLayout initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
           return (LinearLayout) inflater.inflate(R.layout.blog,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
