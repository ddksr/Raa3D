package tools;

public class Plane {
    
    public static double[] intersectPoint(double[] abcd, double[] lineE, double[] lineR) {
        
        double t = - (abcd[0]*lineE[0] + abcd[1]*lineE[1] + abcd[2]*lineE[2] + abcd[3]) / (abcd[0]*lineR[0] + abcd[1]*lineR[1] + abcd[2]*lineR[2]);
        return new double[] {
                lineE[0] + t*lineR[0],
                lineE[1] + t*lineR[1],
                lineE[2] + t*lineR[2]
        };
    }
    
}
