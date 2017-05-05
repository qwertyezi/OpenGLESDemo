package com.yezi.openglesdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yezi.openglmedia.filter.BaseFilter;
import com.yezi.openglmedia.filter.BeautyFilter;
import com.yezi.openglmedia.filter.BlurFilter;
import com.yezi.openglmedia.filter.BrightnessFilter;
import com.yezi.openglmedia.filter.FilterGroup;
import com.yezi.openglmedia.filter.GrayFilter;
import com.yezi.openglmedia.utils.enums.FilterType;
import com.yezi.openglmedia.utils.enums.ScaleType;
import com.yezi.openglmedia.view.VideoGLSurfaceView;


public class TestVideoGLActivity extends AppCompatActivity {

    private VideoGLSurfaceView mSurfaceView;
    private final BaseFilter[] filters = {
            new GrayFilter(FilterType.VIDEO),
            new BrightnessFilter(FilterType.VIDEO).setBrightness(-0.3f),
            new BeautyFilter(FilterType.VIDEO),
            new BlurFilter(FilterType.VIDEO).setIntensity(16),
            new FilterGroup(new BrightnessFilter().setBrightness(-0.3f))
                    .setFilterType(FilterType.VIDEO)
    };
    private final ScaleType[] scaleTypes = {
            ScaleType.CENTER_INSIDE, ScaleType.CENTER_CROP, ScaleType.FIT_XY
    };
    private int mCurrentScaleType;
    private int mCurrentFilter = 0;

    private Button mBtnScaleType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_videogl);
        mSurfaceView = (VideoGLSurfaceView) findViewById(R.id.surface_view);
        mSurfaceView.setLoopPlay(true);
        mSurfaceView.setVideoUri("android.resource://" + getPackageName() + "/" + R.raw.test);
        mBtnScaleType = (Button) findViewById(R.id.btn_scale_type);
        mBtnScaleType.setText(mSurfaceView.getScaleType().toString());
    }

    public void onFilterChangeClick(View view) {
        ++mCurrentFilter;
        if (mCurrentFilter == filters.length) {
            mCurrentFilter = 0;
        }
        mSurfaceView.setFilter(filters[mCurrentFilter]);
    }

    public void onScaleTypeChangeClick(View view) {
        ++mCurrentScaleType;
        if (mCurrentScaleType == scaleTypes.length) {
            mCurrentScaleType = 0;
        }
        mSurfaceView.setScaleType(scaleTypes[mCurrentScaleType]);
        mBtnScaleType.setText(scaleTypes[mCurrentScaleType].toString());
    }

    public void onVolumeClick(View view) {
        mSurfaceView.toggleVolume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSurfaceView.onPause();
        mSurfaceView.release();
    }
}
