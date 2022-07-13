#version 430 core

uniform sampler2D tex;

const int MAX_LIGHTS = 10;
uniform int numOfLights;
uniform vec4 lightsources[MAX_LIGHTS];

vec4 lightColor = vec4(0.6, 0.6, 0.5, 1.0);

out vec4 fragColor;

uniform vec4 cameraPos;

vec4 norm = vec4(1.0);
vec4 lightDir = vec4(1.0);

in vec4 fragPos;
in vec4 color;
in vec4 uvCoord;
in vec4 normal;

struct Light{
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
};

vec4 calculateAmbient(){
    float ambientStrength = 1.0;
    vec4 ambient = ambientStrength * lightColor;
    return ambient;
}

vec4 calculateDiffuse(){
	float diff = max(dot(norm, lightDir), 0.0);
	vec4 diffuse = diff * lightColor;
	return diffuse;
}

vec4 calculateSpecular(){
	float specularStrength = 0.5;

	vec4 viewDir = normalize(cameraPos - fragPos);
	vec4 reflectDir = reflect(-lightDir, norm);
	float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
	vec4 specular = specularStrength * spec * lightColor;

	return specular;
}

Light calculateLight(){
	Light light;
	light.ambient = calculateAmbient();

	light.diffuse = vec4(0.0);
	light.specular = vec4(0.0);
	for(int i = 0; i < numOfLights; i++){
		lightDir = normalize(lightsources[i] - fragPos);
		light.diffuse += calculateDiffuse();
		light.specular += calculateSpecular();
	}
	return light;
}

void main()
{
	norm = normalize(normal);
	Light light = calculateLight();
	vec4 texColor = texture(tex, uvCoord.xy);
	fragColor = texColor * (light.ambient + light.diffuse + light.specular);
}

