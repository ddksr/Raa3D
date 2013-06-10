package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import tools.Vector;

public class ImageOp {
    
    /*
     * Funkcija prebere neko sliko, jo spremeni v sivinsko, nato 
     * pretvori v pm format. Rezultat shrani v datoteko.
     */
    public static void writePMImageToFile(BufferedImage img, String outputFile) {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);  
        ColorConvertOp op = new ColorConvertOp(cs, null);  
        BufferedImage image = op.filter(img, null);
        
        int magicNumber = 1447642455;
        int numberOfPlains = 1;
        int numOfRows = image.getHeight(); 
        int numOfColumns = image.getWidth();
        int numOfBands = 1;
        int pixelFormat = 32769;
        int numOfCommentBytes = 58;
        byte comment = 100;
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(outputFile));
            out.writeInt(magicNumber);
            out.writeInt(numberOfPlains);
            out.writeInt(numOfRows);
            out.writeInt(numOfColumns);
            out.writeInt(numOfBands);
            out.writeInt(pixelFormat);
            out.writeInt(numOfCommentBytes);    
            
            for(int i=0; i<image.getHeight(); i++)
                for(int j=0; j<image.getWidth(); j++) {
                    int rgb = img.getRGB(j, i);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = (rgb & 0xFF);

                    int gray = (r + g + b) / 3;
                    //System.out.println(gray);
                    out.writeByte(gray);
                }
            out.write(comment);
            out.close();
            
            } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /*
     * Funkcija pretvori sliko v �eljen format in jo shrani v datoteko. 
     * Formati: JPG, PNG, BMP, GIF, WBMP
     */
    public static void convertImage(BufferedImage img, String outputFormat, String outputFile) {
        try {
            File file = new File(outputFile);
            ImageIO.write(img, outputFormat.toLowerCase(), file);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    /*
     * Funkcija naredi screenshot iz opengl-a in sliko shrani v neko datoteko tipa PNG, JPG, BMP, WBMP, GIF
     * Parametri: 
     *  1. buffer: buffer iz opengl
     *  2. width: sirina slike
     *  3. height: visina slike
     *  4. bbp: bytes per pixel, tipicno 4 - Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
     *  5. outputFormat: PNG, JPG, BMP, WBMP, GIF
     *  6. outputFile: absolutna pot + ime datoteke npr. c:\neki.jpg
     */
    public static void screenShot(ByteBuffer buffer, int width, int height, int bpp, String outputFormat, String outputFile) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x < width; x++) { 
            for(int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }
        convertImage(image, outputFormat, outputFile);
    }
    
    /**
     * Convert a buffered image to a ByteBuffer for displaying purpuse
     * @param bufferedImage
     * @return
     */
    public static ByteBuffer getImageDataFromImage(BufferedImage bufferedImage)
    {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);

        for(int i=0; i<bufferedImage.getHeight(); i++) {
            for(int j=0; j<bufferedImage.getWidth(); j++) {
                int argb  = bufferedImage.getRGB( j, i );
                byte alpha = (byte) (argb >> 24);
                byte red   = (byte) (argb >> 16);
                byte green = (byte) (argb >> 8);
                byte blue  = (byte) (argb);

                byteBuffer.put(red);
                byteBuffer.put(green);
                byteBuffer.put(blue);
                byteBuffer.put(alpha);
            }
        }
        byteBuffer.flip();

        return byteBuffer;
    }
    
    public static BufferedImage createImageFromLines(LinkedList<float[]> lines3D, double[] normal, int w, int h) {
        float minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, 
                minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        float[][] lines = new float[lines3D.size()][4];
        
        float factor = 1F;
        int i = 0;
        for(float[] line : lines3D) {
            //System.out.println(line[0] + " " + line[1] + " " + line[2] + " " + line[3] + " " + line[4] + " " + line[5]);
            
            
            // generate two base vectors for the plane
            double[] a = {2., 3., -(2.*normal[1] + 2.*normal[1])/normal[2]}; // generate perpendicular vector
            double[] b = Vector.crossProduct(a, normal); // get another perpendicular vector

            // generate 2d lines from 3d lines (project on plane)
            lines[i] = new float[] {
                    factor*(float)(Vector.dotProduct(a, new double[]{line[0], line[1], line[2]})),
                    factor*(float)(Vector.dotProduct(b, new double[]{line[0], line[1], line[2]})),
                    factor*(float)(Vector.dotProduct(a, new double[]{line[3], line[4], line[5]})),
                    factor*(float)(Vector.dotProduct(b, new double[]{line[3], line[4], line[5]}))
            };
            
            // reset bounds
            minX = Math.min(Math.min(lines[i][0], lines[i][2]), minX);
            maxX = Math.max(Math.max(lines[i][0], lines[i][2]), maxX);
            minY = Math.min(Math.min(lines[i][1], lines[i][3]), minY);
            maxY = Math.max(Math.max(lines[i][1], lines[i][3]), maxY);
            i++;
        }
        lines3D = null; // remove from RAM
        
        // get width and height
        
        // generate image
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
        
        // prepare canvas
        Graphics2D canvas = out.createGraphics();
        
        // draw on canvas
        //canvas.setColor(new Color(0));
        System.out.println(minX + " " + maxX + " - " + minY + " " + maxY);
        for(i = 0; i < lines.length; i++) {
            //System.out.println(lines[i][0] + " "+ lines[i][1] + " " + lines[i][2] + " " + lines[i][3] + " ");
            canvas.drawLine(
                    (int)mapper(lines[i][0], minX, maxX, 0, w),
                    (int)mapper(lines[i][1], minY, maxY, 0, h),
                    (int)mapper(lines[i][2], minX, maxX, 0, w),
                    (int)mapper(lines[i][3], minY, maxY, 0, h)
            );
            System.out.println((int)mapper(lines[i][0], minX, maxX, 0, w) +
                    " " + (int)mapper(lines[i][1], minY, maxY, 0, h) + 
                    " " + (int)mapper(lines[i][2], minX, maxX, 0, w) +
                    " " + (int)mapper(lines[i][3], minY, maxY, 0, h));
    
            /*canvas.drawLine(minX + lines[i][0], 
                    minY + lines[i][1], 
                    minX + lines[i][2], 
                    minY + lines[i][3]);*/
        }
        return out;
    }
    
    public static float mapper(float x, float fromMin, float fromMax, float toMin, float toMax) {
        return ((x-fromMin)/(fromMax - fromMin)) * (toMax - toMin) + toMin;
    }
    
}
