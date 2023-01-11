#version 430 core

layout(location = 0) in vec4 aPos;

uniform mat4 lightSpaceMatrix;
uniform mat4 model;

void main()
{
	gl_Position = lightSpaceMatrix * model * aPos;
}
