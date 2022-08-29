#version 430 core

uniform sampler2D tex;

uniform vec4 cameraPos;

in vec4 fragPos;
in vec4 color;
in vec4 uvCoord;
in vec4 normal;

const int MAX_LIGHTS = 10;
uniform int numOfLights;
uniform vec4 lightsources[MAX_LIGHTS];
vec4 lightsource = vec4(1.0);

uniform vec4 sunPosition;

struct Material {
	float ambient;
	float specular;
	float diffuse;
	float reflectance;
	int hasTexture;
};

out vec4 fragColor;

float a = 0.2, d = 0.8, s = 0.5;

#include <Utils/lighting.glsl>

void main() {

	lightsource = vec4(0.0, 10.0, 10.0, 1.0);

	vec3 color = vec3(0.0);

	lightsource = sunPosition;
	color += calculateSunLight();

	if (numOfLights == 0) {
		fragColor = vec4(color, 1.0);
	} else {
		vec4 colorWithLight = vec4(color, 1.0);
		for (int i = 0; i < numOfLights; i++) {
			lightsource = lightsources[i];
			colorWithLight += vec4(calculateLight(), 1.0);
		}

		fragColor = colorWithLight;
	}
}

