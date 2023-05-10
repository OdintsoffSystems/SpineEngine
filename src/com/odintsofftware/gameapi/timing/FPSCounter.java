/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odintsofftware.gameapi.timing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author Ivan
 */
public class FPSCounter {
        
	private long lastCount; // last time the fps is counted
	private int currentFPS, // the real fps achieved
	        frameCount;
	
	/** ************************************************************************* */
	/** ***************************** CONSTRUCTOR ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Creates new <code>FPSCounter</code>.
	 */
	public FPSCounter() {
	}
	
	/**
	 * Refresh the FPS counter, reset the fps to 0 and the timer counter to
	 * start counting from current time.
	 */
	public void refresh() {
		this.frameCount = 0;
		this.lastCount = System.currentTimeMillis();
	}
	
	/**
	 * The main method that calculating the frame per second.
	 */
	public void calculateFPS() {
		this.frameCount++;
		if (System.currentTimeMillis() - this.lastCount > 1000) {
			this.lastCount = System.currentTimeMillis();
			this.currentFPS = this.frameCount;
			this.frameCount = 0;
		}
	}
	
	/**
	 * Returns current FPS.
	 * @return The current FPS.
	 * @see #calculateFPS()
	 */
	public int getCurrentFPS() {
		return this.currentFPS;
	}
    
    public void paint(Graphics g) {
      g.setColor(Color.red);
      g.setFont(Font.getFont("Bold"));
      g.drawString(String.valueOf(currentFPS), 50, 50);  
    }
        
}