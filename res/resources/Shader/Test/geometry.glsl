#version 430 core

layout (points) in;
layout (triangle_strip, max_vertices = 8) out;

in vec4 baseColor[];

out vec4 color;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main(void){
	vec4 offset = vec4(-1.0, 1.0, 0.0, 0.0);
	vec4 vertexPos = offset + gl_in[0].gl_Position;
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vertexPos;
	color = baseColor[0] * vec4(1.0, 0.0, 0.0, 0.0);
	EmitVertex();
}
