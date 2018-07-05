package com.example.camerasample;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by liqian-ps on 2016/12/13.
 */

public class HWVideoEncoder implements BaseEncoder{
    private Surface mInputSurface;
    private MediaCodec mEncoder;
    private EncodeThread thread;
    private MediaDataCallback mCallBack;
//    private static final String MIME_TYPE = "video/avc";

    public void CreateEncoder(int width, int height, int bitrate, int fps){

        MediaFormat mediaFormat = MediaFormat.createVideoFormat(Utils.VIDEO_MIME, width, height);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 2 * 1024 * 1024);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, fps);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2);
        try {
            mEncoder = MediaCodec.createEncoderByType(Utils.VIDEO_MIME);
            mEncoder.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mInputSurface = mEncoder.createInputSurface();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mEncoder.start();

        thread = new EncodeThread();
        thread.start();
    }

    @Override
    public  Surface getSurface(){
        return mInputSurface;
    }

    @Override
    public void openCodec(MediaFormat foramt) {
        try {
            mEncoder = MediaCodec.createEncoderByType(Utils.VIDEO_MIME);
            mEncoder.configure(foramt, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mInputSurface = mEncoder.createInputSurface();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mEncoder.start();

        thread = new EncodeThread();
        thread.start();
    }

    @Override
    public void setCallback(MediaDataCallback callback) {
        mCallBack = callback;
    }

    class EncodeThread extends Thread{
        @Override
        public void run() {

            ByteBuffer[] outputBuffers = mEncoder.getOutputBuffers();
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            while(true) {
                int bufferId = mEncoder.dequeueOutputBuffer(bufferInfo, 0);
                if (bufferId == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    outputBuffers = mEncoder.getOutputBuffers();
                } else if (bufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    MediaFormat format = mEncoder.getOutputFormat();
                        if(mCallBack != null){
                            mCallBack.onMeidaFormatChange(format, Utils.TYPE_VIDEO);
                        }
                } else if (bufferId == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    continue;
                }
                else if (bufferId < 0) {

                } else {
                    ByteBuffer buffer = outputBuffers[bufferId];
                    if (mCallBack != null) {
                        mCallBack.onMediaData(buffer, bufferInfo, Utils.TYPE_VIDEO);
                    }
                    mEncoder.releaseOutputBuffer(bufferId, false);
                    if((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM )!= 0){
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void sendData(ByteBuffer byteBuffer, MediaCodec.BufferInfo info) {

    }

    //    public void setCallback(MediaDataCallback callback){
//        mCallBack = callback;
//    }
//
//    public interface MediaDataCallback{
//        public  void onMeidaFormatChange(MediaFormat format);
//        public  void onMediaData(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo);
//    }


//    public void stop(){
//        if(mEncoder != null){
//            mEncoder.signalEndOfInputStream();
//        }
//        try {
//            thread.join();
//        }catch (InterruptedException e){
//
//        }
//
//
//    }

    @Override
    public void closeCodec() {
        if(mEncoder != null){
            mEncoder.signalEndOfInputStream();
        }
        try {
            thread.join();
        }catch (InterruptedException e){

        }
    }
}
