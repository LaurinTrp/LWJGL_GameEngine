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

//vec3 calculateLight() {
//	float a = 0.2, d = 0.3, s = 0.5;
//	//  ---------------------------
//
//	vec3 color = color.rgb;
//	color = texture(tex, uvCoord.st).rgb;
//
//	//  vector light to fragment
//	vec3 fragmentToLight = normalize(lightsource.xyz - fragPos.xyz);
//
//	//  ambient ----------------------------------------------------------------
//	vec3 ambientColor = color;
//
//	//  diffuse ----------------------------------------------------------------
//	float diffuse = dot(normalize(normal.xyz), fragmentToLight);
//
//	diffuse = max(diffuse, 0.0f);
//	vec3 diffuseColor = color * diffuse;
//
//	//  specular ---------------------------------------------------------------
//	vec3 reflection = reflect(-fragmentToLight, normalize(normal.xyz));
//	vec3 fragmentTocameraPos = normalize(cameraPos.xyz - fragPos.xyz);
//
//	float specular = dot(reflection, fragmentTocameraPos);
//	specular = max(specular, 0.0f); //  0.0 ... 1.0
//	specular = pow(specular, 32.0f);
//
//	vec3 specularColor = color * specular;
//
//	return (ambientColor * a) + (diffuseColor * d) + (specularColor * s);
//}

void main() {

	lightsource = vec4(0.0, 10.0, 10.0, 1.0);

	vec3 color = color.rgb;
	color = texture(tex, uvCoord.st).rgb;

	vec4 colorWithLight = vec4(0.0);
	for(int i = 0; i < numOfLights; i++){
		lightsource = lightsources[i];
		colorWithLight += vec4(calculateLight(), 1.0);
	}

	fragColor = colorWithLight;
}

