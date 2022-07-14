#version 430 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 aColor;
layout(location = 2) in vec4 texCoord;
layout(location = 3) in vec4 aNormal;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out vec4 fragPos;
out vec4 color;
out vec4 uvCoord;
out vec4 normal;

void main()
{
	vec4 worldPos = modelMatrix * position;
	fragPos = worldPos;

	normal = aNormal;

    color = aColor;
    uvCoord = texCoord;

    gl_Position = projectionMatrix * viewMatrix * modelMatrix * position;
}
