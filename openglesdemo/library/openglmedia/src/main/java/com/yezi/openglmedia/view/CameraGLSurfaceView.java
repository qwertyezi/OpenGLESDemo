package com.yezi.openglmedia.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.yezi.openglmedia.BuildConfig;
import com.yezi.openglmedia.render.VideoRecordRender;
import com.yezi.openglmedia.render.VideoRender;
import com.yezi.openglmedia.utils.camera.CameraInstance;
import com.yezi.openglmedia.utils.enums.ScaleType;


public class CameraGLSurfaceView extends BaseGLSurfaceView implements SurfaceTexture.OnFrameAvailableListener {

    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String TAG = "CameraGLSurfaceView";

    public CameraGLSurfaceView(Context context) {
        super(context);
    }

    public CameraGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void startRecording() {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                ((VideoRecordRender) mBaseRender).startRecording(EGL14.eglGetCurrentContext());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        CameraInstance.getInstance().resumeCamera();
    }

    @Override
    public void release() {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mBaseRender.release();
            }
        });
        ((VideoRecordRender) mBaseRender).stopRecording();
        CameraInstance.getInstance().releaseCamera();
    }

    public void stopRecording() {
        ((VideoRecordRender) mBaseRender).stopRecording();
    }

    @Override
    protected void init() {
        mBaseRender = new VideoRecordRender();
        mBaseRender.setScaleType(ScaleType.CENTER_CROP);
        setRenderer(mBaseRender);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        ((VideoRecordRender) mBaseRender).setOnSurfaceCreatedListener(new VideoRender.onSurfaceCreatedListener() {
            @Override
            public void onSurfaceCreated() {
                ((VideoRecordRender) mBaseRender).getSurfaceTexture().setOnFrameAvailableListener(CameraGLSurfaceView.this);
                CameraInstance.getInstance().setCameraSize(720, 1280, false);
                CameraInstance.getInstance().openCamera();
                mBaseRender.setDataSize(CameraInstance.getInstance().getPreviewSize().height, CameraInstance.getInstance().getPreviewSize().width);
                CameraInstance.getInstance().startPreview(((VideoRecordRender) mBaseRender).getSurfaceTexture());
            }
        });
    }

    private int mFrameCount = 0;
    private long mLastTime = 0;

    public interface onFrameCountListener {
        void onFrameCount(int count);
    }

    private onFrameCountListener mFrameCountListener;

    public void setFrameCountListener(onFrameCountListener listener) {
        mFrameCountListener = listener;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        requestRender();

        if (DEBUG) {
            if (mLastTime == 0) {
                mLastTime = System.currentTimeMillis();
            }
            ++mFrameCount;
            if (System.currentTimeMillis() - mLastTime >= 1000) {
                Log.i(TAG, "相机帧率：" + mFrameCount);
                if (mFrameCountListener != null) {
                    mFrameCountListener.onFrameCount(mFrameCount);
                }
                mFrameCount = 0;
                mLastTime = 0;
            }
        }
    }
}
