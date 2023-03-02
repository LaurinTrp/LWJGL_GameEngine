#version 430 core

layout (points) in;
layout (triangle_strip, max_vertices = 8) out;

in vec4 color[];

out vec4 aColor;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main(void){
	vec4 offset = vec4(-1.0, 1.0, 0.0, 0.0);
	vec4 vertexPos = offset + gl_in[0].gl_Position;
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vertexPos;
	color = color[0] * vec3(1.0, 0.0, 0.0, 0.0);
}
