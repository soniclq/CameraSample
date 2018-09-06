uniform mat4 uTexMatrix;  // Texture 的变换矩阵 （只对texture变形）
uniform mat4 uMVPMatrix;
attribute vec4 aPosition;
attribute vec4 aTextureCoord;

varying vec2 textureCoordinate;

void main() {
    gl_Position =  uMVPMatrix * aPosition;
    textureCoordinate = (uTexMatrix * aTextureCoord).xy;
}