#version 430

vec3 calculateLightWithColor(vec4 lightColor){
	//  ---------------------------

	vec3 reflectance = texture(texReflectance, uvCoord.st).rgb;

	//  vector light to fragment
	vec3 fragmentToLight = normalize(lightsource.xyz);

	//  ambient ----------------------------------------------------------------
	vec3 ambientColor = lightColor.rgb;

	//  diffuse ----------------------------------------------------------------
	float diffuse = dot(normalize(normal.xyz), fragmentToLight);

	diffuse = max(diffuse, 0.0f);
	vec3 diffuseColor = lightColor.rgb * diffuse;

	//  specular ---------------------------------------------------------------
	vec3 reflection = reflect(-fragmentToLight, normalize(normal.xyz));
	vec3 fragmentTocameraPos = normalize(cameraPos.xyz - fragPos.xyz);

	float specular = dot(reflection, fragmentTocameraPos);
	specular = max(specular, 0.0f); //  0.0 ... 1.0

	specular = pow(specular, 16.0f);

	vec3 specularColor = lightColor.rgb * specular;

	return (ambientColor * 0.1) + (diffuseColor * 0.1) + (specularColor * 0.1);
}

vec3 calculateLight() {
	//  ---------------------------
	vec4 color = texture(texDiffuse, uvCoord.st);

	return calculateLightWithColor(color);
}

vec3 calculateSunLight(vec4 sunColor) {
	return calculateLightWithColor(sunColor*0.5);
}
