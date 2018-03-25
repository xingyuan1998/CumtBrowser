package com.flyingstudio.cumtbrowser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.flyingstudio.cumtbrowser.bar.BottomBar;
import com.flyingstudio.cumtbrowser.bar.SearchBar;

public class MainActivity extends AppCompatActivity implements
        SearchBar.onClickSearchBarListener,BottomBar.onClickBottomBarListener{
    private static final String TAG = "MainActivity";
    private String input;//输入的内容
    private SearchBar searchBar;//搜索栏
    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        searchBar=(SearchBar)findViewById(R.id.serach_bar);
        input=searchBar.getInput();
        searchBar.setOnClickSearchBarListener(this);
        bottomBar=(BottomBar)findViewById(R.id.bottom_bar);
        bottomBar.setOnClickBottomBarListener(this);

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

    @Override
    public void cancel() {
        Toast.makeText(this,"你点了取消",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "你点了取消");
    }

    @Override
    public void baidu(String input) {
        Toast.makeText(this,"你点了百度一下",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "你点了百度一下");
    }

    @Override
    public void access(String input) {
        Toast.makeText(this,"访问",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "访问");
    }

    @Override
    public void record() {
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
}
