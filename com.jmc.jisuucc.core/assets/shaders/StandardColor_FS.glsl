#version 330

in vec2 fragmentUV;
out vec4 outputColor;

uniform sampler2D myTexture;

void main(){
    outputColor = texture(myTexture, fragmentUV).rgba;
}