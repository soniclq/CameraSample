package com.example.camerasample.widget;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import com.example.camerasample.camera.CameraRender;
import com.example.camerasample.camera.CommonHandlerListener;

import java.io.IOException;

public class CameraSurfaceView extends GLSurfaceView implements SurfaceTexture.OnFrameAvailableListener, CommonHandlerListener{
    public static final int CAMERA_SETUP = 1;
    private CameraRender mCameraRender;
    private Camera mCamera;
    private CommonHandlerListener commonHandlerListener;
    private CameraHandler cameraHandler;

    public CameraSurfaceView(Context context) {
        super(context);
        init();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    private void init(){
        cameraHandler = new CameraHandler(this);
        setEGLContextClientVersion(2);
        mCameraRender = new CameraRender(cameraHandler);

        setRenderer(mCameraRender);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case CameraHandler.CAMERA_SETUP:
                SurfaceTexture texture = (SurfaceTexture)msg.obj;
                texture.setOnFrameAvailableListener(this);
                openCamera(texture);
                // 相机显示旋转90
                mCamera.setDisplayOrientation(90);
                mCamera.startPreview();
                break;
            default:
                break;
        }
    }


    class CameraHandler extends Handler {
        public static final int CAMERA_SETUP = 1;
        private CommonHandlerListener listener;

        public CameraHandler(CommonHandlerListener handler) {
            listener = handler;
        }

        @Override
        public void handleMessage(Message msg) {
            listener.handleMessage(msg);
        }
    }



//    private Handler cameraHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
////            super.handleMessage(msg);
//            switch (msg.what) {
//                case CAMERA_SETUP:
//                    SurfaceTexture texture = (SurfaceTexture)msg.obj;
//                    texture.setOnFrameAvailableListener();
//                    openCamera(texture);
//                    mCamera.startPreview();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    private void openCamera(SurfaceTexture texture){
        mCamera = Camera.open();
        Camera.Parameters  parameters = mCamera.getParameters();
        parameters.setRotation(90);
        mCamera.setParameters(parameters);
        Camera.Size size = mCamera.getParameters().getPreviewSize();

        try {
            mCamera.setPreviewTexture(texture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        requestRender();
    }
}
