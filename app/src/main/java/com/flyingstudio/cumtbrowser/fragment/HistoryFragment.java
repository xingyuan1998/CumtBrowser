package com.flyingstudio.cumtbrowser.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.flyingstudio.cumtbrowser.R;
import com.flyingstudio.cumtbrowser.adapter.HistoryAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by MEzzsy on 2018/3/26.
 */

@SuppressLint("ValidFragment")
public class HistoryFragment extends Fragment implements HistoryAdapter.JumpListener {
    private HistoryAdapter adapter;
    private List<String> stringList;
    private RecyclerView recyclerView;
    private Button button;
    private clearHistoryListener listener;

    @SuppressLint("ValidFragment")
    public HistoryFragment(List<String> stringList) {
        this.stringList = stringList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.serach_history, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.history_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HistoryAdapter(stringList);
        adapter.setJumpListener(this);
        button = (Button) view.findViewById(R.id.btn_clear_history);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clear();
                stringList.clear();
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void dataChanged() {
        adapter.notifyDataSetChanged();
    }

    public void setClearHistoryListener(clearHistoryListener listener) {
        this.listener = listener;
    }

    @Override
    public void jump(String content) {
        listener.jumpAgain(content);
    }

    public interface clearHistoryListener {
        void clear();
        void jumpAgain(String content);
    }

    @Override
    public void onDestroy() {
        // 保存历史记录
        SharedPreferences.Editor editor = getContext().getSharedPreferences("history",
                Context.MODE_PRIVATE).edit();
        editor.putInt("size", stringList.size());
        for (int i = 0; i < stringList.size(); i++) {
            editor.putString(i + "", stringList.get(i));
        }
        editor.apply();

        super.onDestroy();
    }

    public HistoryAdapter getAdapter() {
        return adapter;
    }
}
