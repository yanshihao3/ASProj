package com.zq.asproj.logic;

/**
 * @program: ASProj
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-16 15:17
 **/

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import com.zq.asproj.R;
import com.zq.asproj.fragment.CategoryFragment;
import com.zq.asproj.fragment.FavoriteFragment;
import com.zq.asproj.fragment.HomePageFragment;
import com.zq.asproj.fragment.ProfileFragment;
import com.zq.asproj.fragment.RecommendFragment;
import com.zq.common.ui.tab.FragmentTabView;
import com.zq.common.ui.tab.TabViewAdapter;
import com.zq.ui.tab.bottom.TabBottomInfo;
import com.zq.ui.tab.bottom.TabBottomLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 将MainActivity的一些逻辑内聚在这里，让MainActivity更清晰
 */
public class MainActivityLogic {

    private FragmentTabView mFragmentTabView;

    private ActivityProvider mActivityProvider;

    private List<TabBottomInfo<?>> mInfoList;

    private TabBottomLayout mTabBottomLayout;

    private int currentItemIndex;

    private final static String SAVED_CURRENT_ID = "SAVED_CURRENT_ID";

    public MainActivityLogic(ActivityProvider activityProvider, @Nullable Bundle savedInstanceState) {
        mActivityProvider = activityProvider;
        //fix 不保留活动导致的Fragment重叠问题
        if (savedInstanceState != null) {
            currentItemIndex = savedInstanceState.getInt(SAVED_CURRENT_ID);
        }
        initBottom();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SAVED_CURRENT_ID, currentItemIndex);
    }

    public FragmentTabView getFragmentTabView() {
        return mFragmentTabView;
    }

    public List<TabBottomInfo<?>> getInfoList() {
        return mInfoList;
    }

    public TabBottomLayout getHiTabBottomLayout() {
        return mTabBottomLayout;
    }

    private void initBottom() {
        mTabBottomLayout = mActivityProvider.findViewById(R.id.tab_bottom_layout);
        mInfoList = new ArrayList<>();
        int defaultColor = mActivityProvider.getResources().getColor(R.color.tabBottomDefaultColor);
        int tintColor = mActivityProvider.getResources().getColor(R.color.tabBottomTintColor);
        TabBottomInfo homeInfo = new TabBottomInfo<Integer>(
                "首页",
                "fonts/iconfont.ttf",
                mActivityProvider.getString(R.string.if_home),
                null,
                defaultColor,
                tintColor
        );
        homeInfo.fragment = HomePageFragment.class;
        TabBottomInfo infoFavorite = new TabBottomInfo<Integer>(
                "收藏",
                "fonts/iconfont.ttf",
                mActivityProvider.getString(R.string.if_favorite),
                null,
                defaultColor,
                tintColor
        );
        infoFavorite.fragment = FavoriteFragment.class;
        TabBottomInfo infoCategory = new TabBottomInfo<Integer>(
                "分类",
                "fonts/iconfont.ttf",
                mActivityProvider.getString(R.string.if_category),
                null,
                defaultColor,
                tintColor
        );
        infoCategory.fragment = CategoryFragment.class;
        TabBottomInfo infoRecommend = new TabBottomInfo<Integer>(
                "推荐",
                "fonts/iconfont.ttf",
                mActivityProvider.getString(R.string.if_recommend),
                null,
                defaultColor,
                tintColor
        );
        infoRecommend.fragment = RecommendFragment.class;
        TabBottomInfo infoProfile = new TabBottomInfo<Integer>(
                "我的",
                "fonts/iconfont.ttf",
                mActivityProvider.getString(R.string.if_profile),
                null,
                defaultColor,
                tintColor
        );
        infoProfile.fragment = ProfileFragment.class;
        mInfoList.add(homeInfo);
        mInfoList.add(infoFavorite);
        mInfoList.add(infoCategory);
        mInfoList.add(infoRecommend);
        mInfoList.add(infoProfile);
        mTabBottomLayout.inflateInfo(mInfoList);
        initFragmentTabView();
        mTabBottomLayout.addTabSelectedChangeListener((index, prevInfo, nextInfo) -> {
            mFragmentTabView.setCurrentItem(index);
            MainActivityLogic.this.currentItemIndex = index;
        });
        mTabBottomLayout.defaultSelected(mInfoList.get(currentItemIndex));
    }

    private void initFragmentTabView() {
        TabViewAdapter tabViewAdapter = new TabViewAdapter(mActivityProvider.getSupportFragmentManager(), mInfoList);
        mFragmentTabView = mActivityProvider.findViewById(R.id.fragment_tab_view);
        mFragmentTabView.setAdapter(tabViewAdapter);
    }


    public static interface ActivityProvider {
        <T extends View> T findViewById(@IdRes int id);

        Resources getResources();

        FragmentManager getSupportFragmentManager();

        String getString(@StringRes int resId);
    }
}
