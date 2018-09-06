package com.example.camerasample.filters;

import android.content.Context;
import android.opengl.GLES20;

import com.example.camerasample.R;
import com.example.camerasample.gl.GlUtil;

import java.nio.FloatBuffer;

public class BaseFilter {

    protected  int mFilterProgram;

    private int maPositionLoc;
    private int muMVPMatrixLoc;
    private int maTextureCoordLoc;
    private int mTextureLoc;
    private int muTexMatrixLoc;
//    protected  int[] m


    public BaseFilter(Context context){
        mFilterProgram = createProgram(context, getVertexShaderId(), getFragmentShaderId());
        getGLSLValues();
    }

    protected  int getVertexShaderId(){
        return R.raw.vertex_shader_base;
    }

    protected  int getFragmentShaderId(){
        return R.raw.fragment_shader_base;
    }

    int createProgram(Context context, int vertexId, int fragmentId){
        int program;
        program = GlUtil.createProgram(context, vertexId, fragmentId);
        return  program;
    }


    protected void getGLSLValues() {
        mTextureLoc = GLES20.glGetUniformLocation(mFilterProgram, "inputImageTexture");
        maPositionLoc = GLES20.glGetAttribLocation(mFilterProgram, "aPosition");
        muMVPMatrixLoc = GLES20.glGetUniformLocation(mFilterProgram, "uMVPMatrix");
        maTextureCoordLoc = GLES20.glGetAttribLocation(mFilterProgram, "aTextureCoord");
        muTexMatrixLoc = GLES20.glGetUniformLocation(mFilterProgram, "uTexMatrix");
    }

    public void draw(int cameraId, int textureId){
        GLES20.glUseProgram(mFilterProgram);


    }

    protected void onBindValue(int target, int textureId, FloatBuffer vertexBuffer, FloatBuffer textureBuffer){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(target, textureId);
        GLES20.glUniform1i(mTextureLoc, 0);

        GLES20.glEnableVertexAttribArray(maPositionLoc);
        GLES20.glVertexAttribPointer(maPositionLoc, 2, GLES20.GL_FLOAT, false, 4* 2, vertexBuffer );

        GLES20.glEnableVertexAttribArray(maTextureCoordLoc);
        GLES20.glVertexAttribPointer(maTextureCoordLoc, 2, GLES20.GL_FLOAT, false, 4* 2, textureBuffer);
    }
}
