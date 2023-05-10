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
import com.odintsofftware.gameapi.graphics.GraphicsUtils.StretchMethod;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import javax.swing.JFrame;

/**
 *
 * @author Ivan Odintsoff
 */
public  class       GameFrame 
        extends     JFrame 
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
    
    // Screen Related
    private Dimension gameResolution;        
    private int colorDepth;
    
    private StretchMethod currentMethod;    
    
    private BufferedImage offscreenBuffer;
    
    private GameCallback callback;
    
    private long lastUpdateTime;
    private long desiredFPS = 60;
    private long desiredDeltaLoop = (1000/*1000*1000*/)/desiredFPS;
    private long beginLoopTime;
    private long currentUpdateTime;
    private long endLoopTime;
    private long deltaLoop;

   
   
    /** Creates a new instance of Main */
    public GameFrame(GameCallback callback) {
    	super(GraphicsUtils.getGraphicsDevice().getDefaultConfiguration());  
        this.callback = callback;
    }
    
    public void initialize(String windowTitle, boolean fullScreen, Dimension screenResolution, int colorDepth, StretchMethod method) {        
        setupScreen(screenResolution, colorDepth, method);
        
        setTitle(windowTitle);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        initializeScreen(fullScreen);
        
        addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        
        callback.startGame(); 
        startLoop(); // Starts GameLoop                 
    }
    
    public void initialize(String windowTitle, boolean fullScreen, Dimension screenResolution, int colorDepth, int fps, StretchMethod method) {        
        setupScreen(screenResolution, colorDepth, method);
        
        setFPS(fps);
        setTitle(windowTitle);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        initializeScreen(fullScreen);
        
        addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        
        callback.startGame(); 
        startLoop(); // Starts GameLoop                 
    }

    /* ==========================================================================
     * ========================= GAME LOOP ======================================
     */

    private void startLoop() {
        
        currentUpdateTime = System.currentTimeMillis();
        deltaLoop = 0;
        
        while(true){
            beginLoopTime = System.currentTimeMillis();

            render();
            currentScene.tick((int)deltaLoop);
            
            lastUpdateTime = currentUpdateTime;
            currentUpdateTime = System.currentTimeMillis();
            
            endLoopTime = System.currentTimeMillis();
            deltaLoop = endLoopTime - beginLoopTime;
            

            if (deltaLoop > desiredDeltaLoop){
                //Do nothing. We are already late.
            } else {
                try {
                    long sleepTime = (desiredDeltaLoop - deltaLoop);//(1000*1000);
                    Thread.sleep(sleepTime);
                } catch(InterruptedException e) {
                    //Do nothing
                }
            }
        }
    }

    
    /* ==========================================================================
     * ========================= SCREEN SETUP ===================================
     */            
    
    private void setFPS(int fps) {
        desiredFPS = fps;
        desiredDeltaLoop = (1000*1000*1000)/desiredFPS;
    }
    
    private void setupScreen(Dimension gameResolution, int colorDepth, StretchMethod method) {
        this.gameResolution = gameResolution;
        this.colorDepth = colorDepth;
        this.currentMethod = method;
        this.offscreenBuffer = new BufferedImage(getRenderingResolution().width, getRenderingResolution().height, BufferedImage.TYPE_INT_RGB);
    }
    
    private void initializeScreen(boolean wantFullScreen) {
        boolean isFullScreen = device.isFullScreenSupported() && wantFullScreen;
        DisplayMode dm = new DisplayMode(getRenderingResolution().width, 
                                         getRenderingResolution().height, 
                                         getColorDepth(), 0);
               
        setUndecorated(isFullScreen);
        setResizable(false);
        
        canvas.setSize(getRenderingResolution());
        add(canvas);
        canvas.setFocusable(false);        
        
        if (isFullScreen) {
            // Full-screen mode
            device.setFullScreenWindow(this);
            device.setDisplayMode(dm);
            validate();
        } else {
            // Windowed mode
            pack();
            setVisible(true);
        }
        
                
        canvas.createBufferStrategy(2);
        bf = canvas.getBufferStrategy();
//        if (bf != null) {
//            BufferCapabilities caps = bf.getCapabilities();
//            try {
//                Class ebcClass = Class.forName(
//                    "sun.java2d.pipe.hw.ExtendedBufferCapabilities");
//                Class vstClass = Class.forName(
//                    "sun.java2d.pipe.hw.ExtendedBufferCapabilities$VSyncType");
//
//                Constructor ebcConstructor = ebcClass.getConstructor(
//                    new Class[] { BufferCapabilities.class, vstClass });
//                Object vSyncType = vstClass.getField("VSYNC_ON").get(null);
//
//                BufferCapabilities newCaps = (BufferCapabilities)ebcConstructor.newInstance(
//                    new Object[] { caps, vSyncType });
//
//                try {
//                    canvas.createBufferStrategy(2, newCaps);
//                }
//                catch (AWTException awte) {
//                    
//                }
//
//                // TODO: if success, setCanChangeRefreshRate(false) and setRefreshRate(60). 
//                // Possibly override refreshRateSync()?
//                
//            }
//            catch (Throwable t) {
//                // Ignore
//                t.printStackTrace();
//            }
//        }
        
    }    
    
    @Override
    public Dimension getRenderingResolution() {
        return new Dimension(gameResolution.width * currentMethod.getValue(), 
                             gameResolution.height * currentMethod.getValue());
    }
    
    @Override
    public Dimension getGameResolution() {
        return gameResolution;
    }
    
    @Override
    public int getColorDepth() {
        return colorDepth;
    }
    
    @Override
    public StretchMethod getStretchMethod() {
        return currentMethod;
    }
    
    @Override
    public Graphics getOffscreenBufferGraphics() {
        return offscreenBuffer.getGraphics();
//        Graphics bfg = bf.getDrawGraphics();
//        if (getCurrentStrechMethod() != StretchMethod.NO_STRETCH) {
//            Graphics2D g2 = (Graphics2D)bfg;
//            g2.scale(getCurrentStrechMethod().getValue(), getCurrentStrechMethod().getValue());
//            return g2;
//        }        
//        return bf.getDrawGraphics();
    }
    
    @Override
    public int getMiddleX() {
        return gameResolution.width /2;
    }
    
    @Override
    public int getMiddleY() {
        return gameResolution.height /2;
    }
    
    @Override
    public int getScreenWidth() {
        return getRenderingResolution().width;
    }

    @Override
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
    
    @Override
    public void setScene(Scene newScene) {
    	this.currentScene = newScene;
        newScene.setGame(this);
    }

    @Override
    public Scene getScene() {
        return this.currentScene;
    }
    
    @Override
    public GameCallback getGameCallback() {
        return callback;
    }

    /* ==========================================================================
     * ========================= RENDERING ======================================
     */
    
    @Override
    public StretchMethod getCurrentStrechMethod() {
        return getStretchMethod();
    }  

    @Override
    public void render() {
         try {
            g = bf.getDrawGraphics();
            Graphics2D g2 = (Graphics2D) g;
            if (currentScene != null) {                
                currentScene.render();
                if (getCurrentStrechMethod() != StretchMethod.NO_STRETCH) {
                    
                    AffineTransform at = AffineTransform.getScaleInstance(getCurrentStrechMethod().getValue(), getCurrentStrechMethod().getValue());
                    g2.scale(getCurrentStrechMethod().getValue(), getCurrentStrechMethod().getValue());
                    g2.drawImage(offscreenBuffer, at, null);    
                }               
                g2.drawImage(offscreenBuffer, 0, 0, null);       
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

    @Override
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

    @Override
    public void keyTyped(KeyEvent ke) {}

    @Override
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

    @Override
    public int getKeyState() {
        return keyState;
    }    
    
    /* =========================================================================
     * ========================== MOUSE INPUT ==================================     
     */    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (currentScene != null) getScene().onClick(e.getPoint());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (currentScene != null) getScene().mousePressed(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (currentScene != null) getScene().mouseReleased(e.getPoint());
      
    }
   
    @Override
    public void mouseMoved(MouseEvent e) {
        if (currentScene != null) getScene().mouseMoved(e.getPoint());
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        // Do nothing stub
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        // Do nothing stub
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        // Do nothing stub
    }

    public void setCallback(GameCallback callback) {
        this.callback = callback;
    }
}
