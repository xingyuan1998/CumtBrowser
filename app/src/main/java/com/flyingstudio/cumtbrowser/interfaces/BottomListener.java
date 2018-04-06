package com.flyingstudio.cumtbrowser.interfaces;

/**
 * Created by MEzzsy on 2018/3/31.
 */

public interface BottomListener {
    void back();//返回
    void forward();//向前
    void home();//返回到主界面
    void pages();//页面管理器
    void jumpToNewPage(int position);//跳到新的页面
}
