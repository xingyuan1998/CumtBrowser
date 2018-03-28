package com.flyingstudio.cumtbrowser.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.flyingstudio.cumtbrowser.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by MEzzsy on 2018/3/26.
 */

@SuppressLint("ValidFragment")
public class HistoryFragment extends Fragment {
    private ArrayAdapter<String> adapter;
    private List<String> stringList;
    private ListView listView;
    private Button button;
    private clearHistoryListener listener;

    @SuppressLint("ValidFragment")
    public HistoryFragment(List<String> stringList) {
        this.stringList=stringList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.serach_history,container,false);
        adapter=new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,stringList);
        button=(Button)view.findViewById(R.id.btn_clear_history);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clear();
                stringList.clear();
                adapter.notifyDataSetChanged();
            }
        });
        listView=(ListView)view.findViewById(R.id.history_view);
        listView.setAdapter(adapter);
//        listView.addFooterView(button);
        return view;
    }

    public void dataChanged(){
        adapter.notifyDataSetChanged();
    }

    public void setClearHistoryListener(clearHistoryListener listener){
        this.listener=listener;
    }

    public interface clearHistoryListener{
        void clear();
    }

    @Override
    public void onDestroy() {
        // 保存历史记录
        SharedPreferences.Editor editor=getContext().getSharedPreferences("history",
                Context.MODE_PRIVATE).edit();
        editor.putInt("size",stringList.size());
        for (int i=0;i<stringList.size();i++){
            editor.putString(i+"",stringList.get(i));
        }
        editor.apply();

        super.onDestroy();
    }
}
