package com.example.camerasample.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.example.camerasample.camera.CameraRender;

public class CameraSurfaceView extends GLSurfaceView {
    private CameraRender mCameraRender;

    public CameraSurfaceView(Context context) {
        super(context);
        init();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    private void init(){
        setEGLContextClientVersion(2);
        mCameraRender = new CameraRender();

        setRenderer(mCameraRender);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
}
