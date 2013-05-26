package models;

/**
 * @author Simon Žagar, 63090355
 * April, 23, 2013
 */
public class Box {
	double[][] c;
	int[] triangleIndices;

	public Box(double[][] c) {this.c = c;}
	public Box(){
		c = new double[3][2];
		for(int i=0;i<3;i++){
			c[i][0]=Double.MIN_VALUE;
			c[i][1]=Double.MAX_VALUE;
		}
	}
	
	
	public Box(double[] points, int[] indices){ //this is for inserting triangles
        c = new double[3][2];
        for(int i=0;i<3;i++){
            c[i][0]=points[indices[0]*3+i];
            c[i][1]=points[indices[0]*3+i];
        }
        this.triangleIndices=indices;
        for(int i=1;i<3;i++){//for each point (except for the one already takin into account)
            for(int j=0;j<3;j++){//for each coordinate (x,y,z)
                c[j][0]=Math.min(points[indices[i]*3+j], c[j][0]);
                c[j][1]=Math.max(points[indices[i]*3+j], c[j][1]);
            }
        }
    }
	
	public double avgSqDistToPoint(Point3D p){
		double x = (c[0][0]+c[0][1])/2-p.x;
		double y = (c[1][0]+c[1][1])/2-p.y;
		double z = (c[2][0]+c[2][1])/2-p.z;
		return x*x+y*y+z*z;
	}
	
	public boolean isCollidingBox(Box box){
		double[][] a = this.c;
		double[][] b = box.c;
		for(int i=0;i<3;i++){
			if(a[i][0]>b[i][1])return false;
			if(a[i][1]<b[i][0])return false;
		}
		return true;
	}
	public boolean isCollidingBox2(Box box){
		double[][] a = this.c;
		double[][] b = box.c;
		for(int i=0;i<3;i++){
			if(a[i][0]==b[i][1])return true;
			if(a[i][1]==b[i][0])return true;
			if(a[i][0]>b[i][1])return false;
			if(a[i][1]<b[i][0])return false;
		}
		return true;
	}
}
