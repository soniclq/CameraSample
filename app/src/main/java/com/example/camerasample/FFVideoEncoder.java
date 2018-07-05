package com.example.camerasample;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;

import java.nio.ByteBuffer;

/**
 * Created by liqian-ps on 2017/2/27.
 */

public class FFVideoEncoder implements BaseEncoder {

    private long mHandle;

    @Override
    public void openCodec(MediaFormat foramt) {
            mHandle = createEncoder();
            int width = 0;
            int height = 0;
            int bitrate = 2 * 1024 * 1024;
            int iGap = 1;
            if(foramt.containsKey(MediaFormat.KEY_WIDTH)){
                width = foramt.getInteger(MediaFormat.KEY_WIDTH);
            }

            if(foramt.containsKey(MediaFormat.KEY_HEIGHT)){
                height = foramt.getInteger(MediaFormat.KEY_HEIGHT);
            }

            if(foramt.containsKey(MediaFormat.KEY_BIT_RATE)){
                bitrate = foramt.getInteger(MediaFormat.KEY_BIT_RATE);
            }
            if(foramt.containsKey(MediaFormat.KEY_I_FRAME_INTERVAL)){
                iGap = foramt.getInteger(MediaFormat.KEY_I_FRAME_INTERVAL);
            }

            openCodec(mHandle, width, height, bitrate, iGap);
    }

    @Override
    public void setCallback(MediaDataCallback callback) {

    }

    @Override
    public void sendData(ByteBuffer byteBuffer, MediaCodec.BufferInfo info) {
        byte[] src = new byte[info.size];
        byteBuffer.get(src);
        encodeFrame(mHandle, src, info.presentationTimeUs, src.length);

//        if(debug){
//            File file = new File("/sdcard/test.aac");
//
//        }
//
//        ByteBuffer out = ByteBuffer.wrap(dst);
//        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
//        bufferInfo.size = dst.length;
//        bufferInfo.flags = info.flags;
//        bufferInfo.presentationTimeUs = info.presentationTimeUs;
    }

    @Override
    public void closeCodec() {

    }

    @Override
    public Surface getSurface() {
        return null;
    }

    private native  long createEncoder();
    private native void openCodec(long handle, int width, int height, int bitrate, int igap);
    private native void encodeFrame(long handle, byte[] src, long pts, int length);
//    private native void close(long handle);

    static {
        System.loadLibrary("native-lib");
    }
}
