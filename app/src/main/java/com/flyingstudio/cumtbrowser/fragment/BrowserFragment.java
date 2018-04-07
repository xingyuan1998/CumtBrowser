package com.flyingstudio.cumtbrowser.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.flyingstudio.cumtbrowser.R;

/**
 * 网页的碎片
 */
public class BrowserFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private WebView webView;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private String input;
    private LinearLayout agentWebParent;

    public BrowserFragment() {
    }

    public WebView getWebView() {
        return webView;
    }

    @SuppressLint("ValidFragment")
    public BrowserFragment(String content){
        content=content.trim();
        input=content;
        if (Patterns.WEB_URL.matcher(content).matches()) {
            if ((input.indexOf("http://"))!=-1||(input.indexOf("https://"))!=-1){
                input = content;
            }else {
                input="https://"+input;
            }
        }else {
            input=content;
        }
    }

    public static BrowserFragment newInstance(String param1, String param2) {
        BrowserFragment fragment = new BrowserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_browser, container, false);
        webView=(WebView)view.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mListener.changeCurrentUrl(url);
            }
        });
        if (Patterns.WEB_URL.matcher(input).matches()) {
            webView.loadUrl(input);
        }else {
            webView.loadUrl("https://www.baidu.com/s?wd="+input+"&ie=UTF-8");
        }
        return view;
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener listener){
        mListener=listener;
    }

    public interface OnFragmentInteractionListener {
        void changeCurrentUrl(String s);
    }
}
