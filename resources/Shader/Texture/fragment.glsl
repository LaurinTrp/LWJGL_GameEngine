#version 430 core

uniform sampler2D tex;


in vec4 ourColor;
in vec4 ourTexCoord;

out vec4 fragColor;

void main()
{
    fragColor = texture(tex, ourTexCoord.xy) * ourColor;
}
