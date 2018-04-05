package com.flyingstudio.cumtbrowser.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyingstudio.cumtbrowser.R;
import com.flyingstudio.cumtbrowser.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MEzzsy on 2018/4/5.
 */

public class WebViewWindow extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_web_view_pager,container,false);
        return view;
    }

}
