package com.example.camerasample.filters;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.camerasample.R;
import com.example.camerasample.gl.Drawable2d;
import com.example.camerasample.gl.GlUtil;

import java.nio.FloatBuffer;

public class BaseFilter {

    protected  int mFilterProgram;

    private int maPositionLoc;
    private int muMVPMatrixLoc;
    private int maTextureCoordLoc;
    private int mTextureLoc;
    private int muTexMatrixLoc;

    private float[] mTexMatrix;
    private float[] mMvpMatrix;

    private int mTextureTarget;

    private Drawable2d drawable2d;
//    protected  int[] m


    public BaseFilter(Context context){
        mFilterProgram = createProgram(context, getVertexShaderId(), getFragmentShaderId());

        mTexMatrix = new float[16];
        Matrix.setIdentityM(mTexMatrix, 0);

        mMvpMatrix = new float[16];
        Matrix.setIdentityM(mMvpMatrix, 0);
        mTextureTarget = GLES11Ext.GL_TEXTURE_EXTERNAL_OES;

        drawable2d = new Drawable2d(Drawable2d.Prefab.FULL_RECTANGLE);
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

    public void draw(int cameraId, int textureId, FloatBuffer vertexBuffer, FloatBuffer textureBuffer){
        GLES20.glUseProgram(mFilterProgram);
        onBindValue(GLES20.GL_TEXTURE_2D, textureId, vertexBuffer, textureBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    protected void onBindValue(int target, int textureId, FloatBuffer vertexBuffer, FloatBuffer textureBuffer){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(target, textureId);
        GLES20.glUniform1i(mTextureLoc, 0);

        GLES20.glEnableVertexAttribArray(maPositionLoc);
        GLES20.glVertexAttribPointer(maPositionLoc, 2, GLES20.GL_FLOAT, false, 4* 2, vertexBuffer );

        GLES20.glEnableVertexAttribArray(maTextureCoordLoc);
        GLES20.glVertexAttribPointer(maTextureCoordLoc, 2, GLES20.GL_FLOAT, false, 4* 2, textureBuffer);


        GLES20.glUniformMatrix4fv(muMVPMatrixLoc, 1,false, mMvpMatrix, 0);
        GLES20.glUniformMatrix4fv(muTexMatrixLoc, 1, false, mTexMatrix, 0);
    }


    /**
     * Creates a texture object suitable for use with this program.
     * <p>
     * On exit, the texture will be bound.
     */
    public int createTextureObject() {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        GlUtil.checkGlError("glGenTextures");

        int texId = textures[0];
        GLES20.glBindTexture(mTextureTarget, texId);
        GlUtil.checkGlError("glBindTexture " + texId);

        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);
        GlUtil.checkGlError("glTexParameter");

        return texId;
    }

    public FloatBuffer getVertexBuffer(){
        return drawable2d.getVertexArray();
    }

    public FloatBuffer getTextrueBuffer(){
        return  drawable2d.getTexCoordArray();
    }
}
