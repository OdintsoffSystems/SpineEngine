/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odintsofftware.gameapi.core;

/**
 *
 * @author Ivan Odintsoff
 */
public interface GameCallback {
    
    public void startGame();
    
    public void sceneFinished(Scene last);
    
}
