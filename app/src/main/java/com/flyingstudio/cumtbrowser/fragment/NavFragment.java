package com.flyingstudio.cumtbrowser.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyingstudio.cumtbrowser.R;
import com.flyingstudio.cumtbrowser.adapter.ButtonAdapter;
import com.flyingstudio.cumtbrowser.bar.MyButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MEzzsy on 2018/4/1.
 */

public class NavFragment extends Fragment {

    private List<MyButton> myButtons=new ArrayList<>();;

    public NavFragment() {
        initButtons();
    }

    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_nav, container, false);
        //初始化RecyclerView
        RecyclerView recyclerView = (RecyclerView)
                view.findViewById(R.id.navigation_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        ButtonAdapter adapter=new ButtonAdapter(myButtons,getContext());
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initButtons(){
        MyButton button_baidu=new MyButton("百度",R.drawable.baidu);
        myButtons.add(button_baidu);
        MyButton button_taoba=new MyButton("淘宝",R.drawable.taobao);
        myButtons.add(button_taoba);
        MyButton button_cumt=new MyButton("矿大",R.drawable.cumt);
        myButtons.add(button_cumt);
        MyButton button_atcumt=new MyButton("学生在线",R.drawable.atcumt);
        myButtons.add(button_atcumt);
    }
}
