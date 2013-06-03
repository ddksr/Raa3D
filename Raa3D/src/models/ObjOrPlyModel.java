/* Author of this file: Simon �agar, 2012, Ljubljana
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
 */
package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.StringTokenizer;
import static org.lwjgl.opengl.ARBBufferObject.*;
import static org.lwjgl.opengl.ARBVertexBufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static tools.Tools.allocFloats;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.smurn.jply.ElementReader;
import org.smurn.jply.PlyReader;
import org.smurn.jply.PlyReaderFile;
import org.smurn.jply.util.NormalMode;
import org.smurn.jply.util.NormalizingPlyReader;
import org.smurn.jply.util.TesselationMode;
import org.smurn.jply.util.TextureMode;

import tools.Vector;


/**
 * @author Simon �agar
 *@since 0.2
 *@version 0.2
 */
public class ObjOrPlyModel {
	// file formats for objects
	private static final int FILE_FORMAT_OBJ=0;
	private static final int FILE_FORMAT_PLY=1;
	private int fileFormat=-1;
	
	//global variables
	//     used for both .obj and .ply formats
	public double centerx, centery, centerz;
	public float maxX, maxY, maxZ;
	public float minX, minY, minZ;
	
	
	//     used for .obj files
	ArrayList<Mesh> meshes;
	private int numberOfSubdivisions=0;
	private int maxSubDepth=0;
	int aplicationSubdivisionLimit=1; //limit of how many subdivisions are allowed max
	ArrayList<Float> vertices;
	
	//     used for .ply files
	private int indexBufferName_forPlyFiles;
	private int vertexBufferName_forPlyFiles;
	int triangleCount_forPlyFiles;
	private int vertexSize_forPlyFiles;
	int triangleSize_forPlyFiles;
	
	RTree rtreeOfTriangles_forPlyFiles;
	//FloatBuffer verticesNormals_forPlyFiles;
	//IntBuffer indexes_forPlyFiles;
	/*
	 * Plain
	 */
	boolean plainVisible=false;
	float plainX=0, plainY=0, plainZ=0;
	float rotate = 0;
	
	public ObjOrPlyModel(String filepath){
	    vertices = new ArrayList<Float>();
	    int tempFaceCount=0;
	    maxX=Float.MIN_VALUE; maxY=Float.MIN_VALUE; maxZ=Float.MIN_VALUE;
	    minX=Float.MAX_VALUE; minY=Float.MAX_VALUE; minZ=Float.MAX_VALUE;
	    centerx=0; centery=0; centerz=0;
	    
	    if(filepath.endsWith(".obj")){
	        meshes=new ArrayList<ObjOrPlyModel.Mesh>();
	        fileFormat=FILE_FORMAT_OBJ;
	        File file = new File(filepath);
	        Scanner scanner;
	        float x,y,z;
	        boolean newG=false;
	        ArrayList<Integer> tempFaces= new ArrayList<Integer>();
	        ArrayList<String> groups= new ArrayList<String>();
	        try {
	            scanner = new Scanner(file);
	            String type;
	            String line;
	            while(scanner.hasNext()){
	                line=scanner.nextLine();
	                StringTokenizer strTokenizer= new StringTokenizer(line);
	                type=strTokenizer.nextToken();
	                if(type.equalsIgnoreCase("v")){
	                    vertices.add(x=Float.parseFloat(strTokenizer.nextToken()));
	                    vertices.add(y=Float.parseFloat(strTokenizer.nextToken()));
	                    vertices.add(z=Float.parseFloat(strTokenizer.nextToken()));
	                    centerx+=x;centery+=y;centerz+=z;
	                    if(x<minX)minX=x;
	                    if(y<minY)minY=y;
	                    if(z<minZ)minZ=z;
	                    if(x>maxX)maxX=x;
	                    if(y>maxY)maxY=y;
	                    if(z>maxZ)maxZ=z;
	                }else if(type.equalsIgnoreCase("f")){
	                    int a, b, c;
	                    StringTokenizer tok=new StringTokenizer(strTokenizer.nextToken(),"//");
	                    a=Integer.parseInt(tok.nextToken());
	                    tok=new StringTokenizer(strTokenizer.nextToken(),"//");
	                    b=Integer.parseInt(tok.nextToken());
	                    tok=new StringTokenizer(strTokenizer.nextToken(),"//");
	                    c=Integer.parseInt(tok.nextToken());
	                            
	                    tempFaces.add(c);
	                    tempFaces.add(b);
	                    tempFaces.add(a);
	                    
	                    tempFaceCount++;
	                }else if(type.equalsIgnoreCase("g")){
	                    if(tempFaceCount>0){
	                        //It seems that since last starting a new group, there have been faces stored
	                        //Here I create a new mesh
	                        Mesh mesh=new Mesh(groups, tempFaces, vertices);
	                        meshes.add(mesh);
	                        //After the whole file will be read, each mesh object's faces will be stored as VBOs (Vertex Buffer Objects).
	                        System.out.println("Created a new mesh java object that will have it's own VBO.");
	                    }else if(newG)System.out.println("One \"g\" holding 0 elements discarted.");
	                    //start a new group
	                    newG=true;
	                    groups = new ArrayList<String>();
	                    tempFaces= new ArrayList<Integer>();
	                    tempFaceCount=0;
	                    while(strTokenizer.hasMoreTokens()){
	                        groups.add(strTokenizer.nextToken());
	                    }
	                }
	            }
	            if(tempFaceCount>0){
	              //It seems that since last starting a new group, there have been faces stored
	                //Here I create a new mesh
	                Mesh mesh=new Mesh(groups, tempFaces, vertices);
	                meshes.add(mesh);
	                //After the whole file will be read, each mesh object's faces will be stored as VBOs (Vertex Buffer Objects).
	                System.out.println("Created a new mesh java object that will have it's own VBO.");
	            }else{System.out.println("One \"g\" holding 0 elements discarted.");}
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
	        //The file has been read.
	        centerx/=(vertices.size()/3);centery/=(vertices.size()/3);centerz/=(vertices.size()/3);
	        
	        for(Mesh mesh:meshes){
	            mesh.constructVBO();
	        }
	    }else if(filepath.endsWith(".ply")){
	        fileFormat=FILE_FORMAT_PLY;
	        File file = new File(filepath);
	        try {
	            // source: file:///C:/Users/SkullMage/Desktop/LWJGLDemo.html (May 14th, 2013)
	            // opens .ply file
                PlyReader plyReader = new PlyReaderFile(file);
                // normalizes the data in the PLY file to ensure only triangles are gotten and that normals are assigned
                plyReader = new NormalizingPlyReader(plyReader, TesselationMode.TRIANGLES, NormalMode.ADD_NORMALS_CCW, TextureMode.PASS_THROUGH);
                int vertexCount = plyReader.getElementCount("vertex");
                triangleCount_forPlyFiles = plyReader.getElementCount("face");
                vertexSize_forPlyFiles = 3*4 + 3*4 + 4*4;// 32 bit float per each: location coordinate, normal coordinates and color component
                triangleSize_forPlyFiles = 3*4;// indexes to vertices as 32 bit integers
                
                vertexBufferName_forPlyFiles = glGenBuffersARB(); // return a new buffer object name
                glBindBufferARB(GL_ARRAY_BUFFER_ARB, vertexBufferName_forPlyFiles); // bind buffer to say it is the one the following operations apply to
                glBufferDataARB(GL_ARRAY_BUFFER_ARB, vertexCount * vertexSize_forPlyFiles, GL_STATIC_DRAW_ARB); // creates a new buffer to the bounded buffer with specified space and hints it's usage
                FloatBuffer vertexBuffer = glMapBufferARB(GL_ARRAY_BUFFER_ARB, GL_WRITE_ONLY_ARB, null).asFloatBuffer();
                //verticesNormals_forPlyFiles = BufferUtils.createFloatBuffer(vertexCount*2*3);
                
                indexBufferName_forPlyFiles = glGenBuffersARB(); // agan, return a new buffer object name
                glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, indexBufferName_forPlyFiles);
                glBufferDataARB(GL_ELEMENT_ARRAY_BUFFER_ARB, triangleCount_forPlyFiles * triangleSize_forPlyFiles, GL_STATIC_DRAW_ARB);
                IntBuffer indexBuffer = glMapBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, GL_WRITE_ONLY_ARB, null).asIntBuffer();
                //indexes_forPlyFiles = BufferUtils.createIntBuffer(triangleCount_forPlyFiles*3);
                
                
                // read PLY data into the buffers
                ElementReader reader = plyReader.nextElementReader();
                while (reader != null) {
                    if(reader.getElementType().getName().equals("vertex")) {
                        org.smurn.jply.Element vertex = reader.readElement();
                        while (vertex != null) {
                            float x = (float)vertex.getDouble("x");
                            float y = (float)vertex.getDouble("y");
                            float z = (float)vertex.getDouble("z");
                            float nx = (float)vertex.getDouble("nx");
                            float ny = (float)vertex.getDouble("ny");
                            float nz = (float)vertex.getDouble("nz");
                            float r = (float)vertex.getDouble("red")/255f;
                            float g = (float)vertex.getDouble("green")/255f;
                            float b = (float)vertex.getDouble("blue")/255f;
                            float a = (float)vertex.getDouble("alpha")/255f;
                            
                            //System.out.println(r+", "+g+", "+b+", "+a);
                            /*
                            verticesNormals_forPlyFiles.put((float)x);
                            verticesNormals_forPlyFiles.put((float)y);
                            verticesNormals_forPlyFiles.put((float)z);
                            verticesNormals_forPlyFiles.put((float)nx);
                            verticesNormals_forPlyFiles.put((float)ny);
                            verticesNormals_forPlyFiles.put((float)nz);
                            */
                            vertexBuffer.put(x);
                            vertexBuffer.put(y);
                            vertexBuffer.put(z);
                            vertexBuffer.put(nx);
                            vertexBuffer.put(ny);
                            vertexBuffer.put(nz);
                            vertexBuffer.put(r);
                            vertexBuffer.put(g);
                            vertexBuffer.put(b);
                            vertexBuffer.put(a);
                            
                            /*
                            trianglesForRTree[trianglesForRTreeIdx++]=x;
                            trianglesForRTree[trianglesForRTreeIdx++]=y;
                            trianglesForRTree[trianglesForRTreeIdx++]=z;
                            */
                            /*
                            vertices.add((float)x);//TODO: remove if not necessary
                            vertices.add((float)y);//TODO: remove if not necessary
                            vertices.add((float)z);//TODO: remove if not necessary
                             */
                            minX = Math.min(minX, x);
                            minY = Math.min(minY, y);
                            minZ = Math.min(minZ, z);
                            maxX = Math.max(maxX, x);
                            maxY = Math.max(maxY, y);
                            maxZ = Math.max(maxZ, z);
                            centerx+=x;centery+=y;centerz+=z;
                            
                            vertex = reader.readElement();
                        }
                        
                    } else if(reader.getElementType().getName().equals("face")) {
                        org.smurn.jply.Element triangle = reader.readElement();
                        while (triangle != null) {
                            int[] indices = triangle.getIntList("vertex_index");
                            for(int index : indices) {
                                indexBuffer.put(index);
                                //indexes_forPlyFiles.put(index);
                            }
                            triangle = reader.readElement();
                        }
                    }
                    reader.close();
                    reader = plyReader.nextElementReader();
                }
                // unmap buffers
                glUnmapBufferARB(GL_ARRAY_BUFFER_ARB);
                glUnmapBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB);
                
                //verticesNormals_forPlyFiles.rewind();
                //indexes_forPlyFiles.rewind();
                //glBufferDataARB(GL_ARRAY_BUFFER_ARB, verticesNormals_forPlyFiles, GL_STATIC_DRAW_ARB);
                //glBufferDataARB(GL_ELEMENT_ARRAY_BUFFER_ARB, indexes_forPlyFiles, GL_STATIC_DRAW_ARB);
                
                centerx/=vertexCount;centery/=vertexCount;centerz/=vertexCount;
                
                System.out.println("RTree started");
                System.gc();
                //prepare a double[] array for RTree (used for searching ray intersections (not needed for simple rendering))
                reader=null;
                plyReader=null;
                vertexBuffer.rewind();
                indexBuffer.rewind();
                float[] trianglesForRTree = new float[triangleCount_forPlyFiles*9];
                int trianglesForRTreeIdx=0;
                while(vertexBuffer.hasRemaining()){
                    trianglesForRTree[trianglesForRTreeIdx++]=vertexBuffer.get();
                    trianglesForRTree[trianglesForRTreeIdx++]=vertexBuffer.get();
                    trianglesForRTree[trianglesForRTreeIdx++]=vertexBuffer.get();
                    for(int i=0;i<7;i++){
                        vertexBuffer.get();
                    }
                }
                vertexBuffer=null;
                rtreeOfTriangles_forPlyFiles = new RTree(trianglesForRTree, indexBuffer, 4);
                indexBuffer=null;
                System.gc();
                System.out.println("RTree constructed");
                
                
            } catch(IOException e) {
                e.printStackTrace();
            }
	    }
	    
	}
	
	
	public void increaseSubdivisionDepth() {
	    numberOfSubdivisions=Math.min(aplicationSubdivisionLimit, numberOfSubdivisions+1);
	    if(maxSubDepth<numberOfSubdivisions){
	        for(Mesh mesh:meshes)mesh.increaseMaxSubdivision();
	        maxSubDepth++;
	    }
	}
	public void decreaseSubdivisionDepth(){
	    numberOfSubdivisions=Math.max(0, numberOfSubdivisions-1);
	}
	
	
	/**
     * @since 0.2
     * @version 0.4
     */
	public void render(int program){
	    
	    if(program==-1){
            GL20.glUseProgram(0);
        }else{
            GL20.glUseProgram(program);
            int myUniformLocation = glGetUniformLocation(program, "bloodColor");
            glUniform4f(myUniformLocation, 0.8f, 0.06667f, 0.0f, 1);
        }
	    
	    
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glTranslatef(-(float)centerx,-(float)centery,-(float)centerz);
		glDisable(GL11.GL_CULL_FACE);
		if(fileFormat==FILE_FORMAT_OBJ){
		    for(Mesh vbo:meshes){
		        vbo.render(numberOfSubdivisions);
		    }
		}else if(fileFormat==FILE_FORMAT_PLY){
		    // add if for if the colors are present
            int myAttributeLocationForColors = GL20.glGetAttribLocation(program, "perVertexColor");
            GL20.glEnableVertexAttribArray(myAttributeLocationForColors);
            org.lwjgl.opengl.ARBBufferObject.glBindBufferARB(GL_ARRAY_BUFFER_ARB, vertexBufferName_forPlyFiles);
            GL20.glVertexAttribPointer(myAttributeLocationForColors, 4, GL11.GL_FLOAT, true, 10*4, 6*4);
            // add locations
            int myAttributeLocationForLocations = GL20.glGetAttribLocation(program, "perVertexLocation");
            GL20.glEnableVertexAttribArray(myAttributeLocationForLocations);
            org.lwjgl.opengl.ARBBufferObject.glBindBufferARB(GL_ARRAY_BUFFER_ARB, vertexBufferName_forPlyFiles);
            GL20.glVertexAttribPointer(myAttributeLocationForLocations, 3, GL11.GL_FLOAT, true, 10*4, 0);
            
            // add min and max z
            int attributeLocationForMinMaxZ = GL20.glGetUniformLocation(program, "minMaxZ");
            GL20.glUniform2f(attributeLocationForMinMaxZ, (float)minZ, (float)maxZ);
		    
		    //GL11.glEnable(GL31.GL_PRIMITIVE_RESTART);
		    GL11.glEnable(GL15.GL_ARRAY_BUFFER_BINDING);
		    GL11.glEnable(GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING);
            
		    glBindBufferARB(GL_ARRAY_BUFFER_ARB, vertexBufferName_forPlyFiles);
		    glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, indexBufferName_forPlyFiles);
		    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		    glVertexPointer(3, GL11.GL_FLOAT, vertexSize_forPlyFiles, 0);
		    
		    GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		    glNormalPointer(GL11.GL_FLOAT, vertexSize_forPlyFiles, 12);
		    
		    GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		    glColorPointer(4, GL11.GL_FLOAT, vertexSize_forPlyFiles, 24);
		    
		    //org.lwjgl.opengl.Util.checkGLError();
		    glDrawElements(GL_TRIANGLES, triangleCount_forPlyFiles * 3, GL_UNSIGNED_INT, 0);
		    //org.lwjgl.opengl.Util.checkGLError();
		    if(plainVisible) {
		        drawPlain(1);
		    }
		    
		}
		Bubbles.getAndSetMatrices();
		glPopMatrix();		
	}
	
	public void drawPlain(int i) {
	    glPushMatrix();
	    glTranslatef((float)centerx,(float)centery,(float)centerz);
	    glRotatef((float)(rotate*Math.PI/180), 0, 1, 0);
	    glTranslatef(-(float)centerx,-(float)centery,-(float)centerz);
	    
	    float maxxx = Float.MIN_VALUE;
	    if(minX>maxxx) maxxx=minX;
        if(maxX>maxxx) maxxx=maxX;
        if(minY>maxxx) maxxx=minY;
        if(maxY>maxxx) maxxx=maxY;
        if(minZ>maxxx) maxxx=minZ;
        if(maxZ>maxxx) maxxx=maxZ;
	    
        glTranslatef(0,0,(float)plainZ);
        
         glBegin(GL11.GL_QUADS);
         glVertex3f(-maxxx, -maxxx,(float)centerz);
         glVertex3f(maxxx, -maxxx,(float)centerz);
         glVertex3f(maxxx, maxxx,(float)centerz);
         glVertex3f(-maxxx, maxxx,(float)centerz);
          glEnd();
        
        glPopMatrix();
	}
	
	
	public static float[] getNormals(ArrayList<Float> vertices, ArrayList<Integer> faces){
	    float[] normals= new float[vertices.size()];
	    for(int f=0;f<faces.size()/3;f++){
	        int a=faces.get(f*3)-1;
	        int b=faces.get(f*3+1)-1;
	        int c=faces.get(f*3+2)-1;
	        
	        double[]A=new double[]{vertices.get(a*3),vertices.get(1+a*3),vertices.get(2+a*3)};
	        double[]B=new double[]{vertices.get(b*3),vertices.get(1+b*3),vertices.get(2+b*3)};
	        double[]C=new double[]{vertices.get(c*3),vertices.get(1+c*3),vertices.get(2+c*3)};
	        
	        double[]AB=Vector.subtraction(B, A);
	        double[]AC=Vector.subtraction(C, A);
	        double[]n=Vector.crossProduct(AB, AC);
	        n=Vector.normalize(n);
	        
	        
	        normals[a*3]+=n[0]; normals[a*3+1]+=n[1]; normals[a*3+2]+=n[2];
	        normals[b*3]+=n[0]; normals[b*3+1]+=n[1]; normals[b*3+2]+=n[2];
	        normals[c*3]+=n[0]; normals[c*3+1]+=n[1]; normals[c*3+2]+=n[2];
	    }
	    for(int normal=0;normal<normals.length/3;normal++){
	        double[] n= new double[]{normals[normal*3], normals[normal*3+1], normals[normal*3+2]};
	        n=Vector.normalize(n);
	        normals[normal*3]=(float)n[0];
	        normals[normal*3+1]=(float)n[1];
	        normals[normal*3+2]=(float)n[2];
	    }
	    return normals;
	}
	

    /**
	 * The VBO class I made holds ID for indices to faces (and also their count/amount) and ID to vertices and normals. It also remembers the belongings to groups of the actual VBO it refers to.
	 * During the parsing of the .obj file, each object of the VBO class briefly holds an array of integers as faces (in my case: 3 integers = 1 face), it than writes and actual VBO into the graphics card.
	 * @author Simon �agar
	 * @since 0.2
	 * @version 0.2
	 */
	class Mesh{
	    ArrayList<Float> vertices;
	    ArrayList<Float> normals;
	    ArrayList<Integer> faces;
		ArrayList<String> groups;//groups this VBO belongs to
		
		int maxSubDepth;//maximal number of subdivisions able to render
		ArrayList<Integer> verticesAndNormalsIDs;
		ArrayList<Integer> verticesCounters;
		ArrayList<Integer> indicesIDs;
		ArrayList<Integer> facesCounters;
		
		public Mesh(ArrayList<String> groups, ArrayList<Integer> faces, ArrayList<Float> vertices){
		    maxSubDepth=-1;
		    verticesAndNormalsIDs= new ArrayList<Integer>();
		    verticesCounters = new ArrayList<Integer>();
		    indicesIDs = new ArrayList<Integer>();
		    facesCounters = new ArrayList<Integer>();
			this.groups=groups;
			this.faces=faces;
			this.vertices=vertices;
		}
		
		public void increaseMaxSubdivision(){
		    //Disclaimer: This will change the location of vertices used in other VBOs as well. (At the writing of the program, that's not an issue).
		    ArrayList<Float> Fs=new ArrayList<Float>();
		    ArrayList<PointNeighbours> allNeighbours= new ArrayList<ObjOrPlyModel.Mesh.PointNeighbours>();
		    for(int i=0;i<vertices.size()/3;i++)allNeighbours.add(new PointNeighbours());
		    
		    for(int f=0;f<faces.size()/3;f++){//f is quasi-index of face with three points
		        int a=faces.get(f*3)-1;//quasi-index of point a
	            int b=faces.get(f*3+1)-1;//quasi-index of point b
	            int c=faces.get(f*3+2)-1;// quasi-index of point c
	            Fs.add((vertices.get(a*3)+vertices.get(b*3)+vertices.get(c*3))/3);
	            Fs.add((vertices.get(a*3+1)+vertices.get(b*3+1)+vertices.get(c*3+1))/3);
	            Fs.add((vertices.get(a*3+2)+vertices.get(b*3+2)+vertices.get(c*3+2))/3);
	       
	            allNeighbours.get(a).faces.add(f);
	            allNeighbours.get(b).faces.add(f);
	            allNeighbours.get(c).faces.add(f);
	            allNeighbours.get(a).points.add(b);
	            allNeighbours.get(a).points.add(c);
	            allNeighbours.get(b).points.add(a);
                allNeighbours.get(b).points.add(c);
                allNeighbours.get(c).points.add(b);
                allNeighbours.get(c).points.add(a);
                
                allNeighbours.get(a).pointsOriginal.add(b);
                allNeighbours.get(a).pointsOriginal.add(c);
                allNeighbours.get(b).pointsOriginal.add(c);
                allNeighbours.get(b).pointsOriginal.add(a);
                allNeighbours.get(c).pointsOriginal.add(a);
                allNeighbours.get(c).pointsOriginal.add(b);
		    }
		    
		    //Create edge points
		    ArrayList<FaceEdgePoints> facesEdges= new ArrayList<ObjOrPlyModel.Mesh.FaceEdgePoints>();
		    for(int i=0;i<faces.size()/3;i++)facesEdges.add(new FaceEdgePoints());
		    ArrayList<Float> Es=new ArrayList<Float>();
		    int eIndex=0;
		    for(int point1=0; point1<allNeighbours.size(); point1++){
		        PointNeighbours point1Neighbours=allNeighbours.get(point1);
		        for(int point2:point1Neighbours.points){
		            PointNeighbours point2Neighbours=allNeighbours.get(point2);
		            int faceOne=-1;
		            int faceTwo=-1;
		            
		            for(int face1:point1Neighbours.faces){
		                for(int face2:point2Neighbours.faces){
		                    if(face1==face2){
		                        if(faceOne==-1)faceOne=face1;
		                        else if(faceTwo==-1)faceTwo=face1;
		                    }
		                }
		            }
		            if(faceOne!=-1 && faceTwo!=-1){
		                float[] P1, P2, F1, F2;
		                P1= new float[]{vertices.get(point1*3), vertices.get(point1*3+1), vertices.get(point1*3+2)};
		                P2= new float[]{vertices.get(point2*3), vertices.get(point2*3+1), vertices.get(point2*3+2)};
		                F1= new float[]{Fs.get(faceOne*3), Fs.get(faceOne*3+1), Fs.get(faceOne*3+2)};
		                F2= new float[]{Fs.get(faceTwo*3), Fs.get(faceTwo*3+1), Fs.get(faceTwo*3+2)};
		                Es.add((P1[0]+P2[0]+F1[0]+F2[0])/4);
		                Es.add((P1[1]+P2[1]+F1[1]+F2[1])/4);
		                Es.add((P1[2]+P2[2]+F1[2]+F2[2])/4);
		                //point1Neighbours.points.remove(new Integer(point2));
		                if(!point2Neighbours.points.remove(new Integer(point1))){
		                    System.out.println("Removed a non-existing point");
		                };
		                
		                //find out when in a face the edge is
		                //faceOne
		                int a=faces.get(faceOne*3)-1;//quasi-index of point a
		                int b=faces.get(faceOne*3+1)-1;//quasi-index of point b
		                if(a==point1 || a==point2){
		                    if(b==point1 || b==point2)facesEdges.get(faceOne).e1=eIndex;
		                    else facesEdges.get(faceOne).e3=eIndex;
		                }else  facesEdges.get(faceOne).e2=eIndex;
		              //faceTwo
                        a=faces.get(faceTwo*3)-1;//quasi-index of point a
                        b=faces.get(faceTwo*3+1)-1;//quasi-index of point b
                        if(a==point1 || a==point2){
                            if(b==point1 || b==point2)facesEdges.get(faceTwo).e1=eIndex;
                            else facesEdges.get(faceTwo).e3=eIndex;
                        }else  facesEdges.get(faceTwo).e2=eIndex;
                        eIndex++;
		            }
		        }
		    }
		    
		    ArrayList<Float> newOriginal= new ArrayList<Float>();
		    //Now the extra points have been submitted
		    //We need to recalculate new vertices values
		    for(int point1=0;point1<vertices.size()/3;point1++){
		        PointNeighbours point1Neighbours=allNeighbours.get(point1);//still contains the faces we need
		        int f=0;
		        float[] F=new float[3];
		        for(int face1:point1Neighbours.faces){
		            f++;
		            F[0]+=Fs.get(face1*3);
		            F[1]+=Fs.get(face1*3+1);
		            F[2]+=Fs.get(face1*3+2);
		        }
		        F[0]/=f;F[1]/=f;F[2]/=f;
		        
		        float[] R=new float[3];
		        float[] P=new float[]{vertices.get(point1*3), vertices.get(point1*3+1), vertices.get(point1*3+2)};
		        int e=0;
		        for(int point2:point1Neighbours.pointsOriginal){
		            e++;
		            R[0]+=(vertices.get(point2*3)+P[0])/2;
		            R[1]+=(vertices.get(point2*3+1)+P[1])/2;
		            R[2]+=(vertices.get(point2*3+2)+P[2])/2;
		        }
		        R[0]/=e;R[1]/=e;R[2]/=e;
		        if(e==f){//This means there are as many edges as there are faces. In other cases the formula can't be used and points aren't moved.
		            newOriginal.add((F[0]+2*R[0]+(f-3)*P[0])/f);
		            newOriginal.add((F[1]+2*R[1]+(f-3)*P[1])/f);
		            newOriginal.add((F[2]+2*R[2]+(f-3)*P[2])/f);
		        }else{
		            newOriginal.add(P[0]);
		            newOriginal.add(P[1]);
		            newOriginal.add(P[2]);
		        }
		    }
		    
		    vertices=new ArrayList<Float>();
		    for(float f:newOriginal)vertices.add(f);
		    for(float f:Fs)vertices.add(f);
		    for(float f:Es)vertices.add(f);
		    
		    ArrayList<Integer> newFaces= new ArrayList<Integer>();
		    //Now create new indexes for the new faces
		    for(int f=0;f<faces.size()/3;f++){
		        FaceEdgePoints edgePoints = facesEdges.get(f);
		        int e1=newOriginal.size()/3+Fs.size()/3+edgePoints.e1;
		        int e2=newOriginal.size()/3+Fs.size()/3+edgePoints.e2;
		        int e3=newOriginal.size()/3+Fs.size()/3+edgePoints.e3;
		        int a=faces.get(f*3)-1;//quasi-index of point a
                int b=faces.get(f*3+1)-1;//quasi-index of point b
                int c=faces.get(f*3+2)-1;// quasi-index of point c
                int fPoint=newOriginal.size()/3+f;
                if(edgePoints.e1!=-1){
                    newFaces.add(a);
                    newFaces.add(e1);
                    newFaces.add(fPoint);
                    
                    newFaces.add(b);
                    newFaces.add(fPoint);
                    newFaces.add(e1);
                }else{
                    newFaces.add(a);
                    newFaces.add(b);
                    newFaces.add(fPoint);
                }
                if(edgePoints.e2!=-1){
                    newFaces.add(b);
                    newFaces.add(e2);
                    newFaces.add(fPoint);
                    
                    newFaces.add(c);
                    newFaces.add(fPoint);
                    newFaces.add(e2);
                    
                }else{
                    newFaces.add(b);
                    newFaces.add(c);
                    newFaces.add(fPoint);
                }
                if(edgePoints.e3!=-1){
                    newFaces.add(c);
                    newFaces.add(e3);
                    newFaces.add(fPoint);
                    
                    newFaces.add(a);
                    newFaces.add(fPoint);
                    newFaces.add(e3);
                    
                }else{
                    newFaces.add(c);
                    newFaces.add(a);
                    newFaces.add(fPoint);
                }
		    }
		    faces= new ArrayList<Integer>();
		    for(int i:newFaces)faces.add(i+1);
		    constructVBO();
		}
		
		public void constructVBO(){
		    maxSubDepth++;
		    verticesAndNormalsIDs.add(glGenBuffersARB());
		    verticesCounters.add(vertices.size());
		    indicesIDs.add(glGenBuffersARB());
		    facesCounters.add(faces.size());
		    
		    glBindBufferARB(GL_ARRAY_BUFFER_ARB, verticesAndNormalsIDs.get(maxSubDepth));
		    glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, indicesIDs.get(maxSubDepth));
		    float[] normals = getNormals(vertices, faces);
		    FloatBuffer verticesAndNormalsBuffer = BufferUtils.createFloatBuffer(vertices.size()+normals.length);
		    for(float f:vertices)verticesAndNormalsBuffer.put(f);
		    for(float f:normals)verticesAndNormalsBuffer.put(f);
		    verticesAndNormalsBuffer.rewind();
		    IntBuffer facesBuffer = BufferUtils.createIntBuffer(faces.size());
		    for(int i:faces)facesBuffer.put(i-1);
		    facesBuffer.rewind();
		    
		    glBufferDataARB(GL_ARRAY_BUFFER_ARB, verticesAndNormalsBuffer, GL_STATIC_DRAW_ARB);
		    glBufferDataARB(GL_ELEMENT_ARRAY_BUFFER_ARB, facesBuffer, GL_STATIC_DRAW_ARB);
		    System.out.println("Finished building a VBO. Faces: "+(faces.size()/3)+" Vertices: "+ (vertices.size()/3));
		}
		
		public void render(int subDepth){
		    if(maxSubDepth<subDepth)return;
			glBindBufferARB(GL_ARRAY_BUFFER_ARB, verticesAndNormalsIDs.get(subDepth));
			glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, indicesIDs.get(subDepth));
			
			glEnableClientState(GL_VERTEX_ARRAY);
			glVertexPointer(3, GL_FLOAT, 0, 0);
			glEnableClientState(GL_NORMAL_ARRAY);
			glNormalPointer(GL_FLOAT, 0, (4*verticesCounters.get(subDepth)));
			
			glDrawElements(GL_TRIANGLES, facesCounters.get(subDepth), GL_UNSIGNED_INT, 0);
			
			if(plainVisible) {
                drawPlain(1);
            }
		}
		
		class FaceEdgePoints{
		    int e1=-1, e2=-1, e3=-1;
		}
		class PointNeighbours{
		    public PointNeighbours(){
		        faces = new HashSet<Integer>();
		        points = new HashSet<Integer>();
		        pointsOriginal = new LinkedHashSet<Integer>();
		    }
		    HashSet<Integer> faces;
		    HashSet<Integer> points;
		    LinkedHashSet<Integer> pointsOriginal;//These points are used for changing the original points' positions
		}
	}
	
	public boolean changePlainState() {
        if(plainVisible)
            plainVisible = false;
        else
            plainVisible = true;
        return plainVisible;
    }
	
	public void incPlain(int coordinate, float value){
	    if(coordinate==0) //z
	        plainZ+=value;
	}
	public void rotatePlain(int coordinate, float value){
        if(coordinate==0) //z
            rotate +=value;
    }
}


/**
TODO: add Apache License, whatever you must for 
    - file:///C:/Users/SkullMage/Desktop/LWJGLDemo.html
    - http://jply.smurn.org/lwjgldemo/xref/org/smurn/jply/lwjgldemo/RectBounds.html
    - jply library
*/