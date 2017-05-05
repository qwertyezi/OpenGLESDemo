package com.yezi.openglmedia.recorder;

import com.yezi.openglmedia.filter.BaseFilter;

public class RenderHandler {

    private CodecInputSurface mCodecInputSurface;
    private Thread mThread;
    private BaseFilter mVideoFilter;
    private boolean mCanRender = false;
    private float[] mTransformMatrix = new float[16];

    public RenderHandler(BaseFilter videoFilter, CodecInputSurface inputSurface) {
        mVideoFilter = videoFilter;
        mCodecInputSurface = inputSurface;
    }

    public void doRender(float[] matrix) {
        mTransformMatrix = matrix;
        mCanRender = true;
    }

    public void startRender() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mCodecInputSurface.makeCurrent();
                while (true) {
                    if (mCanRender) {
                        mVideoFilter.setTransformMatrix(mTransformMatrix);
                        mVideoFilter.onDrawFrame();
                        mCodecInputSurface.swapBuffers();
                        mCanRender = false;
                    }
                }
            }
        });
        mThread.start();
    }

    public void stopRender() {
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
    }
}
