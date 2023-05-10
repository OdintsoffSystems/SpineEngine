/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.odintsofftware.gameapi.core;
/*
 * OdinCanvas.java
 *
 * Created on 15 de Dezembro de 2006, 23:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import com.odintsofftware.gameapi.input.interfaces.IClickable;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Scene is placed on a GameFrame. Each Scene is like a separated
 * screen for your game.
 * @author ivan
 */
public abstract class       Scene               
{

     /**
     * Constant for the <code>UP</code> game action.
     *
     * <P>Constant value <code>1</code> is set to <code>UP</code>.</P>
     */
    public static final int UP = 1;

    /**
     * Constant for the <code>DOWN</code> game action.
     *
     * <P>Constant value <code>6</code> is set to <code>DOWN</code>.</P>
     */
    public static final int DOWN = 6;

    /**
     * Constant for the <code>LEFT</code> game action.
     *
     * <P>Constant value <code>2</code> is set to <code>LEFT</code>.</P>
     */
    public static final int LEFT = 2;

    /**
     * Constant for the <code>RIGHT</code> game action.
     *
     * <P>Constant value <code>5</code> is set to <code>RIGHT</code>.</P>
     */
    public static final int RIGHT = 5;

    /**
     * Constant for the <code>FIRE</code> game action.
     *
     * <P>Constant value <code>8</code> is set to <code>FIRE</code>.</P>
     */
    public static final int FIRE = 8;

    /**
     * Constant for the general purpose &quot;<code>A</code>&quot; game action.
     *
     * <P>Constant value <code>9</code> is set to <code>GAME_A</code>.</P>
     */
    public static final int GAME_A = 9;

    /**
     * Constant for the general purpose &quot;<code>B</code>&quot; game action.
     *
     * <P>Constant value <code>10</code> is set to <code>GAME_B</code>.</P>
     */
    public static final int GAME_B = 10;

    /**
     * Constant for the general purpose &quot;<code>C</code>&quot; game action.
     *
     * <P>Constant value <code>11</code> is set to <code>GAME_C</code>.</P>
     */
    public static final int GAME_C = 11;

    /**
     * Constant for the general purpose &quot;<code>D</code>&quot; game action.
     *
     * <P>Constant value <code>12</code> is set to <code>GAME_D</code>.</P>
     */
    public static final int GAME_D = 12;

    /**
     * The bit representing the UP key.  This constant has a value of
     * <code>0x0002</code> (1 << Canvas.UP).
     */
    public static final int UP_PRESSED = 1 << UP;

    /**
     * The bit representing the DOWN key.  This constant has a value of
     * <code>0x0040</code> (1 << Canvas.DOWN).
     */
    public static final int DOWN_PRESSED = 1 << DOWN;

    /**
     * The bit representing the LEFT key.  This constant has a value of
     * <code>0x0004</code> (1 << Canvas.LEFT).
     */
    public static final int LEFT_PRESSED = 1 << LEFT;

    /**
     * The bit representing the RIGHT key.  This constant has a value of
     * <code>0x0020</code> (1 << Canvas.RIGHT).
     */
    public static final int RIGHT_PRESSED = 1 << RIGHT;

    /**
     * The bit representing the FIRE key.  This constant has a value of
     * <code>0x0100</code> (1 << Canvas.FIRE).
     */
    public static final int FIRE_PRESSED = 1 << FIRE;

    /**
     * The bit representing the GAME_A key (may not be supported on all
     * devices).  This constant has a value of
     * <code>0x0200</code> (1 << Canvas.GAME_A).
     */
    public static final int GAME_A_PRESSED = 1 << GAME_A;

    /**
     * The bit representing the GAME_B key (may not be supported on all
     * devices).  This constant has a value of
     * <code>0x0400</code> (1 << Canvas.GAME_B).
     */
    public static final int GAME_B_PRESSED = 1 << GAME_B;

    /**
     * The bit representing the GAME_C key (may not be supported on all
     * devices).  This constant has a value of
     * <code>0x0800</code> (1 << Canvas.GAME_C).
     */
    public static final int GAME_C_PRESSED = 1 << GAME_C;

    /**
     * The bit representing the GAME_D key (may not be supported on all
     * devices).  This constant has a value of
     * <code>0x1000</code> (1 << Canvas.GAME_D).
     */
    public static final int GAME_D_PRESSED = 1 << GAME_D;
    
    private Game game = null;
    private GameCallback gameCallback;
    private ArrayList<IClickable> clickableObjects = new ArrayList<IClickable>();

    
    protected abstract void initialize();
    
    /**
     * Method called at each cycle of the timer. Time related game logic goes here.
     */
    public abstract void tick(int delta);

    /**
     * Method that renders the Scene on the GameFrame/Applet.
     */
    
    public abstract void render();
              
    public Graphics2D getGraphics() {
        return (Graphics2D) game.getOffscreenBufferGraphics();
    }
    
    public void finishScene() {
        game.getGameCallback().sceneFinished(this);
    }
    
    public void setGame(Game game) {
        this.game = game;
        initialize();
    }
    
    public Game getGame() {
        return game;
    }
    
    // Clickable objects handle ================================================
    
    public void addClickable(IClickable object) {
        clickableObjects.add(object);        
    }
    
    public void removeClickable(IClickable object) {
        clickableObjects.remove(object);
    }
    
    // Events Broadcast    
    public void onClick(Point clickPos){
        for (IClickable clickable : clickableObjects) {
            if (clickable.getClickableArea().contains(clickPos))
                clickable.onClick();
        }
    }
    
    public void mousePressed(Point pressedPos){
        for (IClickable clickable : clickableObjects) {
            if (clickable.getClickableArea().contains(pressedPos))
                clickable.onMouseButtonPressed();
        }
    }

    public void mouseReleased(Point releasePos){
     for (IClickable clickable : clickableObjects) {
            if (clickable.getClickableArea().contains(releasePos))
                clickable.onMouseButtonPressed();
        }
    }
    
    public void mouseMoved(Point movePos) {
         for (IClickable clickable : clickableObjects) {
            if (clickable.getClickableArea().contains(movePos))
                clickable.onMouseOver();
        }        
    }
    
}
