/* Author of this file: Simon Žagar, 2013, Ljubljana
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 */



varying float zValue;
uniform vec2 minMaxZ;

void main(){
	float relativeHeight = (2.0 * (zValue-minMaxZ[0]) ) /minMaxZ[1];
	
	gl_FragColor = vec4((2.0-relativeHeight), relativeHeight, 0.0, 1.0);
}



//Phong lighting model
//2 - just diffuse with blood red color as dedication to NeckVeins application this application was built upon