package com.odintsofftware.gameapi.scene.splashscreen;

import com.odintsofftware.gameapi.core.Scene;
import com.odintsofftware.gameapi.graphics.GraphicsUtils;
import com.odintsofftware.gameapi.graphics.Sprite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A kind of OdinCanvas that automaticaly do Fade In/Fade Out effects
 * for a given Image.
*/
public class ImageFadeSplash extends Scene {

    private long pause;
    private float alpha = 0.00f;
    private boolean fadeIn = true;
    private boolean slept = false;
    private boolean finished = false;
    private Composite alphaComp; 
    private Color bgColor;
    //private Image image;
    private int X;
    private int Y;
    private float steps = 0;
    private int maxSteps = 100;
    private Sprite image;
    private String imagePath;
    
    public ImageFadeSplash(String imagePath, Color bgColor, long pause) {       
        this.imagePath = imagePath;
        this.pause = pause;
        this.bgColor = bgColor;
    }
    
    public ImageFadeSplash(String imagePath, Color bgColor, long pause, int maxSteps) {       
        this.imagePath = imagePath;
        this.pause = pause;
        this.bgColor = bgColor;
        this.maxSteps = maxSteps;
    }
    
    @Override
    public void initialize() {    
        this.image = new Sprite(getGame(), GraphicsUtils.getInstance().createBufferedImage(imagePath));
        image.setPosition(getGame().getMiddleX() - image.getWidth() / 2, getGame().getMiddleY() - image.getHeight() / 2);       
    }

    @Override
    public void tick(int delta) {         
        
            if (fadeIn) {
                if (alpha < 1f) { alpha = ++steps / maxSteps; }
                else if (alpha > 0.90f) { alpha = 1.0f; fadeIn = false;}
            }
            else {            

                if (!slept) {
                    try {
                        Thread.sleep(pause);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ImageFadeSplash.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally {
                        slept = true;
                    }
                }
                else {
                    if (alpha > 0f) { alpha = --steps / maxSteps; }
                    else if (alpha < 0.01f) { finishScene(); }
                }
            }

       
    }

    @Override
    public void render() {
        Graphics2D g2 = getGraphics();

        g2.setColor(bgColor);
        g2.fillRect(0,0, getGame().getRenderingResolution().width, getGame().getRenderingResolution().height);
        image.setAlpha(alpha);        
        image.draw();
    }

    
}
