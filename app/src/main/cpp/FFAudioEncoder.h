//
// Created by liqian-ps on 2017/1/25.
//

#ifndef CAMERASAMPLE_FFAUDIOENCODER_H_H
#define CAMERASAMPLE_FFAUDIOENCODER_H_H

extern "C" {
#include <libavformat/avformat.h>
#include <libavcodec/avcodec.h>
#include <libavutil/opt.h>
}
class AudioEncoder{
private:
        AVCodecContext* codecContext;
public:
    AudioEncoder();
    int open(int sampleRate, int bitrate, int channles);
    int encode(uint8_t* data, int src_len, uint8_t** out, int* out_len);
    int getCSD0(uint8_t** data, int* len);

    int close();

};

#endif //CAMERASAMPLE_FFAUDIOENCODER_H_H
