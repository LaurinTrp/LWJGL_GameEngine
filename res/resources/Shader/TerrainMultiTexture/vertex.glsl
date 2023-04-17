#version 430 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 aColor;
layout(location = 2) in vec4 texCoord;
layout(location = 3) in vec4 aNormal;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

//uniform float terrainSize;

out vec4 fragPos;
out vec4 color;
out vec4 uvCoord;
out vec4 normal;

uniform highp sampler2D heightMap;
uniform float yOffset;
uniform float multiplier;

void main()
{
	float height = (texture(heightMap, texCoord.xy).r * 2.0 - 1.0);
//	float height = (texture(heightMap, texCoord.xy).r + yOffset) * multiplier;

	vec4 newPosition = vec4(position.x, height, position.z, 1.0);

    gl_Position = projectionMatrix * viewMatrix * modelMatrix * newPosition;
    fragPos = modelMatrix * newPosition;

    normal = mat4(transpose(inverse(modelMatrix))) * aNormal;
    color = aColor;
    uvCoord = texCoord;
}
