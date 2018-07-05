//
// Created by liqian-ps on 2017/2/6.
//

#ifndef CAMERASAMPLE_RENDERVIDEO_H
#define CAMERASAMPLE_RENDERVIDEO_H

#include <EGL/egl.h>
#include <EGL/eglext.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <android/native_window.h>

class VideoRender
{
public:
    VideoRender();
    int init(EGLNativeWindowType nativeWindow);
};

#endif //CAMERASAMPLE_RENDERVIDEO_H
