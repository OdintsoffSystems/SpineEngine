/*
*                                                           
*   +++++++++++      +++?       +++++++++++++++++++++++     
*   ++++++++      ++++++++?      ,+++++++++++++++++++++     
*   ++++++      ,+++++++++++       ++++++++++++++++++++     
*   ++++       +++++++++++++        +++++++++++++++++++     
*   +++       =+++++++++++++        +++++++++++++++++++     
*   ++       ,++++++++++++++         ++++++++++++++++++     
*   ~        +++++++++++++++         ++++++++++++++++++     
*           ++++++++++++++++         ++++++++++++++++++     
*           +++++++++++++++=         ++++++++++++++++++     
*          ++++++++++++++++         ?++++++++++++++++++     
*          ++++++++++++++++         +++++++,    :++++++     
*         ++++++++++++++++=        :+++                     
*         ++++++++++++++++         ++       ?+++++?         
*        ,+++++++++++++++,        ++       +++++++++~       
*        ?+++++++++++++++        ++,      ++++++++++++      
*        +++++++++++++++        +++       ++++++++++++      
*        ++++++++++++++:       ?++?        ++++++++++++     
*        +++++++++++++=       ?++++        ++++++++++++     
*        ++++++++++++~       ++++++:        ,++++++++++     
*        +++++++++++       +++++++++          +++++++++     
*         +++++++++      +?++++++++++          ,+++++++     
*   +       ++++       +++++++++++++++,          ++++++     
*   +++~           ~++++++++++++++++++++           ++++     
*   +++++++++++++++++++++++++++++++++++++:          +++     
*   +++++++++++++++++++++++++++++++++++++++          ++     
*   ++++++++++++++++++++++++++ +++++++++++++         ++     
*   +++++++++++++++++++++++++  ++++++++++++++        ~+     
*   +++++++++++++++++++++++++  :++++++++++++++       :+     
*   +++++++++++++++++++++++++   ++++++++++++++       ++     
*   ++++++++++++++++++++++++     +++++++++++++       ++     
*   ++++++++++++++++++++++++      +++++++++++       +++     
*   +++++++++++++++++++++++:        ++++++++       ++++     
*   ++++++++++++++++++++++;                     +++++++     
*
*   ODINTSOFFTWARE
*   Spine Engine Alpha - Java SE Version (Java2D/Jorbis)
* 
*/

package com.odintsofftware.gameapi.core;

import com.odintsofftware.gameapi.graphics.GraphicsUtils.StretchMethod;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author Ivan
 */
public interface Game {
 
    public Dimension getRenderingResolution();
    
    public Dimension getGameResolution();
    
    public int getColorDepth();
    
    public StretchMethod getStretchMethod();
    
    public Graphics getOffscreenBufferGraphics();
    
    public int getMiddleX();
    
    public int getMiddleY();
    
    public int getScreenWidth();

    public int getScreenHeight();
    
    public Container getGameContentPane();
    
    /* ==========================================================================
     * ========================= SCENE SETUP =====================================
     */
    
    public void setScene(Scene newScene);

    public Scene getScene();
    
    public GameCallback getGameCallback();
    
    /* ==========================================================================
     * ========================= RENDERING ======================================
     */
    
    public StretchMethod getCurrentStrechMethod();

    public void render();
    
    /* =========================================================================
     * ========================= KEYBOARD INPUT ================================
     */

    public int getKeyState();
    
}
