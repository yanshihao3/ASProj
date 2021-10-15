package com.zq.common.ui.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @program: ASProj
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-16 15:03
 **/
public class FragmentTabView extends FrameLayout {

    private TabViewAdapter mTabViewAdapter;

    private int currentPosition;

    public FragmentTabView(@NonNull Context context) {
        this(context, null);
    }

    public FragmentTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FragmentTabView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(TabViewAdapter adapter) {
        if (this.mTabViewAdapter != null || adapter == null) {
            return;
        }
        this.mTabViewAdapter = adapter;
        currentPosition = -1;
    }

    public void setCurrentItem(int position) {
        if (position < 0 || position >= mTabViewAdapter.getCount()) {
            return;
        }
        if (currentPosition != position) {
            currentPosition = position;
            mTabViewAdapter.instantiateItem(this, position);
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public Fragment getCurrentFragment() {
        if (mTabViewAdapter == null) {
            throw new IllegalArgumentException("please call setAdapter first");
        }
        return mTabViewAdapter.getCurrentFragment();
    }
}
