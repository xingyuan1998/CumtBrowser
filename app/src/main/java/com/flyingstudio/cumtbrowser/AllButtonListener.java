package com.flyingstudio.cumtbrowser;

/**
 * Created by MEzzsy on 2018/3/31.
 */

public interface AllButtonListener {
    void back();//返回
    void forward();//向前
    void home();//返回到主界面
    void pages();//页面管理器
    void scan2dCode();//扫描二维码
    void cancel();//点击取消的逻辑实现
    void record();//历史记录的逻辑实现
    void access(String input);//访问
    void hideButtomBar();//隐藏底部导航栏
    void showButtomBar();//显示底部导航栏
}
