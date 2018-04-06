package com.flyingstudio.cumtbrowser.interfaces;

/**
 * Created by MEzzsy on 2018/4/6.
 */

public interface WebListener {
    void accessToWeb(String input);
    void webRefresh();

    void webBack();

    void webForward();

    void webHome();
}
