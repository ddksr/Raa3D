package main;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

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
     * Funkcija pretvori sliko v željen format in jo shrani v datoteko. 
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
    
}
