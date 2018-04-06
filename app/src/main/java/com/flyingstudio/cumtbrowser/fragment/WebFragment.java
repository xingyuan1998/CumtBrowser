package com.flyingstudio.cumtbrowser.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flyingstudio.cumtbrowser.BrowserActivity;
import com.flyingstudio.cumtbrowser.R;
import com.flyingstudio.cumtbrowser.interfaces.FeedBackListener;
import com.flyingstudio.cumtbrowser.interfaces.WebListener;

/**
 * Created by MEzzsy on 2018/4/6.
 */

@SuppressLint("ValidFragment")
public class WebFragment extends Fragment implements WebListener,
        BrowserFragment.OnFragmentInteractionListener{
    private MainFragment mainFragment;
    private BrowserFragment browserFragment;
    private FragmentManager manager;
    private BrowserActivity browserActivity;
    private FeedBackListener listener;

    public WebFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        browserActivity = (BrowserActivity) getActivity();
        browserActivity.setWebListener(this);
        manager = getChildFragmentManager();
        mainFragment = new MainFragment();
        replaceFragment(mainFragment);
    }

    //碎片更换
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

    @Override
    public void accessToWeb(String input) {
        browserFragment = new BrowserFragment(input);
        browserFragment.setOnFragmentInteractionListener(this);

        if (!browserFragment.isAdded()) {
            addFragment(browserFragment);
        } else {
            showFragment(browserFragment);
        }
    }

    @Override
    public void webRefresh() {
        browserFragment.getWebView().reload();
    }

    @Override
    public void webBack() {
        if (browserFragment.getWebView().canGoBack()){
            browserFragment.getWebView().goBack();
        }else {
            webHome();
        }
    }

    @Override
    public void webForward() {
        if (browserFragment.getWebView().canGoForward()){
            browserFragment.getWebView().goForward();
        }else {
            return;
        }
    }

    @Override
    public void webHome() {
        replaceFragment(mainFragment);
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

    public void setFeedBackListener(FeedBackListener listener){
        this.listener=listener;
    }

    @Override
    public void changeCurrentUrl(String s) {
        listener.changeCurrentUrl(s);
    }

    public void myOnBackPressed(){
        if (manager.findFragmentById(R.id.fragment)instanceof BrowserFragment){
            webBack();
        }else {
            exit();
        }
    }

    private long exitTime=0;

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 1500) {
            Toast.makeText(getContext(),"再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            browserActivity.finish();
            System.exit(0);
        }
    }

}
