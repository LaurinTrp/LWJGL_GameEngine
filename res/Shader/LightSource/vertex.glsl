#version 430 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 aColor;
layout(location = 2) in vec4 texCoord;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform float x;

out vec4 color;

void main()
{
	color = aColor;

	vec4 myPos = vec4(position.x + x * 2, position.yzw);

    gl_Position = projectionMatrix * viewMatrix * modelMatrix * myPos;
}
