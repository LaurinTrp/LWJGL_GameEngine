#version 430 core

uniform vec4 cameraPos;

in vec4 color;

out vec4 fragColor;

void main() {

	fragColor = color;

}

