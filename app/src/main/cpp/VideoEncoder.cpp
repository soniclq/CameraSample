//
// Created by liqian-ps on 2017/2/27.
//

#include "VideoEncoder.h"
#include "log.h"
extern "C" {
#include "libavutil/imgutils.h"
#include <libavutil/opt.h>
#include "libavutil/avstring.h"

#include "libyuv/convert.h"
}

#include <android/log.h>
#  define  D(x...)  __android_log_print(ANDROID_LOG_INFO, "native-lib", x)
static void ffmpeg_debug_log(void* ptr, int level, const char* fmt, va_list vl) {
    int prio = ANDROID_LOG_DEBUG;

    if (level <= AV_LOG_ERROR) prio = ANDROID_LOG_ERROR;
    else if (level <= AV_LOG_WARNING) prio = ANDROID_LOG_WARN;
    else if (level <= AV_LOG_INFO) prio = ANDROID_LOG_INFO;
    else if (level <= AV_LOG_VERBOSE) prio = ANDROID_LOG_DEBUG;
    else prio = ANDROID_LOG_DEBUG;

    static int print_prefix = 1;
    static int count;
    static char prev[1024];
    char line[1024];
    static int is_atty;
    AVClass *avc = ptr ? *(AVClass **) ptr : NULL;
    line[0] = 0;

    if (print_prefix && avc) {
        if (avc->parent_log_context_offset) {
            AVClass **parent = *(AVClass ***) (((uint8_t *) ptr) +
                                               avc->parent_log_context_offset);
            if (parent && *parent) {
                snprintf(line, sizeof(line), "[%s @ %p] ",
                         (*parent)->item_name(parent), parent);
            }
        }

        snprintf(line + strlen(line), sizeof(line) - strlen(line), "[%s @ %p] ",
                 avc->item_name(ptr), ptr);
    }

    vsnprintf(line + strlen(line), sizeof(line) - strlen(line), fmt, vl);
    print_prefix = strlen(line) && line[strlen(line) - 1] == '\n';
    if (print_prefix && !strncmp(line, prev, sizeof line)) {
        count++;
        if (is_atty == 1)
            fprintf(stderr, "    Last message repeated %d times\r", count);
        return;
    }

    if (count > 0) {
        fprintf(stderr, "    Last message repeated %d times\n", count);
        count = 0;
    }

    D(line);
    av_strlcpy(prev, line, sizeof(line));
}

VideoEncoder::VideoEncoder() {
    avcodec_register_all();
    av_log_set_callback(ffmpeg_debug_log);
}

int VideoEncoder::open(int width, int height, int bitrate, int iGap) {

    AVCodec *codec = avcodec_find_encoder(AV_CODEC_ID_H264);

    this->codecContext = avcodec_alloc_context3(codec);
    codecContext->width = width;
    codecContext->height = height;
    codecContext->bit_rate = 400000;
    codecContext->codec_type = AVMEDIA_TYPE_VIDEO;
    codecContext->pix_fmt = AV_PIX_FMT_YUV420P;
    codecContext->time_base = (AVRational){1,25};
//    codecContext->qmin = 10;
//    codecContext->qmax = 51;
    codecContext->gop_size = 10;
//    codecContext->bit_rate = bitrate;
    codecContext->max_b_frames = 1;
    av_opt_set(codecContext->priv_data, "preset", "slow", 0);

//    AVDictionary *param ;
////    av_dict_set(&param, "preset", "slow", 0);
//    av_dict_set(&param, "tune", "zerolatency", 0);

    int ret = avcodec_open2(codecContext, codec, NULL);
    if(ret < 0){
        LOGD("avcodec_open2 open error %s", av_err2str(ret));
    }

    this->f = fopen("/sdcard/test.h264", "wb");

    return  ret;

}

static int debug = 1;


int VideoEncoder::encode(int8_t *src, uint8_t *dst, long pts) {
    AVFrame *frame = av_frame_alloc();
    AVPacket packet ;
    int got_picture = 0;
    int ret = 0;

    int width = this->codecContext->width;
    int height = this->codecContext->height;

    frame->width = width;
    frame->height = height;
    frame->format = this->codecContext->pix_fmt;
    av_image_alloc(frame->data, frame->linesize, width, height, codecContext->pix_fmt, 1);
    av_init_packet(&packet);
    packet.data = NULL;
    packet.size = 0;
    libyuv::ABGRToI420((uint8_t*)src, width * 4, frame->data[0], width, frame->data[1], width / 2, frame->data[2], width /2 , width, height);


//    frame->data[0] = src ;
//    frame->data[1] = src + width * height;
//    frame->data[2] = src + width * height * 2;

    if(debug){
        FILE *yuv = fopen("/sdcard/test.yuv", "wb");
        fwrite(frame->data[0], width * height, 1, yuv);
        fwrite(frame->data[1], width * height / 4 , 1, yuv);
        fwrite(frame->data[2], width * height / 4, 1 , yuv);
        fclose(yuv);
        debug = 0;
    }
    frame->pts = pts;
//    int picture_size = avpicture_get_size(this->codecContext->pix_fmt, width, height);
//    int got_picture = 0;
//    picture_buf = av_m

    ret = avcodec_encode_video2(this->codecContext, &packet, frame, &got_picture);
    LOGD("encode video ret %d %s got %d", ret, av_err2str(ret), got_picture);
    if(got_picture){
        LOGD("encode video size %d", packet.size);
        fwrite(packet.data, 1, packet.size, f);
        av_free_packet(&packet);
    }

    return 0;
}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_camerasample_FFVideoEncoder_createEncoder(JNIEnv *env, jobject instance) {

// TODO
    VideoEncoder *videoEncoder = new VideoEncoder();
    return  (jlong)videoEncoder;
}



extern "C"
JNIEXPORT void JNICALL
Java_com_example_camerasample_FFVideoEncoder_openCodec__JIIII(JNIEnv *env, jobject instance,
                                                              jlong handle, jint width, jint height,
                                                              jint bitrate, jint igap) {

    // TODO

    VideoEncoder* videoEncoder = (VideoEncoder*)handle;
    videoEncoder->open(width, height, bitrate, igap);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_camerasample_FFVideoEncoder_encodeFrame(JNIEnv *env, jobject instance,
                                                         jlong handle, jbyteArray src_, jlong pts, jint len) {
//    jbyte *src = env->GetByteArrayElements(src_, NULL);
    unsigned  char src[len];
    env->GetByteArrayRegion(src_, 0, len, (jbyte*)src);
//    jbyte *src = env->GetByteArrayRegion(src_, 0, )
//    env->GetByteArrayRegion()
    // TODO
    VideoEncoder* videoEncoder = (VideoEncoder*)handle;
    videoEncoder->encode((int8_t *)src, NULL, pts);

//    env->ReleaseByteArrayElements(src_, src, 0);
}