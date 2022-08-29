#version 430

vec3 calculateLight() {
	//  ---------------------------

	vec3 color = texture(tex, uvCoord.st).rgb;

	//  vector light to fragment
	vec3 fragmentToLight = normalize(lightsource.xyz - fragPos.xyz);

	//  ambient ----------------------------------------------------------------
	vec3 ambientColor = color;

	//  diffuse ----------------------------------------------------------------
	float diffuse = dot(normalize(normal.xyz), fragmentToLight);

	diffuse = max(diffuse, 0.0f);
	vec3 diffuseColor = color * diffuse;

	//  specular ---------------------------------------------------------------
	vec3 reflection = reflect(-fragmentToLight, normalize(normal.xyz));
	vec3 fragmentTocameraPos = normalize(cameraPos.xyz - fragPos.xyz);

	float specular = dot(reflection, fragmentTocameraPos);
	specular = max(specular, 0.0f); //  0.0 ... 1.0

	specular = pow(specular, 16.0f);

	vec3 specularColor = color * specular;

	return (ambientColor * a) + (diffuseColor * d) + (specularColor * s);
}

vec3 calculateSunLight() {
	//  ---------------------------

	vec3 color = texture(tex, uvCoord.st).rgb;

	//  vector light to fragment
	vec3 fragmentToLight = normalize(lightsource.xyz);

	//  ambient ----------------------------------------------------------------
	vec3 ambientColor = color;

	//  diffuse ----------------------------------------------------------------
	float diffuse = dot(normalize(normal.xyz), fragmentToLight);

	diffuse = max(diffuse, 0.0f);
	vec3 diffuseColor = color * diffuse;

	//  specular ---------------------------------------------------------------
	vec3 reflection = reflect(-fragmentToLight, normalize(normal.xyz));
	vec3 fragmentTocameraPos = normalize(cameraPos.xyz - fragPos.xyz);

	float specular = dot(reflection, fragmentTocameraPos);
	specular = max(specular, 0.0f); //  0.0 ... 1.0

	specular = pow(specular, 16.0f);

	vec3 specularColor = color * specular;

	return (ambientColor * 0.2) + (diffuseColor * 0.2) + (specularColor * 0.4);
}
