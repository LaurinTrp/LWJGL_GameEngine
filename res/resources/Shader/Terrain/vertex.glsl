#version 430 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 texCoord;
layout(location = 2) in vec4 aColor;
layout(location = 3) in vec4 aNormal;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out vec4 fragPos;
out vec4 color;
out vec4 uvCoord;
out vec4 normal;

void main() {

	gl_Position = projectionMatrix * viewMatrix * modelMatrix * position;
	fragPos = modelMatrix * position;
	normal = mat4(transpose(inverse(modelMatrix))) * aNormal;
	color = aColor;

	if (fragPos.y < 0.0) {
		color = vec4(0.561, 0.447, 0.251, 1.0);

	} else {
		color = vec4(0.0, 0.5, 0.0, 1.0);
	}

	uvCoord = texCoord;
}
