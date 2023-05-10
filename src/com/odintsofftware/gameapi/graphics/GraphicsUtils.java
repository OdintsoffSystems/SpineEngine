/**
 *
 * @author Ivan
 */

package com.odintsofftware.gameapi.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Ivan Odintsoff
 */
public class GraphicsUtils {

    private static GraphicsUtils instance;

    public enum StretchMethod {
       NO_STRETCH(1),
       DOUBLE_SIZE_STRETCH(2),
       TRIPLE_SIZE_STRETCH(3);
       private final int value;

       private StretchMethod(int value) {
           this.value = value;
       }       
       
       public int getValue() {
           return value;
       }
   }
    
    public static GraphicsUtils getInstance() {
        if (instance == null)
            instance = new GraphicsUtils();
        return instance;
    }

    public static GraphicsDevice getGraphicsDevice() {
       GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
       GraphicsDevice device = env.getDefaultScreenDevice();
       return device;
    }

    public static GraphicsConfiguration getGraphicsConfiguration() {
       GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
       GraphicsConfiguration conf = env.getDefaultScreenDevice().getDefaultConfiguration();
       return conf;
    }

//    public static VolatileImage createVRAMImage(String filename) {
//        BufferedImage bimage = null;
//        VolatileImage img = null;
//        GraphicsConfiguration gc = getGraphicsConfiguration();
//        try {
//            bimage = ImageIO.read(new File(filename));
//            img = GraphicsUtils.createVolatileImage(bimage.getWidth(), bimage.getHeight());
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//	Graphics2D g = null;
//            try {
//                g = img.createGraphics();
//                g.drawImage(img,0,0,null);
//            } finally {
//                // It's always best to dispose of your Graphics objects.
//                g.dispose();
//            }
//        return img;
//    }
//
//    private static VolatileImage createVolatileImage(int width, int height) {
//	GraphicsConfiguration gc = GraphicsUtils.getGraphicsConfiguration();
//	VolatileImage image = null;
//	Graphics2D g = null;
//
//	image = gc.createCompatibleVolatileImage(width, height);
//
//	int valid = image.validate(gc);
//
//	if (valid == VolatileImage.IMAGE_INCOMPATIBLE) {
//		image = GraphicsUtils.createVolatileImage(width, height);
//		return image;
//	}
//
//	return image;
//    }

    public Image createImage(String filename) {
        Image img;
        try {
            img = ImageIO.read(getClass().getResource(filename));
        } catch (IOException e) {
            img = null;
        }
        return img;
    }
    
    public BufferedImage createBufferedImage(String filename) {
        BufferedImage img;
        try {
            img = ImageIO.read(getClass().getResource(filename));
        } catch (IOException e) {
            img = null;
        }
        
        GraphicsConfiguration gfx_c = GraphicsUtils.getGraphicsConfiguration();
        
        if(img.getColorModel().equals(gfx_c.getColorModel()))     
            return img;
        
        BufferedImage compatible_img = gfx_c.createCompatibleImage(img.getWidth(), img.getHeight(), BufferedImage.TRANSLUCENT);
        
        Graphics2D g2 = (Graphics2D) compatible_img.getGraphics();
        g2.drawImage(img, 0, 0, null);
        g2.dispose();
        
        return compatible_img;
    }

}

