#version 430 core

uniform sampler2D tex;

in vec4 ourTexCoord;

out vec4 fragColor;

uniform vec2 screenSize;

void main() {
	const float offset_x = 1.0 / screenSize.x;
	const float offset_y = 1.0 / screenSize.y;
	vec2
	offsets[9] = vec2[](
			vec2(-offset_x, offset_y), // top-left
			vec2( 0.0f, offset_y),// top-center
			vec2( offset_x, offset_y),// top-right
			vec2(-offset_x, 0.0f),// center-left
			vec2( 0.0f, 0.0f),// center-center
			vec2( offset_x, 0.0f),// center-right
			vec2(-offset_x, -offset_y),// bottom-left
			vec2( 0.0f, -offset_y),// bottom-center
			vec2( offset_x, -offset_y)// bottom-right
	);

	float kernel[9] = float[](
			0.1, 0.1, 0.1,
			0.1, 1, 0.1,
			0.1, 0.1, 0.1
	);

	vec3 sampleTex[9];
	for (int i = 0; i < 9; i++) {
		sampleTex[i] = vec3(texture(tex, ourTexCoord.st + offsets[i]));
	}
	vec3 col = vec3(0.0);
	for (int i = 0; i < 9; i++)
		col += sampleTex[i] * kernel[i];

	fragColor = vec4(col, 1.0);
}
