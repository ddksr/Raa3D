package main;

import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.DynamicImage;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;

public class ImageWidget extends de.matthiasmann.twl.Widget
{
   private de.matthiasmann.twl.renderer.Image img;
   LWJGLRenderer imageRenderer;
   /**
    * Creates an empty image object.
    * 
    * @param path
    */
   public ImageWidget()
   {

   }

   /**
    * Creates a widget image from the given renderer image. The parameter cannot be null.
    * 
    * @param path
    */
   public ImageWidget(de.matthiasmann.twl.renderer.Image image) //HERE CAN COME DYNAMICIMAGE
   {
      if(image != null)
      {
         img = image;
      }
      else
      {
         throw new IllegalArgumentException();
      }
   }
   
   public ImageWidget(BufferedImage image) //BUFFERIMAGE CONVERTS TO DYNAMICIMAGE
   {
      if(image != null)
      {
          int w = image.getWidth();
          int h = image.getHeight();
          
          ByteBuffer bb = convertImageData(image);
      //ByteBuffer bb2 = getImageDataFromImageNew(bufferedImage);
      
      
      try {
           imageRenderer = new LWJGLRenderer();
      } catch(LWJGLException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
      }
      
      
      DynamicImage dynamicImage = imageRenderer.createDynamicImage(image.getWidth(), image.getHeight());
      dynamicImage.update(bb, DynamicImage.Format.RGBA);
      img = dynamicImage;
      }
      else
      {
         throw new IllegalArgumentException();
      }
   }
   
   
   public ImageWidget(String filename) //BUFFERIMAGE CONVERTS TO DYNAMICIMAGE
   {
      if(filename != null)
      {
          BufferedImage bufferedImage = null;
          int w, h;
          try {
              bufferedImage = ImageIO.read(new File(filename));
              w = bufferedImage.getWidth();
              h = bufferedImage.getHeight();
          } catch (IOException e) {
              e.printStackTrace();
          }
         
          ByteBuffer bb = convertImageData(bufferedImage);
          //ByteBuffer bb2 = getImageDataFromImageNew(bufferedImage);
          
          
          try {
              imageRenderer = new LWJGLRenderer();
          } catch(LWJGLException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
          }
          
          
          DynamicImage dynamicImage = imageRenderer.createDynamicImage(bufferedImage.getWidth(), bufferedImage.getHeight());
          dynamicImage.update(bb, DynamicImage.Format.RGBA);
          img = dynamicImage;
      }
      else
      {
         throw new IllegalArgumentException();
      }
   }
   

   private ByteBuffer convertImageData(BufferedImage bufferedImage) {
       ByteBuffer imageBuffer;
       WritableRaster raster;
       BufferedImage texImage;

       ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace
               .getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 },
               true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

       raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
               bufferedImage.getWidth(), bufferedImage.getHeight(), 4, null);
       texImage = new BufferedImage(glAlphaColorModel, raster, true,
               new Hashtable());

       // copy the source image into the produced image
       Graphics g = texImage.getGraphics();
       g.fillRect(0, 0, 256, 256);
       g.drawImage(bufferedImage, 0, 0, null);

       // build a byte buffer from the temporary image
       // that be used by OpenGL to produce a texture.
       byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer())
               .getData();

       imageBuffer = ByteBuffer.allocateDirect(data.length);
       imageBuffer.order(ByteOrder.nativeOrder());
       imageBuffer.put(data, 0, data.length);
       imageBuffer.flip();

       return imageBuffer;
   }
   /**
    * Sets the image. Can be null.
    * 
    * @param image
    */
   public void setImage(de.matthiasmann.twl.renderer.Image image)
   {
      img = image;
   }
   
   @Override
   public int getPreferredHeight()
   {
      return img.getHeight();
   }
   
   @Override
   public int getPreferredWidth()
   {
      return img.getWidth();
   }

   @Override
   public void paintWidget(GUI gui)
   {
      super.paintWidget(gui);
      if(img != null)
      {
         img.draw(getAnimationState(), getX(), getY(), getWidth(), getHeight());
      }
   }
}
