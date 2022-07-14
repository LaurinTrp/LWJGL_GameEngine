#version 430 core

layout(location = 0) in vec4 aPos;
layout(location = 1) in vec4 aColor;
layout(location = 2) in vec4 aTexCoord;

out vec4 ourColor;
out vec4 ourTexCoord;

void main()
{
    gl_Position = aPos; //  clipspace
    ourColor = aColor;
    ourTexCoord = aTexCoord;
}
