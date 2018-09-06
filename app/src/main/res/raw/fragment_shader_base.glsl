precision mediump float; //指定默认精度

varying vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
uniform vec2 iResolution;

void main() {
    gl_FragColor = texture2D(inputImageTexture, textureCoordinate);
}