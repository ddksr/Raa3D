/* Author of this file: Simon Žagar, 2012, Ljubljana
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 */
varying float intensity;

void main(){
	vec3 normal = gl_NormalMatrix * gl_Normal;
	intensity = dot(normalize(vec3(gl_LightSource[0].position)), normal);
	gl_Position = ftransform();
}



//intensity per vertex