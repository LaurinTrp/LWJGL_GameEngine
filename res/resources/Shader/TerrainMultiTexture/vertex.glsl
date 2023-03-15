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

uniform sampler2D heightMap;

void main()
{
	vec4 height = texture(heightMap, vec2(position.x/64, position.z/64));

	vec4 newPosition = vec4(position.x, (height.x-0.5) * 4, position.z, position.w);

    gl_Position = projectionMatrix * viewMatrix * modelMatrix * newPosition;
    fragPos = modelMatrix * newPosition;

    normal = mat4(transpose(inverse(modelMatrix))) * aNormal;
    color = aColor;
    uvCoord = texCoord;
}
