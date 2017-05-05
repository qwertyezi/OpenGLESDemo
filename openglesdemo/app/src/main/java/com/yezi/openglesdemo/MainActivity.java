package com.yezi.openglesdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onImageViewClick(View view) {
        startActivity(new Intent(this, TestImageGLActivity.class));
    }

    public void onVideoViewClick(View view) {
        startActivity(new Intent(this, TestVideoGLActivity.class));
    }

    public void onCameraViewClick(View view) {
        startActivity(new Intent(this, TestCameraGLActivity.class));
    }

}
