#version 430 core

uniform sampler2D tex;

out vec4 fragColor;

in vec4 uv;
in vec4 color;

void main() {
	float value = texture(tex, uv.xy).r;
	fragColor = vec4(value, value, value, 1.0) * .5;
//	fragColor = vec4(value/2);
}
