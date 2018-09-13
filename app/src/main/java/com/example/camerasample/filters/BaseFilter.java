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
    protected  int aPositionLocation;
    protected  int aTextureCoordLocation;
    protected  int uTexMatrixLocation;
    protected  int uMVPMatrixLocation;
    protected  int inputImageTexture;
    float [] mMvpMatrix;
    protected Drawable2d drawable2d;

    protected int mTextureId;
    protected int mTextureTarget;

   public BaseFilter(Context context){
       mFilterProgram = createProgram(context);
       mMvpMatrix = new float[16];
       Matrix.setIdentityM(mMvpMatrix, 0);
       drawable2d = new Drawable2d(Drawable2d.Prefab.FULL_RECTANGLE);
       getGLValue();

       mTextureTarget = GLES11Ext.GL_TEXTURE_EXTERNAL_OES;


   }

   private int createProgram(Context context){
       int program = GlUtil.createProgram(context, getVertexShaderId(), getFragmentShaderId());
       return program;
   }

   protected int getVertexShaderId(){
       return R.raw.vertex_shader_base;
   }

   protected  int getFragmentShaderId(){
       return R.raw.fragment_shader_base;
   }

   private void getGLValue(){
       aPositionLocation = GLES20.glGetAttribLocation(mFilterProgram, "aPosition");
       aTextureCoordLocation = GLES20.glGetAttribLocation(mFilterProgram, "aTextureCoord");

       uMVPMatrixLocation = GLES20.glGetUniformLocation(mFilterProgram, "uMVPMatrix");
       uTexMatrixLocation = GLES20.glGetUniformLocation(mFilterProgram, "uTexMatrix");
       inputImageTexture = GLES20.glGetUniformLocation(mFilterProgram, "inputImageTexture");
   }

   public void draw(int textureTarget, int textureId,  FloatBuffer vertexBuffer, FloatBuffer textureBuffer ,float[] textMatrix, int screen_width, int screen_height){
       GLES20.glUseProgram(mFilterProgram);
       bindGLValue(mTextureTarget, textureId, vertexBuffer, textureBuffer, textMatrix);

       GLES20.glViewport(0,0, screen_width, screen_height);
       GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
   }

   protected void bindGLValue(int targetTarget, int targetId, FloatBuffer vertexBuffer, FloatBuffer textureBuffer, float[] textMatrix){

       GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
       GLES20.glBindTexture(targetTarget, targetId);
       GLES20.glUniform1i(inputImageTexture, 0);

       GLES20.glEnableVertexAttribArray(aPositionLocation);
       GLES20.glVertexAttribPointer(aPositionLocation, 2, GLES20.GL_FLOAT, false, 4 * 2, vertexBuffer);

       GLES20.glEnableVertexAttribArray(aTextureCoordLocation);
       GLES20.glVertexAttribPointer(aTextureCoordLocation, 2, GLES20.GL_FLOAT, false, 4 * 2,  textureBuffer);

       GLES20.glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mMvpMatrix, 0);
       GLES20.glUniformMatrix4fv(uTexMatrixLocation, 1, false, textMatrix, 0);

   }

   public FloatBuffer getVertexBuffer(){
       return drawable2d.getVertexArray();
   }

   public FloatBuffer getTextrueBuffer(){
       return drawable2d.getTexCoordArray();
   }

   public int createTextureObject(){
       int[] textid = new int[1];
       GLES20.glGenTextures(1, textid, 0);
       GLES20.glBindTexture(mTextureTarget, textid[0]);

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
