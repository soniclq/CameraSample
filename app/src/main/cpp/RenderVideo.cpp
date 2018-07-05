//
// Created by liqian-ps on 2017/2/6.
//
#include "RenderVideo.h"
#include "log.h"
#include <stdlib.h>

VideoRender::VideoRender() {

}

int VideoRender::init(EGLNativeWindowType nativeWindow) {
    EGLDisplay display = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    EGLint num_configs;
    EGLConfig configs_list;

    if (display == EGL_NO_DISPLAY) {
        LOGD("getDisplay() err %d", eglGetError());
        return -1;
    }
    if (!eglInitialize(display, 0, 0)) {
        LOGD("eglInitialize() err %d", eglGetError());
        return -2;
    }

    const EGLint attribs[] = {
            EGL_BUFFER_SIZE, 32,
            EGL_ALPHA_SIZE, 8,
            EGL_BLUE_SIZE, 8,
            EGL_GREEN_SIZE, 8,
            EGL_RED_SIZE, 8,
            EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
            EGL_SURFACE_TYPE, EGL_WINDOW_BIT,
            EGL_NONE
    };

    const EGLint contextAttribs[] =
            {
                    EGL_CONTEXT_CLIENT_VERSION, 2,
                    EGL_NONE
            };

    EGLConfig config;
    EGLint numConfigs;
    if(!eglChooseConfig(display, attribs, &config, 1,
                        &numConfigs)) {
        return EGL_FALSE;
    }

    EGLContext context;
    context = eglCreateContext(display, config, EGL_NO_CONTEXT,
                               contextAttribs);
    if(context == EGL_NO_CONTEXT)
    {
        return EGL_FALSE;
    }

    EGLSurface surface;
    surface = eglCreateWindowSurface(display, config, nativeWindow, NULL);
    if(surface == EGL_NO_SURFACE)
    {
        return EGL_FALSE;
    }

}