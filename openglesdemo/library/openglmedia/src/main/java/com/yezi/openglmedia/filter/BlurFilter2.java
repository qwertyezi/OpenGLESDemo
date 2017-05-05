package com.yezi.openglmedia.filter;

import android.opengl.GLES20;

import com.yezi.openglmedia.utils.enums.FilterType;

import java.util.Collections;

public class BlurFilter2 extends FilterGroup {

    private static final int LEVEL = 30;
    private int mIntensity = 0;
    private static final int BLUR_SIZE = 512;

    public BlurFilter2() {
        this(FilterType.IMAGE);
    }

    public BlurFilter2(FilterType filterType) {
        super();
        setFilterType(filterType);
    }

    public BlurFilter2 setIntensity(int intensity) {
        if (intensity != mIntensity && intensity >= 0) {
            mIntensity = intensity;
            if (mIntensity > LEVEL)
                mIntensity = LEVEL;
        }
        addFilters();
        return this;
    }

    private void addFilters() {
        initFilters(Collections.nCopies(mIntensity, new BaseFilter() {
            @Override
            public void onDraw() {

            }

            @Override
            public void onCreated(int mProgram) {

            }

            @Override
            public void onChanged(int width, int height) {

            }
        }).toArray(new BaseFilter[mIntensity]));
    }

    @Override
    public void onDrawFrame() {
        if (mFrameBuffers == null || mTextures == null) {
            return;
        }

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
        GLES20.glViewport(0, 0, calcMips(BLUR_SIZE, 1), calcMips(BLUR_SIZE, 1));
        super.onDrawFrame();

        int previousTexture = mTextures[0];

        if (mFilterList != null) {
            int size = mFilterList.size();
            for (int i = 1; i < size; ++i) {
                BaseFilter filter = mFilterList.get(i);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[i]);
                filter.setTextureId(previousTexture);
                GLES20.glViewport(0, 0, calcMips(BLUR_SIZE, i + 1), calcMips(BLUR_SIZE, i + 1));
                filter.onDrawFrame();
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
                previousTexture = mTextures[i];
            }

            for (int i = size - 2; i > 0; --i) {
                BaseFilter filter = mFilterList.get(i);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[i]);
                filter.setTextureId(previousTexture);
                GLES20.glViewport(0, 0, calcMips(BLUR_SIZE, i + 1), calcMips(BLUR_SIZE, i + 1));
                filter.onDrawFrame();
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
                previousTexture = mTextures[i];
            }

            BaseFilter filter = mFilterList.get(0);
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            filter.setTextureId(previousTexture);
            GLES20.glViewport(0, 0, mViewWidth, mViewHeight);
            filter.onDrawFrame();
        }
    }

    @Override
    protected void initFrameBuffer() {
        if (mFilterList != null && mFilterList.size() > 0) {
            int size = mFilterList.size();
            mFrameBuffers = new int[size];
            mTextures = new int[size];

            for (int i = 0; i < size; i++) {
                GLES20.glGenFramebuffers(1, mFrameBuffers, i);
                GLES20.glGenTextures(1, mTextures, i);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[i]);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,
                        calcMips(BLUR_SIZE, i + 1), calcMips(BLUR_SIZE, i + 1), 0,
                        GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[i]);
                GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                        GLES20.GL_TEXTURE_2D, mTextures[i], 0);

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            }
        }
    }

    private int calcMips(int len, int level) {
        return len / (level + 1);
    }
}
