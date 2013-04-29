/* Author of this file: Simon Žagar, 2012, Ljubljana
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 */
package tools;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author Simon Žagar, 63090355
 *@version 0.60
 *@since 0.45
 */
public class Tools {
	/**
	 * @version 0.45
	 * @since 0.45
	 * @param A, B, C - points of the given triangle, drawn in this order. A, B and C must be of float[] with length 3 or more where only first 3 elements are used.
	 * @return Returns a normal on the front face of the triangle.
	 */
	public static float[] normalOnTriangle(float[] A, float[] B, float[] C){
		float[] a = new float[3];//vector B-A
		float[] b = new float[3];//vector C-A
		float[] normal = new float[3];
		for(int i=0;i<3;i++){
			a[i]=B[i]-A[i];
			b[i]=C[i]-A[i];
		}
		normal[0]=a[1]*b[2]-a[2]*b[1];
		normal[1]=a[2]*b[0]-a[0]*b[2];
		normal[2]=a[0]*b[1]-a[1]*b[0];
		float nLen=(float)Math.sqrt(normal[0]*normal[0]+normal[1]*normal[1]+normal[2]*normal[2]);
		normal[0]/=nLen;
		normal[1]/=nLen;
		normal[2]/=nLen;
		return normal;
	}
	
	/**
	 * @version 0.45
	 * @since 0.45
	 * @param rs - describes rotations. It's an array of 3 floats.
	 * @param p - given vector
	 * @return Returns a normal on the front face of the triangle.
	 */
	public static float[] rotatePoint(float[] rs, float[] v){
		//System.out.println("v start"+v[0]+";"+v[1]+";"+v[2]);
		float[] p=new float[]{v[0],v[1],v[2],1};
		float cosx=(float)Math.cos(rs[0]*2*Math.PI/360);
		float sinx=(float)Math.sin(rs[0]*2*Math.PI/360);
		float cosy=(float)Math.cos(rs[1]*2*Math.PI/360);
		float siny=(float)Math.sin(rs[1]*2*Math.PI/360);
		float cosz=(float)Math.cos(rs[2]*2*Math.PI/360);
		float sinz=(float)Math.sin(rs[2]*2*Math.PI/360);
		float[][] matrixx=new float[][]{{1,0,0,0},{0,cosx,-sinx,0},{0,sinx,cosx,0},{0,0,0,1}};
		float[][] matrixy=new float[][]{{cosy,0,siny,0},{0,1,0,0},{-siny,0,cosy,0},{0,0,0,1}};
		float[][] matrixz=new float[][]{{cosz,-sinz,0,0},{sinz,cosz,0,0},{0,0,1,0},{0,0,0,1}};
		float[][] matrix=new float[][]{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
		matrix=multiSquaredMatrix(matrixz,matrix);
		matrix=multiSquaredMatrix(matrixy,matrix);
		matrix=multiSquaredMatrix(matrixx,matrix);
		
		float[] pr=new float[4];
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				pr[i]+=matrix[i][j]*p[j];
			}
		}
		//System.out.println("v end"+pr[0]+";"+pr[1]+";"+pr[2]);
		//if(r[3]==1)System.exit(0);
		return new float[]{pr[0],pr[1],pr[2]};
	}
	
	public static float[][] multiSquaredMatrix(float[][] left, float[][] right){
		float[][] matrix=new float[left.length][left.length];
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				for(int k=0;k<4;k++){
					matrix[i][j]+=left[i][k]*right[k][j];
				}
			}
		}
		return matrix;
	}
	
	/**
	 * @param a
	 * @param b
	 * @return the dot product of a vector a and vector b. They have to be of same size.
	 * @version 0.60
	 * @since 0.60
	 */
	public static float dProd(float[] a, float[] b){//dot product
		float r=0;
		for(int i=0;i<a.length;i++){
			r+=a[i]*b[i];
		}
		return r;
	}
	
	/**
	 * @param a
	 * @param b
	 * @return the sum of two vectors. They have to be of same size.
	 * @version 0.60
	 * @since 0.60
	 */
	public static float[] vSum(float[] a, float[] b){//vector sum
		float[] sum=new float[a.length];
		for(int i=0;i<a.length;i++){
			sum[i]=a[i]+b[i];
		}
		return sum;
	}
	/**
	 * @param a
	 * @param b
	 * @return a vector a minus vector b.
	 * @version 0.60
	 * @since 0.60
	 */
	public static float[] vSub(float[] a, float[] b){
		float[] sub=new float[a.length];
		for(int i=0;i<a.length;i++){
			sub[i]=a[i]-b[i];
		}
		return sub;
	}
	/**
	 * @param s
	 * @param a
	 * @return a vector a multiplied by a scalar s.
	 * @version 0.60
	 * @since 0.60
	 */
	public static float[] vScale(float[] a, float s){//scale a vector (multiply a vector with a scalar)
		float[] scaled=new float[a.length];
		for(int i=0;i<a.length;i++){
			scaled[i]=a[i]*s;
		}
		return scaled;
	}
	/**
	 * @param a
	 * @return the magnitude or length of the vector a.
	 * @version 0.60
	 * @since 0.60
	 */
	public static float vLen(float[] a){
		float sum=0;		
		for(int i=0;i<a.length;i++){
			sum+=a[i]*a[i];
		}
		return (float)Math.sqrt(sum);
	}
	/**
	 * @param a
	 * @return vector a, but normalized
	 * @version 0.60
	 * @since 0.60
	 */
	public static float[] vNorm(float[] a){
		float sum=0;		
		for(int i=0;i<a.length;i++){
			sum+=a[i]*a[i];
		}
		sum=(float)Math.sqrt(sum);
		float[] ret=new float[a.length];
		for(int i=0;i<a.length;i++){
			ret[i]=a[i]/sum;
		}
		return ret;
	}
	/**
	 * @param a
	 * @param scale
	 * @return vector a, but of length scale
	 * @version 0.60
	 * @since 0.60
	 */
	public static float[] vToScale(float[] a, float scale){
		float sum=0;
		for(int i=0;i<a.length;i++){
			sum+=a[i]*a[i];
		}
		sum=(float)Math.sqrt(sum);
		float[] ret=new float[a.length];
		if(sum==0)return ret;
		for(int i=0;i<a.length;i++){
			ret[i]=scale*a[i]/sum;
		}
		return ret;
	}
	/**
	 * @param a
	 * @param b
	 * @return a vector of vector a and vector b being multiplied per element, but those values not being summed into a scalar.
	 * @version 0.60
	 * @since 0.60
	 */
	public static float[] vTimesV(float[] a, float[] b){
		float[] product=new float[a.length];		
		for(int i=0;i<a.length;i++){
			product[i]=a[i]*b[i];
		}
		return product;
	}
	
	/**
	 * Copied from school web page.
	 * @param floatarray
	 * @return
	 */
	public static FloatBuffer allocFloats(float[] floatarray)
	  {
	    FloatBuffer fb = ByteBuffer.allocateDirect(floatarray.length * 4).order(
	        ByteOrder.nativeOrder()).asFloatBuffer();
	    fb.put(floatarray).flip();
	    return fb;
	  }
	/**
	 * Copied from school web page.
	 * @param intarray
	 * @return
	 */
	public static IntBuffer allocInts(int[] intarray)
	  {
	    IntBuffer ib = ByteBuffer.allocateDirect(intarray.length * 4).order(
	        ByteOrder.nativeOrder()).asIntBuffer();
	    ib.put(intarray).flip();
	    return ib;
	  }
}
