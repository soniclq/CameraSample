//
// Created by liqian-ps on 2017/2/27.
//

#ifndef CAMERASAMPLE_VIDEOENCODER_H
#define CAMERASAMPLE_VIDEOENCODER_H

#include<jni.h>
extern "C" {
#include <libavcodec/avcodec.h>
#include <libavutil/imgutils.h>
}
class VideoEncoder {
public:
    VideoEncoder();
    int open(int width, int height, int bitrate, int iGap);
    int encode(int8_t *src, uint8_t *dst, long pts);
private:
    AVCodecContext* codecContext;
    FILE* f;
};


#endif //CAMERASAMPLE_VIDEOENCODER_H
