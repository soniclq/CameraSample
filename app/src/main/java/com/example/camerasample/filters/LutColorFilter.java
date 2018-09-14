package com.example.camerasample.filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.example.camerasample.R;
import com.example.camerasample.gl.GlUtil;

import java.nio.FloatBuffer;

public class LutColorFilter extends BaseFilter {

    protected int inputTextureId;
    protected int inputImageTexture2Location;
    protected int ratioLocation;
    private float mRatio = 1f;


    public LutColorFilter(Context context) {
        super(context);

        ratioLocation = GLES20.glGetUniformLocation(mFilterProgram, "ratio");
    }

    public void setBitmap(Bitmap bitmap){
        inputImageTexture2Location = GLES20.glGetUniformLocation(mFilterProgram, "inputImageTexture2");

        inputTextureId = GlUtil.createTexture(GLES20.GL_TEXTURE_2D, bitmap);
    }

    protected  int getFragmentShaderId(){
        return R.raw.fragment_shader_lut;
    }


    public void draw(int textureTarget, int textureId, FloatBuffer vertexBuffer, FloatBuffer textureBuffer , float[] textMatrix, int screen_width, int screen_height){
        GLES20.glUseProgram(mFilterProgram);
        bindGLValue(mTextureTarget, textureId, vertexBuffer, textureBuffer, textMatrix);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureId);
        GLES20.glUniform1i(inputImageTexture2Location, 1);

        GLES20.glUniform1f(ratioLocation, mRatio);

        GLES20.glViewport(0,0, screen_width, screen_height);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
