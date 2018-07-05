package com.example.camerasample;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liqian-ps on 2016/12/20.
 */

public class HWAudioEncoder implements BaseEncoder {
    private MediaCodec mEncoder;
    private final String MIME_TYPE = MediaFormat.MIMETYPE_AUDIO_AAC;
    private BlockingQueue<Frame> inputQueue = new LinkedBlockingQueue<>(1024);
    private MediaDataCallback mCallBack;
    final  int TIMEOUT_USEC = 10000;
    private final static String TAG = HWAudioEncoder.class.getName();
    private FeedThread feedThread;
    private ProcessThread processThread;


    @Override
    public void openCodec(MediaFormat foramt) {
        try {
            mEncoder = MediaCodec.createEncoderByType(MIME_TYPE);
            mEncoder.configure(foramt, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        }catch (Exception e){
            LogUtils.i(TAG, "error "+e.toString());
        }
        mEncoder.start();

        feedThread = new FeedThread();
        feedThread.start();

        processThread = new ProcessThread();
        processThread.start();
    }



    @Override
    public Surface getSurface() {
        return null;
    }

    @Override
    public void closeCodec() {
        try {

            feedThread.join();
            processThread.join();
        }catch (InterruptedException e){

        }
    }

    @Override
    public void setCallback(MediaDataCallback callback) {
        mCallBack = callback;
    }

    @Override
    public void sendData(ByteBuffer byteBuffer, MediaCodec.BufferInfo info) {
        Frame frame = new Frame();

//        if((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM)== 0){
            frame.data = new byte[info.size];
            byteBuffer.position(info.offset);
            byteBuffer.get(frame.data);
//        }
        frame.bufferInfo = new MediaCodec.BufferInfo();
        frame.bufferInfo.flags = info.flags;
        frame.bufferInfo.size = info.size;
        frame.bufferInfo.presentationTimeUs = info.presentationTimeUs;
        frame.bufferInfo.offset = info.offset;

        frame.isEof = info.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM ? true:false;
        inputQueue.add(frame);
    }

    class FeedThread extends  Thread{
        @Override
        public void run() {

//            super.run();
            while (true) {
                try {
                    Frame frame = inputQueue.take();

                    process(frame.data, frame.bufferInfo, frame.isEof);
                    if (frame.isEof) {
                        break;
                    }
                } catch (InterruptedException e) {

                }
            }


        }
        private  void process(byte[] data, MediaCodec.BufferInfo bufferInfo, boolean isEof){

            while (true) {
                ByteBuffer[] inputBuffers = mEncoder.getInputBuffers();
                int bufferId = mEncoder.dequeueInputBuffer(TIMEOUT_USEC);
                if (bufferId >= 0) {
                    ByteBuffer byteBuffer = inputBuffers[bufferId];
                    byteBuffer.clear();
                    if (data.length > 0) {
                        byteBuffer.limit(data.length);
                        byteBuffer.put(data);
                    }
                    mEncoder.queueInputBuffer(bufferId, 0, data.length,
                            bufferInfo.presentationTimeUs, isEof ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : 0);
                }
                return ;
            }
        }
    }

    class ProcessThread extends Thread{
        @Override
        public void run() {
//            super.run();
            ByteBuffer[] outputBuffers = mEncoder.getOutputBuffers();
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            while(true) {
                int bufferId = mEncoder.dequeueOutputBuffer(bufferInfo, 0);
                LogUtils.i(TAG, ""+bufferInfo.flags);
                if (bufferId == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    outputBuffers = mEncoder.getInputBuffers();
                } else if (bufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    MediaFormat format = mEncoder.getOutputFormat();
                    if(mCallBack != null){
                        mCallBack.onMeidaFormatChange(format, Utils.TYPE_AUDIO);
                    }
                } else if (bufferId == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    continue;
                }
                else if (bufferId < 0) {

                } else {
                    ByteBuffer buffer = outputBuffers[bufferId];
                    if (mCallBack != null) {
                        mCallBack.onMediaData(buffer, bufferInfo, Utils.TYPE_AUDIO);
                    }
                    mEncoder.releaseOutputBuffer(bufferId, false);
                    if((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM )!= 0){
                        break;
                    }
                }
            }
        }
    }

    class Frame{
        public byte[] data;
//        public int pts;
        public MediaCodec.BufferInfo bufferInfo;
        boolean isEof;
    }
}
