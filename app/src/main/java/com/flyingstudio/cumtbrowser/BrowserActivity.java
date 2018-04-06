package com.flyingstudio.cumtbrowser;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.flyingstudio.cumtbrowser.adapter.FragmentAdapter;
import com.flyingstudio.cumtbrowser.adapter.WebFragmentAdapter;
import com.flyingstudio.cumtbrowser.bar.BottomBar;
import com.flyingstudio.cumtbrowser.bar.SearchBar;
import com.flyingstudio.cumtbrowser.fragment.BrowserFragment;
import com.flyingstudio.cumtbrowser.fragment.HistoryFragment;
import com.flyingstudio.cumtbrowser.fragment.MainFragment;
import com.flyingstudio.cumtbrowser.fragment.WebFragment;
import com.flyingstudio.cumtbrowser.interfaces.BottomListener;
import com.flyingstudio.cumtbrowser.interfaces.FeedBackListener;
import com.flyingstudio.cumtbrowser.interfaces.SearchListener;
import com.flyingstudio.cumtbrowser.interfaces.WebListener;
import com.flyingstudio.cumtbrowser.util.MyViewPager;
import com.google.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

public class BrowserActivity extends AppCompatActivity implements SearchListener,
        BottomListener, HistoryFragment.clearHistoryListener,
        FeedBackListener{
    private static final String TAG = "BrowserActivity";
    private SearchBar searchBar;//搜索栏
    public static BottomBar bottomBar;//底部导航栏
    private List<String> historyRecords = new ArrayList<>();//历史记录的列表
    private HistoryFragment historyFragment;//历史记录碎片
    private final static int CODE = 1028;
    private FragmentManager manager = getSupportFragmentManager();
    public static List<String> pageNameList =new ArrayList<>();
    private List<Fragment> pageList = new ArrayList<>();
    public static int currentPosition=1;
    private MyViewPager web_view_page;
    private WebFragmentAdapter fragmentAdapter;
    private WebListener listener;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        return;
//        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        initView();
        //接收上次的历史记录
        SharedPreferences preferences = getSharedPreferences("history",
                Context.MODE_PRIVATE);
        int size = preferences.getInt("size", 0);
        for (int i = 0; i < size; i++) {
            historyRecords.add(preferences.getString(i + "", ""));
        }
    }

    private void initView() {
        web_view_page = (MyViewPager) findViewById(R.id.web_view_page);
        WebFragment webFragment=new WebFragment();
        webFragment.setFeedBackListener(this);

        historyFragment = new HistoryFragment(historyRecords);
        pageList.add(historyFragment);
        searchBar = (SearchBar) findViewById(R.id.serach_bar);
        searchBar.setSearchListener(this);
        bottomBar = (BottomBar) findViewById(R.id.bottom_bar);
        bottomBar.setBottomListener(this);
        bottomBar.setPagesList(pageNameList);

        pageList.add(webFragment);
        pageNameList.add("主界面");

        fragmentAdapter = new WebFragmentAdapter(manager, pageList);
        web_view_page.setAdapter(fragmentAdapter);
        web_view_page.setCurrentItem(1);

        historyFragment.setClearHistoryListener(this);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    myToast("取消权限将不能使用部分功能");
                }
                break;
            default:
                break;
        }
    }

//    //碎片更换
//    private void replaceFragment(Fragment fragment) {
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.fragment, fragment);
//        transaction.commit();
//    }

//    //碎片更换
//    private void addFragment(Fragment fragment) {
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.add(R.id.web_view_page, fragment);
//        transaction.commit();
//    }
//
//    //隐藏碎片
//    private void hideFragment(Fragment fragment) {
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.hide(fragment);
//        transaction.commit();
//    }
//
//    //显示碎片
//    private void showFragment(Fragment fragment) {
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.show(fragment);
//        transaction.commit();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE) {
            String result;
            if (data != null) {
                result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
                searchBar.setSearchBarText(result);
            } else {
                result = "未扫码或扫码失败";
                myToast(result);
            }
        }
    }

    //扫描二维码
    @Override
    public void scan2dCode() {
        if (ContextCompat.checkSelfPermission(BrowserActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BrowserActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivityForResult(intent, CODE);
        }
    }

    //按钮取消的逻辑
    @Override
    public void cancel() {
        searchBar.loseFocus();
        web_view_page.setCurrentItem(currentPosition);
//        Fragment currentFragment = manager.findFragmentById(R.id.fragment);
////        Log.d(TAG, "当前的碎片是" + currentFragment.toString());
//
//        if (currentFragment == historyFragment) {
//            if (browserFragment==null||!browserFragment.isAdded()) {
//                home();
//            } else {
//                hideFragment(historyFragment);
//                showFragment(browserFragment);
//            }
//        }
//
////        if (currentFragment == browserFragment) {
////            if (historyFragment.isVisible()){
////                hideFragment(historyFragment);
////                showFragment(currentFragment);
////            }
////        }
    }

    //按钮访问的逻辑
    @Override
    public void access(String input) {
        web_view_page.setCurrentItem(currentPosition);
        searchBar.hideBtnCancel();

        if (!historyRecords.contains(input)) {
            historyRecords.add(0, input);//使最新的记录放在最开头
            historyFragment.dataChanged();
        }

        listener.accessToWeb(input);

//        hideFragment(historyFragment);
//        pageNameChanged(currentPosition,input);
    }

    private void pageNameChanged(int currentPosition, String input) {
        int position =currentPosition-1;
        pageNameList.set(position,input);
        bottomBar.dataChanged();
    }

    @Override
    public void jumpToNewPage(int position) {
//        Log.d(TAG, "------------------------------------------");
//        Log.d(TAG, "当前的碎片: "+pageList.get(currentPosition));
//        if (!historyFragment.isHidden()){
////            hideFragment(historyFragment);
//        }
        position++;
        if (currentPosition==position){
            return;
        }else {
//            hideFragment(pageList.get(currentPosition));
            currentPosition = position;
            WebFragment webFragment=new WebFragment();
            webFragment.setFeedBackListener(this);
            pageList.add(webFragment);
            fragmentAdapter.notifyDataSetChanged();
            web_view_page.setCurrentItem(currentPosition);
            if (pageNameList.get(currentPosition-1).equals("新页面")||
                    pageNameList.get(currentPosition-1).equals("主界面")){
                searchBar.setSearchBarText("");
            }else {
                searchBar.setSearchBarText(pageNameList.get(currentPosition - 1));
            }
//            if (pageNameList.get(currentPosition).equals("新页面")) {
//                if (currentPosition>=pageList.size()) {
//                    MainFragment mainFragment = new MainFragment();
////                    addFragment(mainFragment);
////                    pageList.add(mainFragment);
//                    searchBar.setSearchBarText(null);
//                }else {
////                    showFragment(pageList.get(currentPosition));
//                }
//            }else {
////                showFragment(pageList.get(currentPosition));
//                searchBar.setSearchBarText(pageNameList.get(currentPosition));
//            }
        }
//        Log.d(TAG, "------------------------------------------");
    }

    @Override
    public void refresh() {
        listener.webRefresh();
    }

    @Override
    public void onFocus() {
        bottomBar.setVisibility(View.GONE);
    }

    @Override
    public void loseFocus() {
        bottomBar.setVisibility(View.VISIBLE);
    }

    //历史记录
    @Override
    public void record() {
        web_view_page.setCurrentItem(0);
    }

    //向后
    @Override
    public void back() {
        listener.webBack();
    }

    //向前
    @Override
    public void forward() {
        listener.webForward();
//        browserFragment.getWebView().goForward();
    }

    //主界面按钮
    @Override
    public void home() {
//        replaceFragment(new MainFragment());
        searchBar.setSearchBarText("");
        listener.webHome();
//        pageNameChanged(currentPosition,"主界面");
    }

    @Override
    public void pages() {
//        myToast("多窗口");
//        Log.d(TAG, "多窗口");
    }

    //按钮清空历史记录的逻辑
    @Override
    public void clear() {
        historyRecords.clear();
    }

    //点击历史记录网址并跳转的逻辑
    @Override
    public void jumpAgain(String content) {
        access(content);
        searchBar.loseFocus();
    }

    //可以偷懒的Toast
    private void myToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        searchBar.loseFocus();
        web_view_page.setCurrentItem(currentPosition);
        Log.d(TAG, "-------------------------");
        Log.d(TAG, "currentPosition: "+currentPosition);
        Log.d(TAG, "manager.findFragmentById(R.id.web_view_page)): "+
                manager.findFragmentById(R.id.web_view_page));
        if (manager.findFragmentById(R.id.web_view_page)instanceof WebFragment){
            ((WebFragment) pageList.get(currentPosition)).myOnBackPressed();
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exit();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    private void exit() {
//        if ((System.currentTimeMillis() - exitTime) > 2000) {
//            myToast("再按一次退出程序");
//            exitTime = System.currentTimeMillis();
//        } else {
//            finish();
//        }
//    }

    public void setWebListener(WebListener listener){
        this.listener=listener;
    }

    @Override
    public void changeCurrentUrl(String s) {
        searchBar.setSearchBarText(s);
        pageNameChanged(currentPosition,s);
    }
}
