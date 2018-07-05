package com.example.camerasample;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * Created by liqian-ps on 2017/1/25.
 */

public class FFAudioEncoder implements BaseEncoder {
    private long mHandle = 0;
    private boolean debug = true;
    private MediaDataCallback callback;
    @Override
    public void sendData(ByteBuffer byteBuffer, MediaCodec.BufferInfo info) {
        byte[] src = new byte[info.size];
        byteBuffer.get(src);
        byte [] dst = encode(mHandle, src, info.size);

        if(debug){
            File file = new File("/sdcard/test.aac");

        }

        ByteBuffer out = ByteBuffer.wrap(dst);
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        bufferInfo.size = dst.length;
        bufferInfo.flags = info.flags;
        bufferInfo.presentationTimeUs = info.presentationTimeUs;
        if(callback != null){
            callback.onMediaData(out, bufferInfo, Utils.TYPE_AUDIO);
        }
    }

    @Override
    public void openCodec(MediaFormat foramt) {

        mHandle = createEncoder();
        int bitrate = 64 * 1000;
        int sampleRate = 44100;
        int channel = 1;
        if(foramt.containsKey(MediaFormat.KEY_BIT_RATE)){
            bitrate = foramt.getInteger(MediaFormat.KEY_BIT_RATE);
        }
        if(foramt.containsKey(MediaFormat.KEY_SAMPLE_RATE)){
            sampleRate = foramt.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        }
        if(foramt.containsKey(MediaFormat.KEY_CHANNEL_COUNT)){
            channel = foramt.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
        }

        open(mHandle, sampleRate, bitrate, channel);

        byte[] csd = getAudioCsd0(mHandle);
        MediaFormat audioFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, sampleRate, channel);
        ByteBuffer csd0 = ByteBuffer.wrap(csd);
        audioFormat.setByteBuffer("csd-0", csd0);
        callback.onMeidaFormatChange(audioFormat, Utils.TYPE_AUDIO);
    }

    @Override
    public void setCallback(MediaDataCallback callback) {
        this.callback = callback;
    }

    @Override
    public Surface getSurface() {
        return null;
    }

    @Override
    public void closeCodec() {

    }

    class ProcessThread extends Thread{
        @Override
        public void run() {

//            super.run();
        }
    }


    private native long createEncoder();
    public native void  open(long handle, int sampleRate, int bitrate, int channel);
    private native byte[] encode(long handle, byte[] srcData, int srcLen);
    private native byte[] getAudioCsd0(long handle);
    private native void closeEncoder(long handle);

    static {
        System.loadLibrary("native-lib");
    }
}
