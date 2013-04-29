/* Author of this file: Simon Žagar, 2012, Ljubljana
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 */
uniform vec4 bloodColor;
uniform vec3 lightDir;
varying vec3 normal;
varying vec4 position;

void main(){
	vec3 n=normalize(normal);
	vec3 l=normalize(vec3(gl_LightSource[0].position));
	float intensity = max(dot(l, n), 0.0);
	
	vec4 diffuse = gl_FrontMaterial.diffuse * gl_LightSource[0].diffuse;
	
	vec4 specular=vec4(0.0, 0.0, 0.0, 0.0);
	if(intensity>0.0){
		vec3 eye = normalize( vec3(position.xyz) );
		vec3 R= (2.0*dot(l, n)*n)-l;
		float cosAngle=dot(normalize(R), eye);
		if(cosAngle>0.0){
			specular = (gl_FrontMaterial.specular * gl_LightSource[0].specular) * pow(cosAngle, gl_FrontMaterial.shininess);
		}
	}
	
	gl_FragColor = gl_Color + intensity * diffuse + specular;
}



//Phong lighting model