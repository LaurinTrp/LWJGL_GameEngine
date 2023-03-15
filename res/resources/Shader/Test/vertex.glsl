#version 430 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 aColor;

//uniform mat4 modelMatrix;
//uniform mat4 viewMatrix;
//uniform mat4 projectionMatrix;

out vec4 baseColor;

void main() {
//    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);
	gl_Position = vec4(position, 1.0);
	baseColor = vec4(aColor, 1.0);
}
