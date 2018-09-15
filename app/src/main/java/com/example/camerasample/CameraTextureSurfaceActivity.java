package com.example.camerasample;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaMuxer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.camerasample.gl.FullFrameRect;
import com.example.camerasample.gl.WindowSurface;
import com.example.camerasample.widget.CameraSurfaceView;

/**
 * Created by liqian-ps on 2017/8/10.
 */

public class CameraTextureSurfaceActivity extends Activity implements View.OnClickListener {

    private final  static  String TAG = CameraTextureSurfaceActivity.class.getName();
//    private SurfaceView mSurfaceView;
    private CameraSurfaceView cameraSurfaceView;
    private WindowSurface mDisplaySurface;
    private FullFrameRect fullFrameRect;
    private int mTextureId;
    private SurfaceTexture mCameraSurfaceTexture;
    private Camera mCamera;


    private int[] mFrameBuffers = null;
    private int[] mFrameBufferTextures = null;

    int imgWidth;
    int imgHeight;

    private BaseEncoder mVideoEncoder;
    private WindowSurface mEncoderSurface;
    private boolean isEncodeVideo = true;

    private final int VIDEO_WIDTH = 1080;
    private final int VIDEO_HEIGHT = 1200;

    private Button btn;
    private Button changeBtn;

    private MediaMuxer mediaMuxer;
    private String mPath = "/sdcard/test3.mp4";
    private int videoTrack = -1;
    private int audioTrack = -1;
    private AudioCapture    mAudioCapture;
    private BaseEncoder    mAudioEncoder;
    private boolean muxterIsStarted = false;
    private boolean isStop = false;
    private long mStartTime = 0;
    private long mAudioFirstTime = Long.MIN_VALUE;
    private long mAudioCurrentTime = 0;
    private long timeTmp = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_surfaceview);
        cameraSurfaceView = (CameraSurfaceView) findViewById(R.id.surface_view);
//        mSurfaceView.getHolder().addCallback(this);
        btn = (Button)findViewById(R.id.btn_stop);
        changeBtn = (Button)findViewById(R.id.switch_filter);
        changeBtn.setOnClickListener(this);
        btn.setOnClickListener(this);

    }

//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        Surface surface = holder.getSurface();
//        EglCore core = new EglCore(null, EglCore.FLAG_RECORDABLE);
//        mDisplaySurface = new WindowSurface(core, surface, false);
//        mDisplaySurface.makeCurrent();
//
//
//        // 创建opengl program 时候没有egl  context 会报错 ，所以必须调makeCurrent
//        // E/libEGL: call to OpenGL ES API with no current context (logged once per thread)
//
//        fullFrameRect = new FullFrameRect(new Texture2dProgram(TEXTURE_EXT));
//        mTextureId = fullFrameRect.createTextureObject();
//        mCameraSurfaceTexture = new SurfaceTexture(mTextureId);
//        mCameraSurfaceTexture.setOnFrameAvailableListener(this);
//        mCamera = Camera.open();
//        Camera.Parameters  parameters = mCamera.getParameters();
//        parameters.setRotation(90);
//        mCamera.setParameters(parameters);
//        Camera.Size size = mCamera.getParameters().getPreviewSize();
//        imgWidth = size.width;
//        imgHeight = size.height;
//
//        try {
//            mCamera.setPreviewTexture(mCameraSurfaceTexture);
//            mCamera.startPreview();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//        try {
//            mediaMuxer = new MediaMuxer(mPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//
//        mStartTime = System.currentTimeMillis();
////        mVideoEncoder =  new HWVideoEncoder();
//        mVideoEncoder = new FFVideoEncoder();
//        mVideoEncoder.setCallback(this);
//        MediaFormat format = MediaFormat.createVideoFormat(Utils.VIDEO_MIME, VIDEO_WIDTH , VIDEO_HEIGHT);
//        format.setInteger(MediaFormat.KEY_BIT_RATE, 2 * 1024 * 1024);
//        format.setInteger(MediaFormat.KEY_FRAME_RATE, 25);
//        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
//                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
//        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2);
//        mVideoEncoder.openCodec(format);
////        mVideoEncoder.CreateEncoder(VIDEO_WIDTH, VIDEO_HEIGHT, 2 * 1024 * 1024, 25);
////        Surface encoderSurface = mVideoEncoder.getSurface();
////        mEncoderSurface = new WindowSurface(core, mVideoEncoder.getSurface(), true);
//        mAudioCapture = new AudioCapture();
//        mAudioEncoder = new FFAudioEncoder();
//        mAudioEncoder.setCallback(this);
//        MediaFormat aFormat = MediaFormat.createAudioFormat(Utils.AUDIO_MIME, 44100, 1);
//        aFormat.setInteger(MediaFormat.KEY_BIT_RATE, 48000);
//        aFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
//        aFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 100 * 1024);
//        mAudioEncoder.openCodec(aFormat);
//
//        mAudioCapture.setDataCallBack(new AudioCapture.AudioDataCallBack() {
//            @Override
//            public void OnCaptureData(byte[] buffer, boolean isEof) {
//                ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
//                MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
//                bufferInfo.offset = 0;
//                bufferInfo.size = buffer.length;
//                if(mAudioFirstTime == Long.MIN_VALUE){
//                    mAudioFirstTime = (System.currentTimeMillis() - mStartTime) * 1000;
//                    mAudioCurrentTime = mAudioFirstTime;
//                }
//                bufferInfo.presentationTimeUs = mAudioCurrentTime;
//                mAudioCurrentTime += bufferInfo.size * 1000 * 1000 / (44100 * 2);
//                LogUtils.i(TAG, "bufferInfo.presentationTimeUs" + bufferInfo.presentationTimeUs);
//                if(isEof) {
//                    bufferInfo.flags = MediaCodec.BUFFER_FLAG_END_OF_STREAM;
//                }else{
//                    bufferInfo.flags = 0;
//                }
//                mAudioEncoder.sendData(byteBuffer, bufferInfo);
//            }
//        });
//        mAudioCapture.start();
//
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        initFilter();
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//
//    }
//
//    @Override
//    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
//            Draw();
//    }
//    private void Draw(){
//        float [] matrix = new float[16];
//        mDisplaySurface.makeCurrent();
//        mCameraSurfaceTexture.updateTexImage();
//        mCameraSurfaceTexture.getTransformMatrix(matrix);
//        SurfaceView sv = (SurfaceView) findViewById(R.id.surface_view);
//        int viewWidth = sv.getWidth();
//        int viewHeight = sv.getHeight();
//        GLES20.glViewport(0, 0, viewWidth, viewHeight);
//
//        fullFrameRect.drawFrame(mTextureId, matrix);
//        ByteBuffer imgdata = readImagedata(viewWidth, viewHeight);
//
//
//        mDisplaySurface.swapBuffers();
//        if(isEncodeVideo){
////            File file= new File("/mnt/sdcard/test.rgba");
////            try {
////                FileOutputStream outputStream = new FileOutputStream(file);
//
////                imgdata.get(data);
////                outputStream.write(data);
////            }catch (IOException e){
////
////            }
////            isEncodeVideo = false;
//            byte[]  data = new byte[viewWidth * viewHeight * 4];
//            imgdata.get(data);
//            MediaCodec.BufferInfo bufferInfo =  new MediaCodec.BufferInfo();
////            bufferInfo.presentationTimeUs = (System.currentTimeMillis() - mStartTime) * 1000 ;
//            bufferInfo.presentationTimeUs = timeTmp++;
//            bufferInfo.size = data.length;
//            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
//            mVideoEncoder.sendData(byteBuffer, bufferInfo);
//
//
//
////            mEncoderSurface.makeCurrent();
////            GLES20.glViewport(0, 0 ,VIDEO_WIDTH, VIDEO_HEIGHT);
////            fullFrameRect.drawFrame(mTextureId, matrix);
////            mEncoderSurface.setPresentationTime((System.currentTimeMillis() - mStartTime) * 1000 * 1000);
////            mEncoderSurface.swapBuffers();
//        }
//
//
//    }
//
//    public static ByteBuffer readImagedata(int swidth, int sheight) {
//        ByteBuffer RGBABuffer = ByteBuffer.allocate(swidth * sheight*4);
//        RGBABuffer.order(ByteOrder.LITTLE_ENDIAN);
//        GLES20.glReadPixels(0, 0, swidth, sheight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, RGBABuffer);
//        RGBABuffer.rewind();
//        return RGBABuffer;
//    }
//
//    public void initFilter(){
//        mFrameBuffers = new int[1];
//        mFrameBufferTextures = new int[1];
//
//        GLES20.glGenFramebuffers(1, mFrameBuffers, 0);
//        GLES20.glGenTextures(1, mFrameBufferTextures, 0);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFrameBufferTextures[0]);
//        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, imgWidth, imgHeight, 0,
//                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
//                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
//                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
//                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
//                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
//        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
//                GLES20.GL_TEXTURE_2D, mFrameBufferTextures[0], 0);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
//    }
//
//    @Override
//    public void onMeidaFormatChange(MediaFormat format, int type) {
//        if(mediaMuxer == null){
//            return ;
//        }
//        try {
//            if (type == Utils.TYPE_VIDEO) {
//                videoTrack = mediaMuxer.addTrack(format);
////                mediaMuxer.start();
//            } else if (type == Utils.TYPE_AUDIO) {
//                audioTrack = mediaMuxer.addTrack(format);
//            }
//            if(videoTrack != -1 && audioTrack != -1){
//                mediaMuxer.start();
//                muxterIsStarted = true;
//            }
//        }catch (Exception e){
//            LogUtils.i(TAG, "ERROR "+e.toString());
//        }
//
//    }
//
//    @Override
//    public void onMediaData(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo, int type) {
//        if(muxterIsStarted) {
//            if (type == Utils.TYPE_VIDEO) {
//                mediaMuxer.writeSampleData(videoTrack, byteBuffer, bufferInfo);
//            } else {
//                mediaMuxer.writeSampleData(audioTrack, byteBuffer, bufferInfo);
//            }
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_stop:
                if(mVideoEncoder != null){
                    isEncodeVideo = false;
                    mVideoEncoder.closeCodec();
                    mAudioCapture.stop();
                    mAudioEncoder.closeCodec();
                    mediaMuxer.stop();
                    mediaMuxer.release();
                }
                break;
            case  R.id.switch_filter:
                if(cameraSurfaceView != null){
                    cameraSurfaceView.changeFilter();
                }
                break;
            default:
                break;
        }
    }
}
