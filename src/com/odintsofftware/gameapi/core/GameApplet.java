/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.odintsofftware.gameapi.core;

import com.odintsofftware.gameapi.graphics.GraphicsUtils;
import com.odintsofftware.gameapi.graphics.GraphicsUtils.StretchMethod;
import com.odintsofftware.gameapi.timing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JApplet;

/**
 *
 * @author Ivan Odintsoff
 */
public  class       GameApplet 
        extends     JApplet 
        implements  Game,
                    KeyListener,
                    MouseListener,
                    MouseMotionListener
                     { 
      
    private int keyState;

    private GraphicsDevice device = GraphicsUtils.getGraphicsDevice();
    private Scene currentScene;
    private BufferStrategy bf;
    private Graphics g;
 
    private Canvas canvas = new Canvas();
    
    private double miliseconds_per_tick;  
    //private long nanoseconds_per_tick;
    
    private int fps = 60;//default
    private final long NANOTIME_PER_TICK = 1000000000 / fps;
    private long lastLoopTime = System.nanoTime();
    double delta;
    
    // Screen Related
    private Dimension gameResolution;
    
    private StretchMethod method;    
    
    private  BufferedImage offscreenBuffer;
    
    private GameCallback callback;
   
//   private IClickable clickableObjects[] = new IClickable[1];
//   private int nClickableObjects; // number of clickable objects
   
    /* ==========================================================================
     * ========================= APPLET =========================================
     */
   
    @Override
    public void init() {
        
    }
    
   
    /** Creates a new instance of Main */
    public GameApplet() {

    }
    
    public void initialize(Dimension screenResolution, StretchMethod method) {        
        setupScreen(screenResolution, method);

        //default FPS (60)
        this.miliseconds_per_tick = 1000/fps; 
                
        initializeScreen();
        
        callback.startGame(); 
        startLoop(); // Starts Thread                 
    }

    /* ==========================================================================
     * ========================= GAME LOOP ======================================
     */

    private void startLoop() {
        
        while (true)
        {

            long loopStartTime = System.nanoTime();                      

            if (currentScene != null) {
                currentScene.tick((int)delta);
                render();
            }
            
            
            long updateLength = loopStartTime - lastLoopTime;
            delta = updateLength / ((double)NANOTIME_PER_TICK);            
            lastLoopTime = loopStartTime;  

            // we want each frame to take 10 milliseconds, to do this
            // we've recorded when we started the frame. We add 10 milliseconds
            // to this and then factor in the current time to give 
            // us our final value to wait for
            // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
            try {
                Thread.sleep((lastLoopTime-System.nanoTime() + NANOTIME_PER_TICK)/1000000);
            } 
            catch (Exception ex) {
                
            }
        }
    }    
    
    /* ==========================================================================
     * ========================= SCREEN SETUP ===================================
     */
    
    private void setupScreen(Dimension gameResolution, StretchMethod method) {
        this.gameResolution = gameResolution;
        this.method = method;
        offscreenBuffer = new BufferedImage(getRenderingResolution().width, getRenderingResolution().height, BufferedImage.TYPE_INT_RGB);
        offscreenBuffer.setAccelerationPriority(1);
    }
    
    private void initializeScreen() {
     
        
        canvas.setSize(getRenderingResolution());
        getContentPane().add(canvas);
        canvas.setFocusable(true);
        canvas.setLocation(0, 0);
        canvas.addKeyListener(this);
        
        
        
        canvas.createBufferStrategy(2);
        bf = canvas.getBufferStrategy();
    }    
    
    public Dimension getRenderingResolution() {
        return new Dimension(gameResolution.width * method.getValue(), 
                             gameResolution.height * method.getValue());
    }
    
    public Dimension getGameResolution() {
        return gameResolution;
    }
    
    public int getColorDepth() {
        return 32;
    }
    
    public StretchMethod getStretchMethod() {
        return method;
    }
    
    public BufferedImage getOffscreenBuffer() {
        return offscreenBuffer;
    }
    
    public Graphics getOffscreenBufferGraphics() {
        return offscreenBuffer.getGraphics();
    }
    
    public int getMiddleX() {
        return gameResolution.width /2;
    }
    
    public int getMiddleY() {
        return gameResolution.height /2;
    }
    
    public int getScreenWidth() {
        return getRenderingResolution().width;
    }

    public int getScreenHeight() {
        return getRenderingResolution().height;
    }
    
    @Override
    public Container getGameContentPane() {
        return getContentPane();
    } 
    
    /* ==========================================================================
     * ========================= SCENE SETUP =====================================
     */
    
    public void setScene(Scene newScene) {
    	this.currentScene = newScene;
        newScene.setGame(this);
    }

    public Scene getScene() {
        return this.currentScene;
    }
    
    public void setGameCallback(GameCallback callback) {
        this.callback = callback;
    }
    
    public GameCallback getGameCallback() {
        return callback;
    }

    /* ==========================================================================
     * ========================= RENDERING ======================================
     */
    
    public StretchMethod getCurrentStrechMethod() {
        return getStretchMethod();
    }  

    public void render() {
         try {
            g = bf.getDrawGraphics();
            Graphics2D g2 = (Graphics2D) g;
            if (currentScene != null) {                
                if (getCurrentStrechMethod() != StretchMethod.NO_STRETCH) {

                    StretchMethod currentMethod = getCurrentStrechMethod();
                    AffineTransform at = AffineTransform.getScaleInstance(currentMethod.getValue(), currentMethod.getValue());
        //          AffineTransformOp aop =
        //                new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    g2.drawImage(getOffscreenBuffer(), at, null);            
                }
                else {
                    g2.drawImage(getOffscreenBuffer(), 0, 0, null);
                }
            }
        } finally {
            // It is best to dispose() a Graphics object when done with it.
            g.dispose();
        }
        // Shows the contents of the backbuffer on the screen.
 	bf.show();
    }

    public void sceneFinished() {
        callback.sceneFinished(currentScene);
    }
    
    /* =========================================================================
     * ========================= KEYBOARD INPUT ================================
     */

    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_LEFT) //esquerda
        {
            keyState = keyState ^ Scene.LEFT_PRESSED;
        }
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT) //direita
        {
            keyState = keyState ^ Scene.RIGHT_PRESSED;
        }
        if (ke.getKeyCode() == KeyEvent.VK_UP) //cima
        {
            keyState = keyState ^ Scene.UP_PRESSED;
        }
        if (ke.getKeyCode() == KeyEvent.VK_DOWN) //baixo
        {
            keyState = keyState ^ Scene.DOWN_PRESSED;
        }
        
        if (ke.getKeyCode() == KeyEvent.VK_A) // A - Game Button A
        {
            keyState = keyState ^ Scene.GAME_A_PRESSED;
        }
        if (ke.getKeyCode() == KeyEvent.VK_S) // S - Game Button B
        {
            keyState = keyState ^ Scene.GAME_B_PRESSED;
        }
        if (ke.getKeyCode() == KeyEvent.VK_Z) // Z - Game Button C
        {
            keyState = keyState ^ Scene.GAME_C_PRESSED;
        }
        if (ke.getKeyCode() == KeyEvent.VK_X) // X - Game Button D
        {
            keyState = keyState ^ Scene.GAME_D_PRESSED;
        }
        
        
        if (ke.getKeyCode() == KeyEvent.VK_SPACE) //espa�o
        {
            keyState = keyState ^ Scene.FIRE_PRESSED;
        }
    }

    public void keyTyped(KeyEvent ke) {}

    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) { // ESC
            System.exit(0);
        }
        if (ke.getKeyCode() == KeyEvent.VK_LEFT) //esquerda
        {
            keyState = keyState | Scene.LEFT_PRESSED;
        }
        
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT) //direita
        {
            keyState = keyState | Scene.RIGHT_PRESSED;
        }
        if (ke.getKeyCode() == KeyEvent.VK_UP) //cima
        {
            keyState = keyState | Scene.UP_PRESSED;
        }
        if (ke.getKeyCode() == KeyEvent.VK_DOWN) //baixo
        {
            keyState = keyState | Scene.DOWN_PRESSED;
        }
        
        if (ke.getKeyCode() == KeyEvent.VK_A) // A - Game Button A
        {
            keyState = keyState | Scene.GAME_A_PRESSED;
        }
        if (ke.getKeyCode() == KeyEvent.VK_S) // S - Game Button B
        {
            keyState = keyState | Scene.GAME_B_PRESSED;
        }
        if (ke.getKeyCode() == KeyEvent.VK_Z) // Z - Game Button C
        {
            keyState = keyState | Scene.GAME_C_PRESSED;
        }
        if (ke.getKeyCode() == KeyEvent.VK_X) // X - Game Button D
        {
            keyState = keyState | Scene.GAME_D_PRESSED;
        }
        
        if (ke.getKeyCode() == KeyEvent.VK_SPACE) //espa�o
        {
            keyState = keyState | Scene.FIRE_PRESSED;
        }

    }

    public int getKeyState() {
        return keyState;
    }    
    
    /* =========================================================================
     * ========================== MOUSE INPUT ==================================     
     */    
    
    public void mouseClicked(MouseEvent e) {
        getScene().onClick(e.getPoint());
    }

    public void mousePressed(MouseEvent e) {
        getScene().mousePressed(e.getPoint());
    }

    public void mouseReleased(MouseEvent e) {
        getScene().mouseReleased(e.getPoint());
      
    }
   
    public void mouseMoved(MouseEvent e) {
        getScene().mouseMoved(e.getPoint());
    }
    
    public void mouseExited(MouseEvent e) {
        // Do nothing stub
    }
    
    public void mouseEntered(MouseEvent e) {
        // Do nothing stub
    }
    
    public void mouseDragged(MouseEvent e) {
        // Do nothing stub
    }

    public void setCallback(GameCallback callback) {
        this.callback = callback;
    }
}
