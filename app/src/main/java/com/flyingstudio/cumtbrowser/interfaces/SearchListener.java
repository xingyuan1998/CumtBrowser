package com.flyingstudio.cumtbrowser.interfaces;

/**
 * Created by MEzzsy on 2018/4/6.
 */

public interface SearchListener {
    void scan2dCode();//扫描二维码
    void cancel();//点击取消的逻辑实现
    void record();//历史记录的逻辑实现
    void access(String input);//访问
    void refresh();//刷新
    void loseFocus();
    void onFocus();
}
