package com.zq.asproj.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.zq.asproj.R;
import com.zq.ui.refresh.OverView;


/**
 * @program: ASProj
 * @description:
 * @author: 闫世豪
 * @create: 2021-09-17 11:14
 **/
public class LottieOverView extends OverView {

    private LottieAnimationView pullAnimationView;

    public LottieOverView(@NonNull Context context) {
        super(context);
    }

    public LottieOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LottieOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.refresh_lottie_overview, this, true);
        pullAnimationView = findViewById(R.id.lottie_layer_name);
        pullAnimationView.setAnimation("loading_wave.json");

    }

    @Override
    protected void onScroll(int scrollY, int pullRefreshHeight) {

    }

    @Override
    protected void onVisible() {

    }

    @Override
    public void onOver() {

    }

    @Override
    public void onRefresh() {
        pullAnimationView.setSpeed(2);
        pullAnimationView.playAnimation();
    }

    @Override
    public void onFinish() {
        pullAnimationView.setProgress(0f);
        pullAnimationView.cancelAnimation();
    }
}
