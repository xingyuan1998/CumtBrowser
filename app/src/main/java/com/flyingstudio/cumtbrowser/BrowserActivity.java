package com.flyingstudio.cumtbrowser;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.flyingstudio.cumtbrowser.bar.BottomBar;
import com.flyingstudio.cumtbrowser.bar.SearchBar;
import com.flyingstudio.cumtbrowser.fragment.BrowserFragment;
import com.flyingstudio.cumtbrowser.fragment.HistoryFragment;
import com.flyingstudio.cumtbrowser.fragment.MainFragment;
import com.google.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

public class BrowserActivity extends AppCompatActivity implements
        AllButtonListener, HistoryFragment.clearHistoryListener {
    private Fragment fragment;
    private BrowserFragment browserFragment;
    private static final String TAG = "BrowserActivity";
    private String input;//输入的内容
    private SearchBar searchBar;//搜索栏
    private BottomBar bottomBar;//底部导航栏
    private List<String> historyRecords = new ArrayList<>();//历史记录的列表
    private HistoryFragment historyFragment;//历史记录碎片
    private final static int CODE = 1028;
    private FragmentManager manager = getSupportFragmentManager();
    private List<String> pageNameList =new ArrayList<>();
    private List<Fragment> pageList=new ArrayList<>();
    private static int currentPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        MainFragment mainFragment=new MainFragment();
        replaceFragment(mainFragment);
        pageList.add(mainFragment);
        pageNameList.add("主界面");

        //接收上次的历史记录
        SharedPreferences preferences = getSharedPreferences("history",
                Context.MODE_PRIVATE);
        int size = preferences.getInt("size", 0);
        for (int i = 0; i < size; i++) {
            historyRecords.add(preferences.getString(i + "", ""));
        }

        initView();
    }

    private void initView() {
        searchBar = (SearchBar) findViewById(R.id.serach_bar);
        input = searchBar.getInput();
        searchBar.setAllButtonListener(this);
        bottomBar = (BottomBar) findViewById(R.id.bottom_bar);
        bottomBar.setAllButtonListener(this);
        bottomBar.setPagesList(pageNameList);
        historyFragment = new HistoryFragment(historyRecords);
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

    //碎片更换
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

    //碎片更换
    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment, fragment);
        transaction.commit();
    }

    //隐藏碎片
    private void hideFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
    }

    //显示碎片
    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.show(fragment);
        transaction.commit();
    }

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

        Fragment currentFragment = manager.findFragmentById(R.id.fragment);
        Log.d(TAG, "当前的碎片是" + currentFragment.toString());

        if (currentFragment == historyFragment) {
            if (browserFragment==null||!browserFragment.isAdded()) {
                home();
            } else {
                hideFragment(historyFragment);
                showFragment(browserFragment);
            }
        }

        if (currentFragment == browserFragment) {
            if (historyFragment.isVisible()){
                hideFragment(historyFragment);
                showFragment(currentFragment);
            }
        }

    }

    //按钮访问的逻辑
    @Override
    public void access(String input) {
        if (!historyRecords.contains(input)) {
            historyRecords.add(0, input);//使最新的记录放在最开头
            historyFragment.dataChanged();
        }
        browserFragment = new BrowserFragment(input);
        browserFragment.setOnFragmentInteractionListener(
                new BrowserFragment.OnFragmentInteractionListener() {
                    //实时地在搜索栏显示当前网址
                    @Override
                    public void changeCurrentUrl(String s) {
                        searchBar.setSearchBarText(s);
                    }
                });

        hideFragment(pageList.get(currentPosition));
        if (!browserFragment.isAdded()) {
            addFragment(browserFragment);
        } else {
            showFragment(browserFragment);
        }

        if (!(pageList.get(currentPosition) instanceof BrowserFragment)) {
            pageList.set(currentPosition, browserFragment);
        }
        pageNameChanged(currentPosition,input);
    }

    private void pageNameChanged(int currentPosition, String input) {
        pageNameList.set(currentPosition,input);
        bottomBar.dataChanged();
    }

    @Override
    public void hideButtomBar() {
        bottomBar.setVisibility(View.GONE);
    }

    @Override
    public void showButtomBar() {
        bottomBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void jumpToNewPage(int position) {
        Log.d(TAG, "------------------------------------------");
        Log.d(TAG, "当前的碎片: "+pageList.get(currentPosition));
        if (!historyFragment.isHidden()){
            hideFragment(historyFragment);
        }
        if (currentPosition==position){
            return;
        }else {
            hideFragment(pageList.get(currentPosition));
            currentPosition = position;
            if (pageNameList.get(currentPosition).equals("新页面")) {
                if (currentPosition>=pageList.size()) {
                    MainFragment mainFragment = new MainFragment();
                    addFragment(mainFragment);
                    pageList.add(mainFragment);
                }else {
                    showFragment(pageList.get(currentPosition));
                }
            }else {
                showFragment(pageList.get(currentPosition));
            }
        }
        Log.d(TAG, "------------------------------------------");
    }

    //历史记录
    @Override
    public void record() {
        hideFragment(manager.findFragmentById(R.id.fragment));
        if (!historyFragment.isAdded()) {
            addFragment(historyFragment);
        } else {
            showFragment(historyFragment);
        }
    }

    //向后
    @Override
    public void back() {
        if (browserFragment.getWebView().canGoBack()) {
            browserFragment.getWebView().goBack();
        } else {
            home();
        }
    }

    //向前
    @Override
    public void forward() {
        browserFragment.getWebView().goForward();
    }

    //主界面按钮
    @Override
    public void home() {
        replaceFragment(new MainFragment());
        searchBar.setSearchBarText(null);
        pageNameChanged(currentPosition,"主界面");
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
//        super.onBackPressed();
        searchBar.loseFocus();

        //获取当前的碎片
        Fragment currentFragment = manager.findFragmentById(R.id.fragment);
        Log.d(TAG, "当前的碎片是" + currentFragment.toString());

        if (currentFragment == historyFragment) {
            if (browserFragment==null||!browserFragment.isAdded()) {
                home();
            } else {
                hideFragment(historyFragment);
                showFragment(browserFragment);
            }
        }

        if (currentFragment == browserFragment) {
            if (historyFragment.isVisible()){
                hideFragment(historyFragment);
                showFragment(currentFragment);
            }
            back();
        }

        //如果当前碎片是主界面，那么就退出程序
        if (currentFragment instanceof MainFragment){
            finish();
        }
    }

}
