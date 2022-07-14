#version 430 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 aColor;
layout(location = 2) in vec4 texCoord;

uniform vec4 offset;

out vec4 color;

void main()
{
    color = aColor;

    //gl_Position = vec4(position_cs.x, position_cs.y, position_cs.z, position_cs.w);
    gl_Position = position;
}
