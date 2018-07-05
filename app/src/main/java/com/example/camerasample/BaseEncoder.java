package com.example.camerasample;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;

import java.nio.ByteBuffer;

/**
 * Created by liqian-ps on 2016/12/21.
 */

public interface BaseEncoder {
     void openCodec(MediaFormat foramt);
     void setCallback(MediaDataCallback callback);
     void sendData(ByteBuffer byteBuffer, MediaCodec.BufferInfo info);
     void closeCodec();
     Surface  getSurface();
}
