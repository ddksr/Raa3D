package models;

/**
 * @author Simon Žagar, 63090355
 * April, 23, 2013
 */
public class Box {
	float[][] c;
	int[] triangleIndices;

	public Box(float[][] c) {this.c = c;}
	public Box(){
		c = new float[3][2];
		for(int i=0;i<3;i++){
			c[i][0]=Float.MIN_VALUE;
			c[i][1]=Float.MAX_VALUE;
		}
	}
	
	
	public Box(float[] points, int[] indices){ //this is for inserting triangles
        c = new float[3][2];
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
	
	public float avgSqDistToPoint(Point3D p){
		float x = (c[0][0]+c[0][1])/2-p.x;
		float y = (c[1][0]+c[1][1])/2-p.y;
		float z = (c[2][0]+c[2][1])/2-p.z;
		return x*x+y*y+z*z;
	}
	
	public boolean isCollidingBox(Box box){
		float[][] a = this.c;
		float[][] b = box.c;
		for(int i=0;i<3;i++){
			if(a[i][0]>b[i][1])return false;
			if(a[i][1]<b[i][0])return false;
		}
		return true;
	}
	public boolean isCollidingBox2(Box box){
		float[][] a = this.c;
		float[][] b = box.c;
		for(int i=0;i<3;i++){
			if(a[i][0]==b[i][1])return true;
			if(a[i][1]==b[i][0])return true;
			if(a[i][0]>b[i][1])return false;
			if(a[i][1]<b[i][0])return false;
		}
		return true;
	}
}
