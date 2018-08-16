package com.example.camerasample.camera;

import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.os.Handler;

import com.example.camerasample.gl.FullFrameRect;
import com.example.camerasample.gl.Texture2dProgram;
import com.example.camerasample.widget.CameraSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRender implements GLSurfaceView.Renderer {

    private SurfaceTexture mSurfaceTexture;
    private FullFrameRect fullFrameRect;
    private Handler mHandler ;
    private int mTextureId;

    public CameraRender(Handler handler){
        mHandler = handler;
    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        fullFrameRect = new FullFrameRect(new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_EXT));

        mTextureId = fullFrameRect.createTextureObject();
        mSurfaceTexture = new SurfaceTexture(mTextureId);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        mHandler.sendMessage(mHandler.obtainMessage(CameraSurfaceView.CAMERA_SETUP, width, height, mSurfaceTexture));
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        float [] matrix = new float[16];
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(matrix);
        fullFrameRect.drawFrame(mTextureId, matrix);

    }
}
