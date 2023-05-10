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

import com.odintsofftware.gameapi.graphics.GraphicsUtils;
import com.odintsofftware.gameapi.graphics.Sprite;

/**
 *
 * @author root
 */
public class ResourceFactory {
    
    /** The single instance of this class to ever exist <singleton> */
	private static final ResourceFactory single = new ResourceFactory();
	
	/**
 	 * Retrieve the single instance of this class 
	 *
 	 * @return The single instance of this class 
	 */
	public static ResourceFactory getInstance() {
		return single;
	}

	/** A value to indicate that we should use Java 2D to render our game */
	public static final int JAVA2D = 1;
	/** A value to indicate that we should use OpenGL (JOGL) to render our game */
	public static final int OPENGL_JOGL = 2;

	/** The type of rendering that we are currently using */
	private int renderingType = JAVA2D;
	/** The window the game should use to render */
	private Game game;

	/** 
        * The default contructor has been made private to prevent construction of 
	 * this class anywhere externally. This is used to enforce the singleton 
	 * pattern that this class attempts to follow
	 */
	private ResourceFactory() {            
	}
        
        public GameFrame getGameFrame(GameCallback callback) {
            if (game == null) {
                game = new GameFrame(callback);                
            }
            
            return (GameFrame)game;
        }
        
        public void setGameApplet(GameApplet applet) {
            if (game == null) {
                game = applet;
            }            
        }
        
	/**
	 * Create or get a sprite which displays the image that is pointed
	 * to in the classpath by "ref"
	 * 
	 * @param ref A reference to the image to load
	 * @return A sprite that can be drawn onto the current graphics context.
	 */
	public Sprite getSprite(String ref) {
		return new Sprite(game, GraphicsUtils.getInstance().createImage(ref));
	}
    
        /**
         * Set the GameFrame for the ResourceFactory to work.
         */
        public void setGameFrame(GameFrame game) {
            this.game = game;
        }
        
}
