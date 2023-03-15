#version 430 core

out vec4 fragColor;

in vec4 color;

void main() {

	fragColor = color;
//	fragColor = vec4(1.0, 0.0, 0.0, 1.0);

}

