package com.example.camerasample;

import android.media.MediaCodec;
import android.media.MediaFormat;

import java.nio.ByteBuffer;

/**
 * Created by liqian-ps on 2016/12/21.
 */

public interface MediaDataCallback {
    public  void onMeidaFormatChange(MediaFormat format, int type);
    public  void onMediaData(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo, int type);
}
