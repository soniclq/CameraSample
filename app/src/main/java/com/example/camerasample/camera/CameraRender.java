package com.example.camerasample.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.os.Handler;

import com.example.camerasample.filters.BaseFilter;
import com.example.camerasample.gl.FullFrameRect;
import com.example.camerasample.widget.CameraSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraRender implements GLSurfaceView.Renderer {

    private SurfaceTexture mSurfaceTexture;
    private FullFrameRect fullFrameRect;
    private Handler mHandler ;
    private int mTextureId;
    private Context mContext;
    private BaseFilter filter;
    private int mScreen_width;
    private int mScreen_height;

    public CameraRender(Context context, Handler handler){
        mHandler = handler;
        mContext = context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
//        fullFrameRect = new FullFrameRect(new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_EXT));
//
//        mTextureId = fullFrameRect.createTextureObject();
        filter = new BaseFilter(mContext);
        mTextureId = filter.createTextureObject();
        mSurfaceTexture = new SurfaceTexture(mTextureId);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        mHandler.sendMessage(mHandler.obtainMessage(CameraSurfaceView.CAMERA_SETUP, width, height, mSurfaceTexture));
        mScreen_width = width;
        mScreen_height = height;
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        float [] texmatrix = new float[16];
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(texmatrix);


        filter.draw(0, mTextureId, filter.getVertexBuffer(), filter.getTextrueBuffer(), texmatrix, mScreen_width, mScreen_height);
//        fullFrameRect.drawFrame(mTextureId, texmatrix);

    }


}
