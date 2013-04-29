/* Author of this file: Simon Žagar, 2012, Ljubljana
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 */
uniform vec4 bloodColor;
uniform vec3 lightDir;
varying vec3 normal;

void main(){
	vec3 n=normalize(normal);
	
	float intensity = max(dot(normalize(vec3(gl_LightSource[0].position)), n), 0.0);
	
	vec4 diffuse = gl_FrontMaterial.diffuse * gl_LightSource[0].diffuse;
	
	gl_FragColor = diffuse*intensity + gl_Color;
}



//intensity per fragment