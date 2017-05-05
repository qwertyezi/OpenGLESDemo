package com.yezi.openglesdemo;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.yezi.openglmedia.filter.BaseFilter;
import com.yezi.openglmedia.filter.BeautyFilter;
import com.yezi.openglmedia.filter.BlurFilter;
import com.yezi.openglmedia.filter.BrightnessFilter;
import com.yezi.openglmedia.filter.FilterGroup;
import com.yezi.openglmedia.filter.GrayFilter;
import com.yezi.openglmedia.utils.enums.ScaleType;
import com.yezi.openglmedia.view.ImageGLSurfaceView;

public class TestImageGLActivity extends AppCompatActivity {

    private ImageGLSurfaceView mSurfaceView;
    private SeekBar mSeekBar;
    private final int[] images = {
            R.mipmap.image_1, R.mipmap.image_2,
            R.mipmap.image_3, R.mipmap.image_4,
            R.mipmap.image_5, R.mipmap.image_6
    };
    private final BaseFilter[] filters = {
            new GrayFilter(),
            new BlurFilter().setIntensity(16),
            new BrightnessFilter().setBrightness(-0.3f),
            new BeautyFilter().setFlag(6),
            new FilterGroup(new BeautyFilter().setFlag(6), new GrayFilter())
    };
    private final ScaleType[] scaleTypes = {
            ScaleType.CENTER_INSIDE, ScaleType.CENTER_CROP, ScaleType.FIT_XY
    };
    private int mCurrentImage;
    private int mCurrentFilter;
    private int mCurrentScaleType;
    private Button mBtnScaleType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_imagegl);

        mSurfaceView = (ImageGLSurfaceView) findViewById(R.id.glsurfaceview);
        mBtnScaleType = (Button) findViewById(R.id.btn_scale_type);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);

        mSurfaceView.setBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.image_1));
        mBtnScaleType.setText(mSurfaceView.getScaleType().toString());

        mCurrentImage = 0;
        mCurrentFilter = 0;
        mCurrentScaleType = 0;

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onSaveClick(View view) {
        mSurfaceView.saveBitmap(null);
    }

    public void onImageChangeClick(View view) {
        ++mCurrentImage;
        if (mCurrentImage == images.length) {
            mCurrentImage = 0;
        }
        mSurfaceView.setBitmap(BitmapFactory.decodeResource(getResources(), images[mCurrentImage]));
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
