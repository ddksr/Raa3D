package models;
/**
 * @author Simon Žagar, 63090355
 * April, 23, 2013
 */
public class Point3D implements Comparable<Point3D> {
	float x,y,z;
	float [] v;

	public Point3D(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		v = new float[]{x,y,z};
	}
	/**
	 * @param p - vector size three or more, but higher dimensions will be ignored
	 */
	public Point3D(float[] p) {
		super();
		this.x = p[0];
		this.y = p[1];
		this.z = p[2];
		v = new float[]{x,y,z};
	}
	
	int compareTo(Point3D point, int d){
		d=d%3;
		float[] t = this.v;
		float[] p = point.v;
		for(int i=0;i<3;i++){
			if(t[d]>p[d])return 1;
			else if(t[d]<p[d])return -1;
			d=(d+1)%3;
		}
		return 0;
	}
	
	float sqDistanceTo(Point3D point){
		float xDiff = this.x-point.x;
		float yDiff = this.y-point.y;
		float zDiff = this.z-point.z;
		return xDiff*xDiff+yDiff*yDiff+zDiff*zDiff;
	}
	
	
	boolean isInBox(Box b){
		float[][]c=b.c;
		if(x>=c[0][0] && x<=c[0][1] &&
				y>=c[1][0] && y<=c[1][1] &&
				z>=c[2][0] && z<=c[2][1])return true;
		return false;
	}
	
	
	boolean isInRangeOfBox(Box box, float range){
		float[][] c = box.c;
		//in most cases the point should be far from box
		if(x+range<c[0][0] || x-range>c[0][1] ||
				y+range<c[1][0] || y-range>c[1][1] ||
				z+range<c[2][0] || z-range>c[2][1])return false;
		//here follow cases when the point is inside the box
		if(x>=c[0][0] && x<=c[0][1] &&
			y>=c[1][0] && y<=c[1][1] &&
			z>=c[2][0] && z<=c[2][1])return true;
		//here the corners are covered
		/*  This dead code makes the function more accurate, but the added calculation is greater than the benefit of knowing some nodes need not be checked.
		 * however if correct behaviour is neeed one needs uncomment this part. 
		if(x<c[0][0]){
			if(y<c[1][0]){
				if(z<c[2][0])if(sqDistanceTo(new Point3D(c[0][0], c[1][0],c[2][0]))>range*range)return false;
				if(z>c[2][1])if(sqDistanceTo(new Point3D(c[0][0], c[1][0],c[2][1]))>range*range)return false;
			}else if(y>c[1][1]){
				if(z<c[2][0])if(sqDistanceTo(new Point3D(c[0][0], c[1][1],c[2][0]))>range*range)return false;
				if(z>c[2][1])if(sqDistanceTo(new Point3D(c[0][0], c[1][1],c[2][1]))>range*range)return false;
			}
		}else if(x>c[0][1]){
			if(y<c[1][0]){
				if(z<c[2][0])if(sqDistanceTo(new Point3D(c[0][1], c[1][0],c[2][0]))>range*range)return false;
				if(z>c[2][1])if(sqDistanceTo(new Point3D(c[0][1], c[1][0],c[2][1]))>range*range)return false;
			}else if(y>c[1][1]){
				if(z<c[2][0])if(sqDistanceTo(new Point3D(c[0][1], c[1][1],c[2][0]))>range*range)return false;
				if(z>c[2][1])if(sqDistanceTo(new Point3D(c[0][1], c[1][1],c[2][1]))>range*range)return false;
			}
		}*/
		return true;
	}
	@Override
	public int compareTo(Point3D o) {
		return compareTo(o, 0);
	}
}
