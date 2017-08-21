package com.ryan.customview;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private TabLayout mTlTitle;
    private ViewPager mVpContent;
    private ArrayList<PageModel> mModels = new ArrayList<>();

    {
        //只用在这里更改就可以
        mModels.add(new PageModel(R.layout.fragment_radar, R.string.title_radar));
        mModels.add(new PageModel(R.layout.fragment_circle, R.string.title_circle));
        mModels.add(new PageModel(R.layout.fragment_search, R.string.title_search));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mVpContent.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return BaseFragment.newInstance(mModels.get(position).mLayoutRes
                        , mModels.get(position).mTitle);
            }

            @Override
            public int getCount() {
                return mModels.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(mModels.get(position).mTitle);
            }
        });
        mTlTitle.setupWithViewPager(mVpContent, true);
        mTlTitle.getTabAt(0).select();

    }

    private void initView() {
        mTlTitle = (TabLayout) findViewById(R.id.tl_title);
        mVpContent = (ViewPager) findViewById(R.id.vp_content);
    }

    private class PageModel {
        @LayoutRes
        int mLayoutRes;
        @StringRes
        int mTitle;

        public PageModel(int layoutRes, int title) {
            mLayoutRes = layoutRes;
            mTitle = title;
        }
    }
}
