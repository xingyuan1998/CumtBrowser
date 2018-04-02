package com.flyingstudio.cumtbrowser.bar;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.flyingstudio.cumtbrowser.AllButtonListener;
import com.flyingstudio.cumtbrowser.R;
import com.flyingstudio.cumtbrowser.adapter.ButtonAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by MEzzsy on 2018/3/24.
 */

public class BottomBar extends LinearLayout implements View.OnClickListener {
    private ImageButton btn_back;
    private ImageButton btn_forward;
    private ImageButton btn_home;
    private ImageButton btn_all_pages;
    private ImageButton btn_settings;
    private AllButtonListener listener;
    private PopupWindow popupWindow;
    private List<MyButton> buttonList = new ArrayList<>();

    public BottomBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bottom_bar, this);
        initView();
    }

    private void initView() {
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_forward = (ImageButton) findViewById(R.id.btn_forward);
        btn_home = (ImageButton) findViewById(R.id.btn_home);
        btn_all_pages = (ImageButton) findViewById(R.id.btn_all_pages);
        btn_settings = (ImageButton) findViewById(R.id.btn_settings);
        btn_back.setOnClickListener(this);
        btn_forward.setOnClickListener(this);
        btn_home.setOnClickListener(this);
        btn_all_pages.setOnClickListener(this);
        btn_settings.setOnClickListener(this);
        initButtons();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                listener.back();
                break;
            case R.id.btn_forward:
                listener.forward();
                break;
            case R.id.btn_home:
                listener.home();
                break;
            case R.id.btn_all_pages:
                listener.pages();
                break;
            case R.id.btn_settings:
                showSettings();
                break;
            default:
                break;
        }
    }

    private void showSettings() {
//        this.setVisibility(GONE);
        //设置contentView
        View contentView = LayoutInflater.from(getContext()).
                inflate(R.layout.popuplayout, null);
        //获取屏幕宽度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().
                getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int w = displayMetrics.widthPixels - 60;

        popupWindow = new PopupWindow(contentView,
                w, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(contentView);

        //初始化RecyclerView
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        ButtonAdapter adapter = new ButtonAdapter(buttonList);
        recyclerView.setAdapter(adapter);

        //显示PopupWindow
//        popupWindow.showAtLocation(this,Gravity.BOTTOM, 0, this.getHeight()+150);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        //适配高度
        int currentHeight = 20;
        if (checkDeviceHasNavigationBar2(getContext())) {
            currentHeight += this.getHeight() + getNavigationBarHeight();
        } else {
            currentHeight += this.getHeight();
        }
        Log.d(TAG, "showSettings: " + checkDeviceHasNavigationBar2(getContext()));
        popupWindow.showAtLocation(this, Gravity.BOTTOM, 0, currentHeight);

//        popupWindow.update();
        Log.d(TAG, "showSettings: " + this.toString() + "\n" + "高度= " + currentHeight);
    }

    //判断设备是否有虚拟按键（navifationbar）。
    private static boolean checkDeviceHasNavigationBar2(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar",
                "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass,
                    "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    //获取虚拟按键的高度
    private int getNavigationBarHeight() {
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }


    private void initButtons() {
        MyButton button1 = new MyButton("UI测试", R.drawable.example);
        buttonList.add(button1);
        MyButton button2 = new MyButton("UI测试", R.drawable.example);
        buttonList.add(button2);
        MyButton button3 = new MyButton("UI测试", R.drawable.example);
        buttonList.add(button3);
        MyButton button4 = new MyButton("UI测试", R.drawable.example);
        buttonList.add(button4);
        MyButton button5 = new MyButton("UI测试", R.drawable.example);
        buttonList.add(button5);
        MyButton button6 = new MyButton("UI测试", R.drawable.example);
        buttonList.add(button6);
        MyButton button7 = new MyButton("UI测试", R.drawable.example);
        buttonList.add(button7);
        MyButton button8 = new MyButton("UI测试", R.drawable.example);
        buttonList.add(button8);
    }

    public void setAllButtonListener(AllButtonListener listener) {
        this.listener = listener;
    }


}
