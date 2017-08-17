package com.ryan.customview;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by renbo on 2017/8/17.
 */

public class BaseFragment extends Fragment {
    @LayoutRes
    private int mLayoutRes;
    @StringRes
    private int mTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (null != args) {
            mLayoutRes = args.getInt("layoutRes");
            mTitle = args.getInt("title");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(mLayoutRes, container, false);
        return view;
    }


    public static BaseFragment newInstance(@LayoutRes int layoutRes, @StringRes int title) {
        BaseFragment fragment = new BaseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("layoutRes", layoutRes);
        bundle.putInt("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }
}
