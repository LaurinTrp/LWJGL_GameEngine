#version 430 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 vertexColor;
layout(location = 2) in vec4 uvCoordinate;



out vec4 color;

void main()
{
    vec4 position_cs = /*proj * view * model * */position;

    color = vertexColor;

    gl_Position = position_cs;

}
