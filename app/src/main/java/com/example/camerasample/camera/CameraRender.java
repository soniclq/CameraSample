package com.example.camerasample.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;

import com.example.camerasample.filters.BaseFilter;
import com.example.camerasample.filters.LutColorFilter;
import com.example.camerasample.gl.FullFrameRect;
import com.example.camerasample.gl.GlUtil;
import com.example.camerasample.widget.CameraSurfaceView;

import java.io.IOException;

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
    private boolean changeFilter = false;

    public CameraRender(Context context, Handler handler){
        mHandler = handler;
        mContext = context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
//        fullFrameRect = new FullFrameRect(new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_EXT));
//
//        mTextureId = fullFrameRect.createTextureObject();
        filter = new LutColorFilter(mContext);
        mTextureId = createTextureObject();
        mSurfaceTexture = new SurfaceTexture(mTextureId);


        try {
            Bitmap bitmap = BitmapFactory.decodeStream(mContext.getResources().getAssets().open("nt_L6_1.png"));
            filter.setBitmap(bitmap);
        }catch (IOException e){

        }

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
        if(changeFilter){
            filter.releaseProgram();
            filter = new BaseFilter(mContext);
            changeFilter = false;
        }

    }

    public void changeFilter(){
        changeFilter = true;
    }

    private int createTextureObject(){
        int[] textid = new int[1];
        GLES20.glGenTextures(1, textid, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textid[0]);

        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);
        GlUtil.checkGlError("glTexParameter");

        return textid[0];
    }

}
