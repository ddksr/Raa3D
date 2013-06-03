/* Author of this file: Simon �agar, 2013, Ljubljana
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 */
uniform vec3 lightDir;
varying vec3 normal;
varying vec4 position;
varying vec4 perVertexColorVar;

void main(){
	vec3 n=normalize(normal);
	vec3 l=normalize(vec3(gl_LightSource[0].position));
	float intensity = max(dot(l, n), 0.0);
	
	vec4 diffuse = perVertexColorVar;
	
	gl_FragColor = intensity * diffuse;
}



//Phong lighting model
//3 - just diffuse