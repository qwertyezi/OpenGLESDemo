package com.yezi.openglesdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yezi.openglmedia.filter.BaseFilter;
import com.yezi.openglmedia.filter.BeautyFilter;
import com.yezi.openglmedia.filter.BeautyFilter2;
import com.yezi.openglmedia.filter.BeautyFilter3;
import com.yezi.openglmedia.filter.BlurFilter;
import com.yezi.openglmedia.filter.NoFilter;
import com.yezi.openglmedia.utils.camera.CameraInstance;
import com.yezi.openglmedia.utils.enums.FilterType;
import com.yezi.openglmedia.utils.enums.ScaleType;
import com.yezi.openglmedia.view.CameraGLSurfaceView;


public class TestCameraGLActivity extends AppCompatActivity {

    private CameraGLSurfaceView mSurfaceView;

    private final BaseFilter[] filters = {
            new NoFilter(),
            new BeautyFilter(FilterType.VIDEO).setFlag(6),
            new BeautyFilter2(FilterType.VIDEO).setBeautyLevel(5),
            new BeautyFilter3(FilterType.VIDEO).setBeautyLevel(6),
            new BlurFilter(FilterType.VIDEO).setIntensity(16),
    };
    private final ScaleType[] scaleTypes = {
            ScaleType.CENTER_INSIDE, ScaleType.CENTER_CROP, ScaleType.FIT_XY
    };
    private int mCurrentScaleType;
    private int mCurrentFilter;

    private Button mBtnScaleType;
    private TextView mTextFrameCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test_cameragl);

        mSurfaceView = (CameraGLSurfaceView) findViewById(R.id.surface_view);
        mTextFrameCount = (TextView) findViewById(R.id.text);
        mBtnScaleType = (Button) findViewById(R.id.btn_scale_type);
        mBtnScaleType.setText(mSurfaceView.getScaleType().toString());

        CameraInstance.getInstance().setRotation(getWindowManager().getDefaultDisplay().getRotation());

        mSurfaceView.setFrameCountListener(new CameraGLSurfaceView.onFrameCountListener() {
            @Override
            public void onFrameCount(int count) {
                mTextFrameCount.setText("相机帧率：" + count);
            }
        });
    }

    public void onSwitchClick(View view) {
        CameraInstance.getInstance().switchCamera();
    }

    public void onFilterChangeClick(View view) {
        ++mCurrentFilter;
        if (mCurrentFilter == filters.length) {
            mCurrentFilter = 0;
        }
        mSurfaceView.setFilter(filters[mCurrentFilter]);
    }

    public void onStopClick(View view) {
        mSurfaceView.stopRecording();
    }

    public void onEncoderClick(View view) {
        mSurfaceView.startRecording();
    }

    public void onScaleTypeChangeClick(View view) {
        ++mCurrentScaleType;
        if (mCurrentScaleType == scaleTypes.length) {
            mCurrentScaleType = 0;
        }
        mSurfaceView.setScaleType(scaleTypes[mCurrentScaleType]);
        mBtnScaleType.setText(scaleTypes[mCurrentScaleType].toString());
    }

    public void onCameraClick(View view) {
        mSurfaceView.saveBitmap(null);
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
