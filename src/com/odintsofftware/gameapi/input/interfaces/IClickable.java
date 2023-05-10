/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odintsofftware.gameapi.input.interfaces;

import java.awt.Shape;

/**
 *
 * @author Ivan
 */
public interface IClickable {
    
    public boolean mouseOver = false;
    
    public void onClick();
    public void onMouseButtonPressed();
    public void onMouseButtonReleased();
    public void onMouseOver();
    public void onMouseOut();
    public Shape getClickableArea();    
    
}
