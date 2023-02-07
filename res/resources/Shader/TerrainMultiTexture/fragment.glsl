#version 430 core

uniform sampler2D blendMap;
uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;

uniform float size;

in vec4 fragPos;
in vec4 color;
in vec4 uvCoord;
in vec4 normal;

out vec4 fragColor;

void main() {

	vec4 rgbValue = texture(blendMap, uvCoord.xy);
	float backgroundAmount = 1 - (rgbValue.r + rgbValue.g + rgbValue.b);

	vec4 tiledCoord = uvCoord * size;

	vec4 backgroundColor = backgroundAmount * texture(backgroundTexture, tiledCoord.xy);
	vec4 rColor = rgbValue.r * texture(rTexture, tiledCoord.xy);
	vec4 gColor = rgbValue.g * texture(gTexture, tiledCoord.xy);
	vec4 bColor = rgbValue.b * texture(bTexture, tiledCoord.xy);

	fragColor = backgroundColor + rColor + gColor + bColor;
}

