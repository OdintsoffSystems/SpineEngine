/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odintsofftware.gameapi.scene.splashscreen;

import com.odintsofftware.gameapi.core.Scene;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author Ivan
 */
public abstract class FadeToColorSplash extends Scene {
    
    private Color targetColor;
    private long pause;
    private boolean fadeIn = true;
    private int alpha = 0;
    private Image image;
        
    public FadeToColorSplash(Image image, Color color, long pause) {
        this.targetColor = new Color(color.getRGB());
    }

    @Override
    public void tick(int delta) {
        if (fadeIn) {
            if (alpha < 255) { alpha++; }
            else if (alpha == 255) { fadeIn = false;}
        }
        else {
            if (alpha > 0) { alpha--; }
            else if (alpha == 0) { animationFinished(); }
        }
        targetColor = new Color(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue(), alpha);
    }

    @Override
    public void render()
    {     
        Graphics g = getGraphics();
//            g2.setPaint(targetColor);
//            g2.fillRect(0,0, getGame().getScreenWidth(), getGame().getScreenHeight());            
        g.drawImage(image, 0, 0, null);
    }
    
    public abstract void animationFinished();
    
}
