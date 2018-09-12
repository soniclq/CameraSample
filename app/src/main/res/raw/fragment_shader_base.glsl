precision mediump float; //指定默认精度
#extension GL_OES_EGL_image_external : require
varying vec2 textureCoordinate;
uniform samplerExternalOES inputImageTexture;
uniform vec2 iResolution;

void main() {
    gl_FragColor = texture2D(inputImageTexture, textureCoordinate);
}