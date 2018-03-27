package com.flyingstudio.cumtbrowser.bar;

import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.flyingstudio.cumtbrowser.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by MEzzsy on 2018/3/24.
 * 顶部搜索条
 */

public class SearchBar extends LinearLayout implements View.OnClickListener{
    private Button btn_delete_input;
    private Button btn_cancel;
    private Button btn_baidu;
    private Button btn_access;
    private EditText edit_serach;
    private String input="";
    private onClickSearchBarListener listener;

    public String getInput() {
        return input;
    }

    public SearchBar(Context context) {
        this(context,null);
    }

    public SearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.search_bar,this);
        initView();

        edit_serach.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                listener.record();
                if (hasFocus){
                    btn_delete_input.setVisibility(VISIBLE);
                    Log.d(TAG, "onFocusChange: 获取焦点");
                }else {
                    btn_delete_input.setVisibility(GONE);
                    Log.d(TAG, "onFocusChange: 没获取焦点");
                }
            }
        });

        

        edit_serach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: 输入开始");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "beforeTextChanged: 输入中");
            }

            @Override
            public void afterTextChanged(Editable s) {
                input=edit_serach.getText().toString();
                if (Patterns.WEB_URL.matcher(input).matches()){
                    btn_cancel.setVisibility(GONE);
                    btn_access.setVisibility(VISIBLE);
                    btn_baidu.setVisibility(GONE);
                }else {
                    btn_cancel.setVisibility(GONE);
                    btn_access.setVisibility(GONE);
                    btn_baidu.setVisibility(VISIBLE);
                }
                if (input.isEmpty()){
                    btn_baidu.setVisibility(GONE);
                    btn_cancel.setVisibility(VISIBLE);
                }

                Log.d(TAG, "beforeTextChanged: 输入结束");
            }
        });
    }

    private void initView() {
        btn_delete_input=(Button)findViewById(R.id.btn_delete_input);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        btn_baidu=(Button)findViewById(R.id.btn_baidu);
        btn_access=(Button)findViewById(R.id.btn_access);
        edit_serach=(EditText)findViewById(R.id.edit_serach);
        edit_serach.clearFocus();
        btn_delete_input.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_baidu.setOnClickListener(this);
        btn_access.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_delete_input:
                edit_serach.setText("");
                break;
            case R.id.btn_cancel:
//                Toast.makeText(getContext(),"取消",Toast.LENGTH_SHORT).show();
                listener.cancel();
                edit_serach.setFocusable(true);
                edit_serach.setFocusableInTouchMode(true);
                edit_serach.requestFocus();
                edit_serach.findFocus();
                break;
            case R.id.btn_baidu:
                input=edit_serach.getText().toString();
                listener.baidu(input);
                edit_serach.setText("");
//                Toast.makeText(getContext(),"百度一下",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_access:
                input=edit_serach.getText().toString();
                listener.access(input);
                edit_serach.setText("");
                break;
            default:
                break;
        }
    }

    public void setOnClickSearchBarListener(onClickSearchBarListener listener){
        this.listener=listener;
    }

    public interface onClickSearchBarListener{
        void cancel();//点击取消的逻辑实现
        void baidu(String input);//点击百度一下的逻辑实现
        void record();//历史记录的逻辑实现
        void access(String input);
    }
}
