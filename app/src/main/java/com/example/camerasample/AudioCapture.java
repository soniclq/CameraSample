package com.example.camerasample;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioSource;
/**
 * Created by liqian-ps on 2016/12/18.
 */

public class AudioCapture {
    private AudioRecord mRecord;
    private boolean isEof =false;
    private byte[] readData = null;
    private AudioDataCallBack mCallBack = null;
    private CaptureThread captureThread;
    public AudioCapture(){
        int miniBuffer = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        mRecord = new AudioRecord(AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, miniBuffer * 3);
        readData = new byte[miniBuffer];
    }


    public void start(){
        captureThread = new CaptureThread();
        captureThread.start();
    }
    public void stop(){

        isEof = true;
        try {
            captureThread.join();
            mRecord.stop();
        }catch (InterruptedException e){

        }
    }


    public interface  AudioDataCallBack{
        public void OnCaptureData(byte[] buffer, boolean isEof);
    }

    public void setDataCallBack(AudioDataCallBack callBack){
        this.mCallBack = callBack;
    }


    public class CaptureThread extends Thread{
        @Override
        public void run() {
//            super.run();
            mRecord.startRecording();

            do{
                mRecord.read(readData, 0, readData.length);
                if(mCallBack != null){
                    mCallBack.OnCaptureData(readData, isEof);
                }
            }while (!isEof);
        }
    }

}
