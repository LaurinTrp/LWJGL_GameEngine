#version 430 core

layout(location = 0) in vec4 position;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out vec4 fragPos;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * position;
    fragPos = modelMatrix * position;
}
