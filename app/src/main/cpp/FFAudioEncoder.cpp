//
// Created by liqian-ps on 2017/1/25.
//

#include <jni.h>
#include "FFAudioEncoder.h"

AudioEncoder::AudioEncoder() {
    avcodec_register_all();
}


int AudioEncoder::open(int sampleRate, int bitrate, int channels) {
    int ret = 0;
    AVCodec *id = avcodec_find_encoder(AV_CODEC_ID_AAC);
    if(id == NULL){
        return -1;
    }

    this->codecContext = avcodec_alloc_context3(id);
    this->codecContext->sample_rate = sampleRate;
    this->codecContext->sample_fmt = AV_SAMPLE_FMT_S16;
    this->codecContext->bit_rate = bitrate;
    this->codecContext->channels = channels;
    this->codecContext->flags |= AV_CODEC_FLAG_GLOBAL_HEADER;

    ret = avcodec_open2(this->codecContext, id, NULL);
    if (ret < 0) {
        return -1;
    }
    return ret;
}

int AudioEncoder::encode(uint8_t *data, int src_len, uint8_t **out, int* out_len) {
    AVPacket avPacket = {0};
    AVFrame* frame ;
    int got_frame_ptr = 0 ;
    int ret = 0;

    av_init_packet(&avPacket);

    frame = av_frame_alloc();
//    memset(&frame, sizeof(frame), 0);
    frame->nb_samples = this->codecContext->frame_size;
    frame->channels = this->codecContext->channels;
    frame->format = this->codecContext->sample_fmt;
    avcodec_fill_audio_frame(frame, this->codecContext->channels, this->codecContext->sample_fmt, data, src_len, 1);

    ret = avcodec_encode_audio2(this->codecContext, &avPacket, frame, &got_frame_ptr);

    if(got_frame_ptr == 1){
        *out = (uint8_t*)av_malloc(avPacket.size);
//        out = avPacket.data;
        memcpy(*out, avPacket.data, avPacket.size);
        *out_len = avPacket.size;
    }
    return 0;
}

int AudioEncoder::getCSD0(uint8_t **data, int *len) {
    *data = this->codecContext->extradata;
    *len = this->codecContext->extradata_size;
    return 0;
}

int AudioEncoder::close() {
    avcodec_free_context(&this->codecContext);
    return 0;
}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_camerasample_FFAudioEncoder_createEncoder(JNIEnv *env, jobject instance) {

    // TODO
    AudioEncoder* audioEncoder = new AudioEncoder();
    return (long)audioEncoder;

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_camerasample_FFAudioEncoder_open(JNIEnv *env, jobject instance, jlong handle,
                                                  jint sampleRate, jint bitrate, jint channel) {

    // TODO
    AudioEncoder* audioEncoder = (AudioEncoder*)handle;
    audioEncoder->open(sampleRate, bitrate, channel);
}


extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_camerasample_FFAudioEncoder_encode(JNIEnv *env, jobject instance, jlong handle,
                                                    jbyteArray srcData_, jint srcLen) {
    jbyte *srcData = env->GetByteArrayElements(srcData_, NULL);

    // TODO
    AudioEncoder* audioEncoder = (AudioEncoder*)handle;
    uint8_t *dstData = NULL;
    int dstLen = 0;
    audioEncoder->encode((uint8_t*)srcData, srcLen, &dstData, &dstLen);
    jbyteArray  dst = env->NewByteArray(dstLen);
    env->SetByteArrayRegion(dst, 0, dstLen, (jbyte*)dstData);
    env->ReleaseByteArrayElements(srcData_, srcData, 0);
    return dst;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_camerasample_FFAudioEncoder_getAudioCsd0(JNIEnv *env, jobject instance,
                                                          jlong handle) {
    // TODO

    AudioEncoder* audioEncoder = (AudioEncoder*)handle;
    int len ;
    uint8_t * data  ;
    audioEncoder->getCSD0(&data, &len);

    jbyteArray dst = env->NewByteArray(len);
    env->SetByteArrayRegion(dst, 0, len, (jbyte*)data);
    return dst;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_camerasample_FFAudioEncoder_closeEncoder(JNIEnv *env, jobject instance,
                                                          jlong handle) {
    // TODO
    AudioEncoder* audioEncoder = (AudioEncoder*)handle;
    audioEncoder->close();

}

