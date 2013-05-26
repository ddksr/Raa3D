package tools;

import java.nio.FloatBuffer;

public class Matrix {
    public static float[] transformXYZ(FloatBuffer matrix, float x, float y, float z){
        float[] ret = new float[]{0,0,0,0};
        ret[0]=matrix.get(0)*x + matrix.get(4)*y + matrix.get(8)*z + matrix.get(12);
        ret[1]=matrix.get(1)*x + matrix.get(5)*y + matrix.get(9)*z + matrix.get(13);
        ret[2]=matrix.get(2)*x + matrix.get(6)*y + matrix.get(10)*z + matrix.get(14);
        ret[3]=matrix.get(3)*x + matrix.get(7)*y + matrix.get(11)*z + matrix.get(15);
        return new float[]{ret[0]/ret[3], ret[1]/ret[3], ret[2]/ret[3]};
    }
    
    public static float[] transformXYZMatrixTransposed(FloatBuffer matrix, float x, float y, float z){
        float[] ret = new float[]{0,0,0,0};
        ret[0]=matrix.get(0)*x + matrix.get(1)*y + matrix.get(2)*z + matrix.get(3);
        ret[1]=matrix.get(4)*x + matrix.get(5)*y + matrix.get(6)*z + matrix.get(7);
        ret[2]=matrix.get(8)*x + matrix.get(9)*y + matrix.get(10)*z + matrix.get(11);
        ret[3]=matrix.get(12)*x + matrix.get(13)*y + matrix.get(14)*z + matrix.get(15);
        return new float[]{ret[0]/ret[3], ret[1]/ret[3], ret[2]/ret[3]};
    }
}
