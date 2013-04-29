/* Author of this file: Simon Žagar, 2012, Ljubljana
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 */
package tools;

/**
 * @author Simon Žagar, 63090355
 *
 */
public class Vector {
    
    /**
     * @param a
     * @param b
     * @return the dot product of a vector a and vector b. They have to be of same size.
     */
    public static double dotProduct(double[] a, double[] b){
        double r=0;
        for(int i=0;i<a.length;i++){
            r+=a[i]*b[i];
        }
        return r;
    }
    
    /**
     * @param a
     * @param b
     * @return Returns vector a minus vector b.
     */
    public static double[] subtraction(double[] a, double[] b){
        double[] sub=new double[a.length];
        for(int i=0;i<a.length;i++){
            sub[i]=a[i]-b[i];
        }
        return sub;
    }
    
    /**
     * @param a - double[] presenting the vector a
     * @param b - double[] presenting the vector b
     * @return Returns the sum of vectors a and b.
     */
    public static double[] sum(double[] a, double[] b){
        double[] sum=new double[a.length];
        for(int i=0;i<a.length;i++){
            sum[i]=a[i]+b[i];
        }
        return sum;
    }
    
    /**
     * @param s - double presenting the scalar
     * @param a - double[] presenting the vector
     * @return Returns vector a multiplied by the scalar s
     */
    public static double[] vScale(double[] a, double s){
        double[] scaled=new double[a.length];
        for(int i=0;i<a.length;i++){
            scaled[i]=a[i]*s;
        }
        return scaled;
    }
    
    /**
     * @param a - double[] presenting the vector a
     * @param b - double[] presenting the vector b
     * @return Returns the cross product axb as a double[] array.
     */
    public static double[] crossProduct(double[] a, double[] b){
        double[] p = new double[3];
        p[0]=a[1]*b[2]-a[2]*b[1];
        p[1]=a[2]*b[0]-a[0]*b[2];
        p[2]=a[0]*b[1]-a[1]*b[0];
        return p;
    }
    
    /**
     * @param a - double[] presenting the vector a
     * @return Returns the vector a scaled to a unit vector.
     */
    public static double[] normalize(double[] a){
        double sum=0;        
        for(int i=0;i<a.length;i++){
            sum+=a[i]*a[i];
        }
        sum=Math.sqrt(sum);
        double[] ret=new double[a.length];
        if(sum==0)return ret;
        for(int i=0;i<a.length;i++){
            ret[i]=a[i]/sum;
        }
        return ret;
    }
    
    /**
     * @param a - double[] presenting the vector a
     * @return Returns the length of the given vector a.
     */
    public static double length(double[] a){
        double sum=0;        
        for(int i=0;i<a.length;i++){
            sum+=a[i]*a[i];
        }
        return Math.sqrt(sum);
    }
    
}
