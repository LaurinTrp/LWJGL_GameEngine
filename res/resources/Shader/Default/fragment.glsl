#version 430 core


uniform sampler2D tex;

in vec4 fragPos;
in vec4 color;
in vec4 uvCoord;
in vec4 normal;

out vec4 fragColor;

void main() {

    fragColor = texture(tex, uvCoord.xy);

}

