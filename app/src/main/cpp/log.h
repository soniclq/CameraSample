//
// Created by liqian-ps on 2017/2/6.
//

#ifndef CAMERASAMPLE_LOG_H
#define CAMERASAMPLE_LOG_H

#include <android/log.h>
#define LOG_TAG  "native-lib"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

#endif //CAMERASAMPLE_LOG_H
