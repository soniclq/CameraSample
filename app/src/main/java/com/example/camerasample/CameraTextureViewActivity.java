package com.example.camerasample;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.TextureView;

import java.io.IOException;

/**
 * Created by liqian-ps on 2017/8/9.
 */

public class CameraTextureViewActivity extends Activity implements TextureView.SurfaceTextureListener{
    private TextureView textureView ;
    private Camera mCamera;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textureView = new TextureView(this);
        textureView.setSurfaceTextureListener(this);
        setContentView(textureView);
//        setContentView(R.layout.layout_surfaceview);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = Camera.open();
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
