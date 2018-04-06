package com.flyingstudio.cumtbrowser.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyingstudio.cumtbrowser.R;
import com.flyingstudio.cumtbrowser.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MainFragment extends Fragment {

    private ViewPager viewPager;
    private List<Fragment>fragments=new ArrayList<>();
    private FragmentAdapter adapter;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewPager = getView().findViewById(R.id.viewPager);
        fragments.add(new NavFragment());
        fragments.add(new NewsFragment());
        adapter=new FragmentAdapter(getChildFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
    }

}
