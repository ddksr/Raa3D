package models;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;



/**
 * @author Simon �agar, 63090355
 * April, 23, 2013
 */

public class RTree {
	Node hanger;
	int treeDegree=3;
	public int treeDepth;
	Node currentBest;
	Node highestInBox;
	
	double[] triangleVertices;
	
	
	/*public RTree(ArrayList<double[]> input, int degree){
		treeDegree=degree;
		hanger = new Node(null);
		treeDepth=0;
		ArrayList<Node> boxes = new ArrayList<Node>(input.size());
		TreeSet<double[]> temp = new TreeSet<double[]>(new Comparator<double[]>() {
			@Override
			public int compare(double[] arg0, double[] arg1) {
				for(int i=0;i<3;i++){
					if(arg0[i]>arg1[i])return 1;
					else if(arg0[i]<arg1[i])return -1;
				}
				return 0;
			}
		});
		for(double[] p : input)temp.add(p);
		for(double[] p : temp)boxes.add(  new Node(new Box(p), null)  );
		treeDepth=1;
		hanger.children[0] = XNearestBulkInsert(boxes).get(0);
		hanger.children[0].parent=hanger;
	}*/
	
	public RTree(double[] inputTriangleVertices, IntBuffer triangleIndices, int degree){
	    triangleVertices=inputTriangleVertices;
        treeDegree=degree;
        hanger = new Node(null);
        treeDepth=0;
        ArrayList<Node> boxes = new ArrayList<Node>(inputTriangleVertices.length/3);
        //TODO: put boxes in some kind of an order (necessary)
        int[] tempTriangleIndices;
        while(triangleIndices.hasRemaining()){
            tempTriangleIndices=new int[]{triangleIndices.get(),triangleIndices.get(),triangleIndices.get()};
            boxes.add(new Node(new Box(inputTriangleVertices, tempTriangleIndices), null));
        }
        triangleIndices=null;
        
        //for(double[] p : temp)boxes.add(  new Node(new Box(inputTriangles), null)  );
        treeDepth=1;
        hanger.children[0] = XNearestBulkInsert(boxes).get(0);
        hanger.children[0].parent=hanger;
    }
	
	private ArrayList<Node> XNearestBulkInsert(ArrayList<Node> boxes){
		Collections.sort(boxes, new XYZComparator(0));
		ArrayList<Node> ret = new ArrayList<Node>(1+boxes.size()/treeDegree);
		int counter = 0;
		Node tempNode = new Node(null);
		for(Node box : boxes){
			if(counter == treeDegree){
				counter = 0;
				tempNode.resetMBB();
				ret.add(tempNode);
				tempNode = new Node(null);
			}
			tempNode.children[counter]=box;
			box.parent=tempNode;
			counter++;
		}
		tempNode.resetMBB();
		ret.add(tempNode);
		
		treeDepth++;
		if(ret.size()==1)return ret;
		else return XNearestBulkInsert(ret);
	}
	
	public static class XYZComparator implements Comparator<Node>{
		int depth;
		public XYZComparator(int depth) {
			super();
			this.depth = depth;
		}

		@Override
		public int compare(Node n1, Node n2) {
			int d=depth%3;
			double[] t = new double[3];
			double[] p = new double[3];
			for(int i=0;i<3;i++){
				t[d]=(n1.box.c[d][0]+n1.box.c[d][0])/2;
				p[d]=(n2.box.c[d][0]+n2.box.c[d][0])/2;
				if(t[d]>p[d])return 1;
				else if(t[d]<p[d])return -1;
				d=(d+1)%3;
			}
			return 0;
		}
	}
	
	
	public boolean member(Point3D p){
		if(hanger.children[0]==null)return false; //O(1)
		else return hanger.children[0].member(p); //O(d) - where d is maximum depth of the tree
	}
	
	public Point3D findNearest(Point3D inputPoint){
		if(hanger.children[0]==null)return null;
		currentBest=null;
		hanger.children[0].aproximateNearest(inputPoint);
		currentBest.unwind(inputPoint, null);
		return new Point3D(currentBest.box.c[0][0], currentBest.box.c[1][0], currentBest.box.c[2][0]);
	}
	
	public Point3D findNearestNotSelf(Point3D inputPoint){
		if(hanger.children[0]==null)return null;
		currentBest=null;
		hanger.children[0].aproximateNearestNotSelf(inputPoint);
		currentBest.unwindNotSelf(inputPoint, currentBest);
		return new Point3D(currentBest.box.c[0][0], currentBest.box.c[1][0], currentBest.box.c[2][0]);
	}
	
	public ArrayList<Point3D> findBox(Box b){
		ArrayList<Point3D> foundPoints = new ArrayList<Point3D>();
		if(hanger.children[0]==null)return foundPoints;
		else{
			ArrayList<Node> foundNodes = new ArrayList<Node>();
			double[][] newBoxDoubles = new double[3][2];
			for(int i=0;i<3;i++)
				for(int j=0;j<2;j++)
					newBoxDoubles[i][j]=b.c[i][j];
			Box box = new Box(newBoxDoubles);
			hanger.children[0].fillNodesInBox(box,foundNodes);
			for(Node n:foundNodes)foundPoints.add(new Point3D(n.box.c[0][0], n.box.c[1][0], n.box.c[2][0]));
			return foundPoints;
		}
	}
	
	
	public Point3D findHighestInBox(Box b, int coordinate){
		if(hanger.children[0]==null)return null;
		else{
			highestInBox=null;
			hanger.children[0].highestInBox(b,coordinate);
			return new Point3D(highestInBox.box.c[0][0], highestInBox.box.c[1][0], highestInBox.box.c[2][0]);
		}
	}
	
	
	class Node{
		Box box;
		Node parent;
		Node[] children;
		
		public Node(Box box, Node parent) {
			super();
			this.box = box;
			this.parent = parent;
			children = new Node[treeDegree];
		}
		public Node(Node parent) {
			super();
			double[][] bx = new double[][]{{Double.MAX_VALUE, Double.MIN_VALUE},
					{Double.MAX_VALUE, Double.MIN_VALUE},
					{Double.MAX_VALUE, Double.MIN_VALUE}};
			this.box = new Box(bx);
			this.parent = parent;
			children = new Node[treeDegree];
		}

		public void resetMBB() {
			for(int i=0;i<treeDegree;i++){
				for(int d=0;d<3;d++){
					if(children[i]!=null){
						this.box.c[d][0]=Math.min(this.box.c[d][0], children[i].box.c[d][0]);
						this.box.c[d][1]=Math.max(this.box.c[d][1], children[i].box.c[d][1]);
					}
				}
			}
		}
		
		public boolean member(Point3D p) {
			if(children[0]==null)return p.isInBox(this.box);
			boolean ret = false;
			for(int i=0;i<treeDegree;i++){
				if(children[i]!=null){
					if(p.isInBox(children[i].box)){
						if(children[i].member(p))ret = true;
					}
				}
			}
			return ret;
		}
		
		public void aproximateNearest(Point3D inputPoint) {
			if(children[0]==null){
				currentBest=this;
				return;
			}
			Node localBest = children[0];
			for(int i=1;i<treeDegree;i++){
				if(children[i]!=null){
					if(children[i].box.avgSqDistToPoint(inputPoint)<localBest.box.avgSqDistToPoint(inputPoint)){
						localBest = children[i];
					}
				}
			}
			localBest.aproximateNearest(inputPoint);
		}
		public void unwind(Point3D inputPoint, Node alreadyChecked) {
			DFNNUnwind(inputPoint, alreadyChecked);
			if(parent!=hanger){
				parent.unwind(inputPoint, this);
			}
		}
		public void DFNNUnwind(Point3D p, Node alreadyChecked){
			if(children[0]==null){
				if(box.avgSqDistToPoint(p)<currentBest.box.avgSqDistToPoint(p))currentBest=this;
				return;
			}
			double range = Math.sqrt(currentBest.box.avgSqDistToPoint(p));
			
			for(int i=0;i<treeDegree;i++){
				if(children[i]!=null){
					if(children[i]!=alreadyChecked){
						if(p.isInRangeOfBox(children[i].box, range)){
							children[i].DFNNUnwind(p, alreadyChecked);
						}
					}
				}
			}
		}
		
		public void aproximateNearestNotSelf(Point3D inputPoint) {
			if(children[0]==null){
				if(this.box.avgSqDistToPoint(inputPoint)!=0){
					currentBest=this;
				}
				return;
			}
			Node localBest = children[0];
			for(int i=1;i<treeDegree;i++){
				if(children[i]!=null){
					double childDistance = children[i].box.avgSqDistToPoint(inputPoint);
					if((childDistance<localBest.box.avgSqDistToPoint(inputPoint) || localBest.box.avgSqDistToPoint(inputPoint)==0) && childDistance!=0){
						localBest = children[i];
					}
				}
			}
			localBest.aproximateNearestNotSelf(inputPoint);
		}
		public void unwindNotSelf(Point3D inputPoint, Node alreadyChecked) {
			DFNNUnwindNotSelf(inputPoint, alreadyChecked);
			if(parent!=hanger){
				parent.unwindNotSelf(inputPoint, this);
			}
		}
		public void DFNNUnwindNotSelf(Point3D p, Node alreadyChecked){
			if(children[0]==null){
				double thisDistance = box.avgSqDistToPoint(p); 
				if(thisDistance<currentBest.box.avgSqDistToPoint(p) && thisDistance!=0)currentBest=this;
				return;
			}
			double range = Math.sqrt(currentBest.box.avgSqDistToPoint(p));
			for(int i=0;i<treeDegree;i++){
				if(children[i]!=null){
					if(children[i]!=alreadyChecked){
						if(p.isInRangeOfBox(children[i].box, range)){
							children[i].DFNNUnwindNotSelf(p, alreadyChecked);
						}
					}
				}
			}
		}
		public void fillNodesInBox(Box b, ArrayList<Node> foundNodes) {
			if(this.box.isCollidingBox(b)){
				if(children[0]==null)foundNodes.add(this);
				else{
					for(int i=0;i<treeDegree;i++){
						if(children[i]!=null){
							children[i].fillNodesInBox(b, foundNodes);
						}
					}
				}
			}
		}
		
		public void highestInBox(Box b, int coordinate) {
			if(box.isCollidingBox(b)){
				if(children[0]==null){
					highestInBox=this;
				}else{
					for(int i=0;i<treeDegree;i++){
						if(children[i]!=null){
							if(children[i].box.isCollidingBox(b))
								if(b.c[coordinate][0]<children[i].box.c[coordinate][0])b.c[coordinate][0]=children[i].box.c[coordinate][0];
						}
					}
					for(int i=0;i<treeDegree;i++){
						if(children[i]!=null){
							children[i].highestInBox(b, coordinate);
						}
					}
				}
				
			}
		}
		
	}
	
}