package com.odintsofftware.gameapi.graphics;

import com.odintsofftware.gameapi.core.Game;
import java.awt.*;


public class Sprite extends Layer 
{
    private int frameHeight;
    private int frameWidth;
    private int frameCount;
    private int currentFrame = 0;
    
    /**
     * list of X coordinates of individual frames
     */
    int[] frameCoordsX;
    /**
     * list of Y coordinates of individual frames
     */
    int[] frameCoordsY;
    
    private Game game;
    private Image image;
    
    private Rectangle boundingBox;
    private boolean isCollidable;
    private boolean drawBounds = false;
    
    private float alpha = 1.00f;
    private AlphaComposite alphaComp;
    private Composite oldComp;
    
    private double rotation = 0;
    private Point rotationPivot = new Point(0,0);

    public Sprite(Game game, Image image) {
        super(image.getWidth(null), image.getHeight(null));

        initializeFrames(image, image.getWidth(null), image.getHeight(null));
        
        setBoundingBox(image.getWidth(null), image.getHeight(null));
        
        this.game = game;
    }

    public Sprite(Game game, Image image, int frameWidth, int frameHeight)  {
        super(frameWidth, frameHeight);

        initializeFrames(image, width, height);
        
        setBoundingBox(frameWidth, frameHeight);
        
        this.game = game;
    }

    private void initializeFrames(Image image, int fWidth, 
                        int fHeight) {

        int imageW = image.getWidth(null);
        int imageH = image.getHeight(null);
            
        int numHorizontalFrames = imageW / fWidth;
        int numVerticalFrames   = imageH / fHeight;

        this.image = image;

        frameWidth = fWidth;
        frameHeight = fHeight;

        frameCount = numHorizontalFrames*numVerticalFrames;

        frameCoordsX = new int[frameCount];
        frameCoordsY = new int[frameCount];
                
        int curFrame = 0;

        for (int yy = 0; yy < imageH; yy += fHeight) {
            for (int xx = 0; xx < imageW; xx += fWidth) {

                frameCoordsX[curFrame] = xx;
                frameCoordsY[curFrame] = yy;
        
                curFrame++;
            }
        }
    }
    
    public void setFrame(int frame) {
        this.currentFrame = frame;
    }

    public void nextFrame() {
        if (currentFrame != getFrameCount() - 1)
            currentFrame++;
        else
            currentFrame = 0;
    }

    public void previousFrame() {
        if (currentFrame != 0)
            currentFrame--;
        else
            currentFrame = getFrameCount() - 1;
    }

    private void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setAlpha(float alpha) {
        if (alpha > 1.00f || alpha < 0.00f) 
            throw new IllegalArgumentException("Invalid alpha value. Valid alpha range is 0.00f to 1.00f");
        this.alpha = alpha;
    }
    
    public float getAlpha() {
        return alpha;
    }
    
    @Override
    public void draw() {
        if (isVisible()) {
            
            Graphics2D g2 = (Graphics2D) game.getOffscreenBufferGraphics();
            // Verifies alpha value and set composite if needed
            if (alpha < 1.00f) {
//                oldComp = g2.getComposite();
                alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
                g2.setComposite(alphaComp);                
            }                      
           
            // Verifies rotation and apply the transformation if needed
            if (rotation > 0d) {
                g2.rotate(Math.toRadians(rotation), rotationPivot.x + getX(), rotationPivot.y + getY());
            }
            
            g2.drawImage(image, getX(), getY(), 
                        getX() + frameWidth, getY() + frameHeight,  
                        frameCoordsX[currentFrame], frameCoordsY[currentFrame],
                        frameCoordsX[currentFrame] + frameWidth, frameCoordsY[currentFrame] + frameHeight, 
                        null);
            
//            if (oldComp != null) g2.setComposite(oldComp);
            
            if (getDrawBounds()) {
                g2.setColor(Color.red);
                g2.draw(getBoundingBoxPosition());
            }            
        }
    }

    public boolean isCollidable() {
        return isCollidable;
    }
    
    public void setCollidable(boolean isCollidable) {
        this.isCollidable = isCollidable;
    }

    public void setDrawBounds(boolean draw) {
        drawBounds = draw;
    }

    public boolean getDrawBounds() {
        return drawBounds;
    }

    public final void setBoundingBox(int width, int height) {
        boundingBox = new Rectangle(width, height);
    }

    public final void setBoundingBox(Point origin, Dimension size) {
        boundingBox = new Rectangle(origin, size);
    }
    
    public Rectangle getBoundingBoxSize() {
        return boundingBox;
    }
    
    public Rectangle getBoundingBoxPosition() {
        return new Rectangle(getX() + boundingBox.x, getY() + boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public boolean colidesWith(Sprite otherSprite) {        
        if (getBoundingBoxPosition().intersects(otherSprite.getBoundingBoxPosition()))
            return true;
        else
            return false;
    }
    
    public void rotate(double degrees) {
        double tempRotation = rotation;
        
        tempRotation += degrees;
        
        if (tempRotation > 359) 
            rotation = tempRotation - 360;
        else if (tempRotation < 0)
            rotation = 360 + tempRotation;
        else
            rotation = tempRotation;        
    }
    
    public void setRotation(double degrees) {
        rotation = degrees;
    }
    public double getRotation() {
        return rotation;
    }
    
    public void setRotationPivot(int x, int y) {
        rotationPivot = new Point(x, y);
    }    
    
    public void setCenterRotationPivot() {
        rotationPivot = new Point(this.getWidth()/2, this.getHeight()/2);
    }
}
