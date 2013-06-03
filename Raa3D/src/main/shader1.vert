/* Author of this file: Simon �agar, 2013, Ljubljana
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 */
varying vec3 normal;

attribute vec3 perVertexLocation;

void main(){
	normal = gl_NormalMatrix * gl_Normal;
	gl_Position = ftransform();
}