package com.flyingstudio.cumtbrowser;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
        SearchBar.onClickSearchBarListener,BottomBar.onClickBottomBarListener,
        HistoryFragment.clearHistoryListener {
    private BrowserFragment browserFragment;
//    private Button button;
    private Fragment fragment;
    private static final String TAG = "BrowserActivity";
    private String input;//输入的内容
    private SearchBar searchBar;//搜索栏
    private BottomBar bottomBar;//底部导航栏
    private List<String> historyRecords=new ArrayList<>();//历史记录的列表
    private HistoryFragment historyFragment;//历史记录碎片
    private final static int CODE=1028;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        replaceFragment(MainFragment.newInstance("dd","dd"));
//        button = findViewById(R.id.change_fragment);
        browserFragment = new BrowserFragment();
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                replaceFragment(browserFragment,R.id.fragment);
//            }
//        });
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        //接收上次的历史记录
        SharedPreferences preferences=getSharedPreferences("history",
                Context.MODE_PRIVATE);
        int size=preferences.getInt("size",0);
        for (int i=0;i<size;i++){
            historyRecords.add(preferences.getString(i+"",""));
        }

        searchBar=(SearchBar)findViewById(R.id.serach_bar);
        input=searchBar.getInput();
        searchBar.setOnClickSearchBarListener(this);
        bottomBar=(BottomBar)findViewById(R.id.bottom_bar);
        bottomBar.setOnClickBottomBarListener(this);
        historyFragment=new HistoryFragment(historyRecords);
        historyFragment.setClearHistoryListener(this);
//        edit_text=(EditText)findViewById(R.id.edit_text);

//        edit_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                showSearchBarAndHistory();
//            }
//        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }else {
                    myToast("取消权限将不能使用部分功能");
                }
                break;
            default:
                break;
        }
    }

    //    private void showSearchBarAndHistory() {
//        edit_text.setVisibility(View.GONE);
//        searchBar.setVisibility(View.VISIBLE);
//        bottomBar.setVisibility(View.GONE);
//    }

    //碎片更换
    private void replaceFragment(Fragment fragment){
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public void replaceFragment(Fragment fragment,int id){
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.fragment, fragment);
//        transaction.commit();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CODE){
            String result;
            if (data!=null) {
                result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
                searchBar.setSearchBarText(result);
            }else {
                result="未扫码或扫码失败";
                myToast(result);
            }
        }
    }

    //扫描二维码
    @Override
    public void scan2dCode() {
        if (ContextCompat.checkSelfPermission(BrowserActivity.this,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BrowserActivity.this,
                    new String[]{Manifest.permission.CAMERA},1);
        }else {
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivityForResult(intent, CODE);
        }
    }

    //按钮取消的逻辑
    @Override
    public void cancel() {
        //移除历史记录的碎片
        if (!historyFragment.isDetached()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(historyFragment);
            transaction.commit();
        }
    }

    //按钮百度一下的逻辑
    @Override
    public void baidu(String input) {
//        Toast.makeText(this,"你点了百度一下",Toast.LENGTH_SHORT).show();
        baiduORaccess(input);
//        Log.d(TAG, "你点了百度一下");
    }

    //按钮访问的逻辑
    @Override
    public void access(String input) {
//        Toast.makeText(this,"访问",Toast.LENGTH_SHORT).show();
        baiduORaccess(input);
//        Log.d(TAG, "访问");
    }

    //历史记录
    @Override
    public void record() {
        replaceFragment(historyFragment);
        Log.d(TAG, "历史记录");
    }

    @Override
    public void back() {
        Toast.makeText(this,"你点了返回",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "你点了返回");
    }

    @Override
    public void forward() {
        Toast.makeText(this,"你点了向前",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "你点了向前");
    }

    @Override
    public void home() {
        Toast.makeText(this,"你点了home",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "你点了home");
    }

    @Override
    public void pages() {
        Toast.makeText(this,"你点了页面管理器",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "你点了页面管理器");
    }

    //按钮清空历史记录的逻辑
    @Override
    public void clear() {
        historyRecords.clear();
//        Log.d(TAG, "清空历史记录");
    }

    //百度一下和访问按钮的具体逻辑
    private void baiduORaccess(String input){
        historyRecords.add(0,input);//使最新的记录放在最开头
//        Collections.reverse(historyRecords);
        historyFragment.dataChanged();
    }

    //可以偷懒的Toast
    private void myToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        searchBar.loseFocus();
//        bottomBar.setVisibility(View.VISIBLE);
    }
}
