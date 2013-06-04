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
        for(int i=1;i<3;i++){//for each point (except for the one already taken into account)
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
	public boolean isIntersectedByRay(float[] e, float[] d){
	    //where is crossing upper bound
	    //
	    //get rid of perpendicular cases
	    for(int i=0;i<3;i++){
	        if(d[i]==0){
	            if(e[i]<c[i][0])return false;
	            if(e[i]>c[i][1])return false;
	            for(int j=0;j<3;j++)if(j!=i){
	                if(d[j]==0){
	                    if(e[j]<c[j][0])return false;
	                    if(e[j]>c[j][1])return false;
	                    return true;
	                }
	            }
	            //parallel to exactly one case
	            int j=0;while(j==i)j++;
	            int k=0;while(k==i || k==j)k++;
	            
	            float t = (c[j][0]-e[j])/d[j];
	            if(t>0){
	                float pk=e[k]+t*d[k];
	                if(pk<=c[k][1] && pk>=c[k][0])return true;
	            }
	            t = (c[j][1]-e[j])/d[j];
                if(t>0){
                    float pk=e[k]+t*d[k];
                    if(pk<=c[k][1] && pk>=c[k][0])return true;
                }
                int l=k;k=j;j=l;
                t = (c[j][0]-e[j])/d[j];
                if(t>0){
                    float pk=e[k]+t*d[k];
                    if(pk<=c[k][1] && pk>=c[k][0])return true;
                }
                t = (c[j][1]-e[j])/d[j];
                if(t>0){
                    float pk=e[k]+t*d[k];
                    if(pk<=c[k][1] && pk>=c[k][0])return true;
                }
	        }
	    }
	    //there were no perpendicular cases, division should be safe at this point
	    for(int i=0;i<3;i++){
	        for(int fb=0;fb<2;fb++){
	            float t = (c[i][fb]-e[i])/d[i];
	            if(t>0){
	                int j=0;while(j==i)j++;
	                int k=0;while(k==i || k==j)k++;
	                float pk=e[k]+t*d[k];
	                float pj=e[j]+t*d[j];
	                
	                if(c[j][0]<=pj && c[j][1]>=pj && c[k][0]<=pk && c[k][1]>=pk)return true;
	            }
	        }
	    }
	    return false;
	}
	
}

