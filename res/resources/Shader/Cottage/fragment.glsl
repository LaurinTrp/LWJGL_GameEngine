#version 430 core

uniform sampler2D tex;

in vec4 fragPos;
in vec4 color;
in vec4 uvCoord;
in vec4 normal;

struct Material {
	float ambient;
	float specular;
	float diffuse;
	float reflectance;
	int hasTexture;
};

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

uniform Material material;
uniform vec4 cameraPos;
uniform vec4 ambientColor;

vec4 lightPos;

out vec4 fragColor;

void setupColors(Material material){
	ambientC = ambientColor * material.ambient;
//	diffuceC = diffuseColor * material.diffuse;
//	specularC = specularColor * material.specular;
}

void main() {
	setupColors(material);

	lightPos = vec4(10.0, 0.0, 10.0, 1.0);

	vec4 norm = normalize(normal);
	vec4 lightDir = normalize(lightPos - fragPos);
	float diff = max(dot(norm, lightDir), 0.0);
	vec4 diffuse = diff * ambientColor;

//	fragColor = texture(tex, uvCoord.xy) * (ambientC + diffuse);
	fragColor = diffuse;
}

