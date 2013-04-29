/* Author of this file: Simon Žagar, 2012, Ljubljana
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 */
package tools;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

/**
 * @author Simon Žagar
 *@since 0.2
 *@version 0.2
 */
public class Quaternion {
    double w, x, y, z;
    //constructors
	public Quaternion(double w, double x, double y, double z){
		this.w=w;this.x=x;this.y=y;this.z=z;
	}
	public Quaternion(){
        this.w=1;this.x=0;this.y=0;this.z=0;
    }
	//static methods
	public static Quaternion quaternionFromAngleAndRotationAxis(double angle, double[] rotationAxis){
	    if(rotationAxis.length!=3)throw new IllegalArgumentException("Argument float[] rotationAxis needs to be of length 3");
	    double axisLength=rotationAxis[0]*rotationAxis[0]+rotationAxis[1]*rotationAxis[1]+rotationAxis[2]*rotationAxis[2];
	    double[] n=new double[]{rotationAxis[0]/axisLength, rotationAxis[1]/axisLength, rotationAxis[2]/axisLength};
	    double sinHalfAngle=Math.sin(angle/2);
	    return new Quaternion(Math.cos(angle/2), n[0]*sinHalfAngle, n[1]*sinHalfAngle, n[2]*sinHalfAngle);
	}
	public static Quaternion quaternionSum(Quaternion q1, Quaternion q2){
		return new Quaternion(q1.w+q2.w, q1.x+q2.x, q1.y+q2.y, q1.z+q2.z);
	}
	public static Quaternion quaternionTimesScalar(Quaternion q1, double scalar){
		return new Quaternion(q1.w*scalar, q1.x*scalar, q1.y*scalar, q1.z*scalar);
	}
	public static Quaternion quaternionMultiplication(Quaternion q1, Quaternion q2){
		return new Quaternion(q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z,
				q1.w*q2.x + q1.x*q2.w + q1.y*q2.z - q1.z*q2.y,
				q1.w*q2.y - q1.x*q2.z + q1.y*q2.w + q1.z*q2.x,
				q1.w*q2.z + q1.x*q2.y - q1.y*q2.x + q1.z*q2.w);
	}
	public static Quaternion quaternionConjugation(Quaternion q){
		return new Quaternion(q.w, -q.x, -q.y, -q.z);
	}
	public static double quaternionNorm(Quaternion q){
		return Math.sqrt(q.w*q.w + q.x*q.x + q.y*q.y + q.z*q.z);
	}
	public static Quaternion quaternionNormalization(Quaternion q){
		double normInverse=1/quaternionNorm(q);
		return new Quaternion(q.w*normInverse, q.x*normInverse, q.y*normInverse, q.z*normInverse);
	}
	public static Quaternion quaternionReciprocal(Quaternion q){
		double normInverseSquared=1/quaternionNorm(q);
		normInverseSquared*=normInverseSquared;
		return quaternionTimesScalar(quaternionConjugation(q),normInverseSquared);
	}
	//instance methods
	public float[][] getRotationMatrix(){
	    float[][] matrix=new float[4][4];
	    float s=(float)w, a=(float)x, b=(float)y, c=(float)z;
	    matrix[0][0]=1-2*b*b-2*c*c;
	    matrix[0][1]=2*a*b-2*s*c;
	    matrix[0][2]=2*a*c+2*s*b;
	    
	    matrix[1][0]=2*a*b+2*s*c;
	    matrix[1][1]=1-2*a*a-2*c*c;
	    matrix[1][2]=2*b*c-2*s*a;
	    
	    matrix[2][0]=2*a*c-2*s*b;
	    matrix[2][1]=2*b*c+2*s*a;
	    matrix[2][2]=1-2*a*a-2*b*b;
	    matrix[3][3]=1;
	    return matrix;
	}
	
	public FloatBuffer getRotationMatrix(boolean rowMajorOrder){
        float s=(float)w, a=(float)x, b=(float)y, c=(float)z;
        FloatBuffer fb= BufferUtils.createFloatBuffer(16);
        
	    if(rowMajorOrder){
	        fb.put(1-2*b*b-2*c*c);
	        fb.put(2*a*b-2*s*c);
	        fb.put(2*a*c+2*s*b);
	        fb.put(0);
	        
	        fb.put(2*a*b+2*s*c);
	        fb.put(1-2*a*a-2*c*c);
	        fb.put(2*b*c-2*s*a);
            fb.put(0);

            fb.put(2*a*c-2*s*b);
            fb.put(2*b*c+2*s*a);
            fb.put(1-2*a*a-2*b*b);
            fb.put(0);

            fb.put(0);
            fb.put(0);
            fb.put(0);
            fb.put(1);
	    }else{
	        fb.put(1-2*b*b-2*c*c);//0
	        fb.put(2*a*b+2*s*c);//4
	        fb.put(2*a*c-2*s*b);//8
	        fb.put(0);//12
	        fb.put(2*a*b-2*s*c);//1
	        fb.put(1-2*a*a-2*c*c);//5
	        fb.put(2*b*c+2*s*a);//9
	        fb.put(0);//13
	        fb.put(2*a*c+2*s*b);//2
	        fb.put(2*b*c-2*s*a);//6
	        fb.put(1-2*a*a-2*b*b);//10
	        fb.put(0);//14
	        fb.put(0);//3
	        fb.put(0);//7
	        fb.put(0);//11
	        fb.put(1);//15
	    }
        fb.rewind();
        return fb;
	}
	
	public double[] getVectorPart(){
	    return new double[] {x,y,z};
	}
	public double[] rotateVector3d(double[] v){
	    Quaternion tempQ = new Quaternion(0, v[0], v[1], v[2]);
	    tempQ=quaternionMultiplication(this, tempQ);
	    tempQ=quaternionMultiplication(tempQ, quaternionReciprocal(this));
	    return tempQ.getVectorPart();
	}
}
