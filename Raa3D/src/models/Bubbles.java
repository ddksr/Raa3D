package models;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX;
import static org.lwjgl.opengl.GL11.GL_MODULATE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION_MATRIX;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;

import tools.Matrix;

public class Bubbles {
    static FloatBuffer modelMatrix=BufferUtils.createFloatBuffer(16), projMatrix=BufferUtils.createFloatBuffer(16);
    static IntBuffer viewport=BufferUtils.createIntBuffer(16);
    static Texture bubbleTexture;
    
    public static void getAndSetMatrices(){
        glGetFloat(GL_MODELVIEW_MATRIX, modelMatrix);
        glGetFloat(GL_PROJECTION_MATRIX, projMatrix);
        glGetInteger(GL_VIEWPORT, viewport);
    }
    public static void setTexture(Texture texture){
        bubbleTexture=texture;
    }
    
    public static void drawBubble(float x, float y, float z, String color){
        //find the location of the point on the screen
        float[] xyzAfterTransform = Matrix.transformXYZ(modelMatrix, x, x, z);
        if(xyzAfterTransform[2]>0)return;
        
        glDisable(GL_LIGHTING);
        GL11.glBindTexture(GL_TEXTURE_2D, bubbleTexture.getTextureID());
        glTexEnvf(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_MODULATE);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, viewport.get(2), 0, viewport.get(3), 1f, 10000f);
        
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
        
        FloatBuffer screenPosition=BufferUtils.createFloatBuffer(3);
        GLU.gluProject(x, y, z, modelMatrix, projMatrix, viewport, screenPosition);
        float ndcZ = (2*screenPosition.get(2)) - 1;
        float cameraZ = ((ndcZ + (10000 + 1)/(10000 - 1)) * (10000 - 1))/-2;
        glTranslatef(screenPosition.get(0), screenPosition.get(1), cameraZ);
        glEnable(GL11.GL_DEPTH_TEST);
        
        
        switch(color){
            case "red":glColor4f(1,0.5f,0.5f,1.0f);break;
            case "green":glColor4f(0.5f,1,0.5f,1.0f);break;
            case "blue":glColor4f(0.5f,0.5f,1,1.0f);break;
        }
        
        glEnable(GL11.GL_DEPTH_TEST);
        glBegin(GL_QUADS);
        glTexCoord2f(1, 0);
        glVertex3f(20, 20, 0f);
        glTexCoord2f(0, 0);
        glVertex3f(-20, 20, 0f);
        glTexCoord2f(0, 1);
        glVertex3f(-20, -20, 0f);
        glTexCoord2f(1, 1);
        glVertex3f(20, -20, 0f);
        glEnd();
        glEnable(GL_TEXTURE_2D);
        
        glLoadIdentity();
        glTranslatef(screenPosition.get(0), screenPosition.get(1), -10);
        glDisable(GL11.GL_DEPTH_TEST);
        
        //draw
        glBegin(GL_QUADS);
        glTexCoord2f(1, 0);
        glVertex3f(20, 20, 0f);
        glTexCoord2f(0, 0);
        glVertex3f(-20, 20, 0f);
        glTexCoord2f(0, 1);
        glVertex3f(-20, -20, 0f);
        glTexCoord2f(1, 1);
        glVertex3f(20, -20, 0f);
        glEnd();
        glEnable(GL11.GL_DEPTH_TEST);
        
        glDisable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LIGHTING);
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        
    }
}
