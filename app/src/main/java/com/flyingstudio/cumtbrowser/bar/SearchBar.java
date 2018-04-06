package com.flyingstudio.cumtbrowser.bar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.flyingstudio.cumtbrowser.AllButtonListener;
import com.flyingstudio.cumtbrowser.R;

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
    private Button btn_code2d;
    private Button btn_refresh;
    private EditText edit_serach;
    private String input="";
    private AllButtonListener listener;
    private InputMethodManager inputMethodManager = (InputMethodManager) getContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);

    public void setSearchBarText(String s){
        edit_serach.setText(s);
    }

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
                if (hasFocus){
                    btn_delete_input.setVisibility(VISIBLE);
                    btn_refresh.setVisibility(GONE);
                    btn_cancel.setVisibility(VISIBLE);

                    listener.record();
                    listener.hideButtomBar();
                    edit_serach.selectAll();

                    //显示其软键盘
                    inputMethodManager.showSoftInput(v,InputMethodManager.SHOW_FORCED);
                }else {
                    btn_delete_input.setVisibility(GONE);
                    btn_access.setVisibility(GONE);
                    btn_baidu.setVisibility(GONE);
                    btn_cancel.setVisibility(GONE);
                    btn_refresh.setVisibility(VISIBLE);

                    listener.showButtomBar();

                    //隐藏软键盘
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                }
            }
        });

        edit_serach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                btn_refresh.setVisibility(GONE);
                btn_cancel.setVisibility(GONE);

                input=edit_serach.getText().toString();
                if (Patterns.WEB_URL.matcher(input).matches()){
                    btn_cancel.setVisibility(GONE);
                    btn_baidu.setVisibility(GONE);
//                    btn_access.setVisibility(VISIBLE);
                    if (edit_serach.hasFocus()) {
                        btn_access.setVisibility(VISIBLE);
                        btn_refresh.setVisibility(GONE);
                    }else {
                        btn_refresh.setVisibility(VISIBLE);
                        btn_access.setVisibility(GONE);
                    }
                }else {
                    btn_cancel.setVisibility(GONE);
                    btn_access.setVisibility(GONE);
                    if (edit_serach.hasFocus()) {
                        btn_baidu.setVisibility(VISIBLE);
                        btn_refresh.setVisibility(GONE);
                    }else {
                        btn_refresh.setVisibility(VISIBLE);
                        btn_baidu.setVisibility(GONE);
                    }
                }
                if (input.isEmpty()){
                    btn_baidu.setVisibility(GONE);
                    btn_cancel.setVisibility(VISIBLE);
                    btn_refresh.setVisibility(GONE);
                }
//                Log.d(TAG, "beforeTextChanged: 输入结束");
            }
        });
    }

    public void loseFocus(){
        edit_serach.setFocusable(false);
    }

    private void initView() {
        btn_delete_input=(Button)findViewById(R.id.btn_delete_input);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        btn_baidu=(Button)findViewById(R.id.btn_baidu);
        btn_access=(Button)findViewById(R.id.btn_access);
        btn_code2d=(Button)findViewById(R.id.btn_code2d);
        btn_refresh=(Button)findViewById(R.id.btn_refresh);
        edit_serach=(EditText)findViewById(R.id.edit_serach);
        edit_serach.clearFocus();
        btn_delete_input.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_baidu.setOnClickListener(this);
        btn_access.setOnClickListener(this);
        btn_code2d.setOnClickListener(this);
        edit_serach.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);
    }

    public void hideBtnCancel(){
        btn_cancel.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_refresh:
                listener.refresh();
                break;
            case R.id.edit_serach:
                edit_serach.setFocusable(true);//设置输入框可聚集
                edit_serach.setFocusableInTouchMode(true);//设置触摸聚焦
                edit_serach.requestFocus();//请求焦点
                edit_serach.findFocus();//获取焦点
                break;
            case R.id.btn_code2d:
                Log.d(TAG, "onClick: btn_code2d");
                listener.scan2dCode();
                break;
            case R.id.btn_delete_input:
                edit_serach.setText("");
                break;
            case R.id.btn_cancel:
                listener.cancel();
                edit_serach.setFocusable(false);
                break;
            case R.id.btn_baidu:
                input=edit_serach.getText().toString();
                listener.access(input);
                edit_serach.setText("");
                edit_serach.setFocusable(false);
//                Toast.makeText(getContext(),"百度一下",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_access:
                input=edit_serach.getText().toString();
                listener.access(input);
                edit_serach.setText("");
                edit_serach.setFocusable(false);
                break;
            default:
                break;
        }
    }

    public void setAllButtonListener(AllButtonListener listener) {
        this.listener=listener;
    }

}
