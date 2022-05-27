import processing.core.PApplet;
import processing.core.PImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Entity {
    protected PApplet applet;

    // image variables
    protected String file;
    protected PImage[] idleFrames, runFrames;
    protected int height, width;
    protected int frameAmount, currentFrame, frameCycle;

    // coordinates and speed
    protected double x, y;
    protected double speed, xSpeed, ySpeed;
    protected boolean moveUp, moveDown, moveLeft, moveRight, faceLeft;
    protected boolean prevUp, prevDown, prevLeft, prevRight;
    protected double prevSpeed;
    

    // HitBox
    protected HitBox hitbox;
    protected int leftShift, rightShift, topShift, bottomShift;

    // room
    protected Room room;

    // health
    protected int health;
    protected boolean isHit;
    protected int hitTime, hitWait;

    public Entity(PApplet applet, double x, double y, Room room, String file) {
        this.applet = applet;
        this.x = x;
        this.y = y;
        this.room = room;
        this.file = file;

        // Get image width and height
        try {
            BufferedImage img = ImageIO.read(new File("src/assets/" + file + "_idle_anim_f0.png"));
            width = img.getWidth();
            height = img.getHeight();
        }
        catch (Exception e) {
            System.out.println("Couldn't read image");
        }

        hitbox = new HitBox(applet, x + leftShift, y + bottomShift, x + rightShift, y + topShift);
        
        hitTime = applet.millis();
        hitWait = 500;
    }

    /**
     * Initializes <code>idleFrames</code> and <code>runFrames</code> arrays
     */
    protected void createFrames() {
        idleFrames = new PImage[frameAmount];
        runFrames = new PImage[frameAmount];

        String path = "assets/" + file;
        for (int i = 0; i < frameAmount; i++) {
            String extension = "_anim_f" + PApplet.nf(i,1) + ".png";
            idleFrames[i] = applet.loadImage(path + "_idle" + extension);
            runFrames[i] = applet.loadImage(path + "_run" + extension);
        }
    }

    protected void showFrame(PImage[] frames) {
        applet.imageMode(PApplet.CENTER);
        int scaleX = 1;

        // Flip the image if Entity is facing left
        if (isFacingLeft()) {
            scaleX *= - 1;
        }

        // Apply necessary transformations
        applet.pushMatrix();
        applet.translate((float)x, (float)y);
        applet.scale(scaleX, 1);
        applet.image(frames[currentFrame / (frameCycle / frameAmount)], 0, 0);
        applet.popMatrix();

        // Update frame
        currentFrame = (currentFrame + 1) % frameCycle;
    }


    /* How the entity moves and is displayed*/
    public void show() {
        if (moveUp || moveDown || moveRight || moveLeft) {
            showFrame(runFrames);
        }
        else {
            showFrame(idleFrames);
        }

        hitbox.show();
    }

    protected void move() {
        boolean moveVertical = moveUp || moveDown;
        boolean moveHorizontal = moveLeft || moveRight;

        // Change Speed
        if (moveVertical && moveHorizontal) {
            xSpeed = speed * Math.cos(Math.PI / 4);
            ySpeed = -speed * Math.sin(Math.PI / 4);
        }
        else if (moveVertical) {
            xSpeed = 0;
            ySpeed = -speed;
        }
        else if (moveHorizontal) {
            xSpeed = speed;
            ySpeed = 0;
        }
        else {
            xSpeed = 0;
            ySpeed = 0;
        }

        // Move character
        if (moveLeft && inLeftBorder()) {
            x -= xSpeed;
        }
        else if (moveRight && inRightBorder()) {
            x += xSpeed;
        }

        if (moveUp && inUpBorder()) {
            y += ySpeed;
        }
        else if (moveDown && inDownBorder()) {
            y -= ySpeed;
        }

        hitbox.setBottomLeft(x + leftShift, y + bottomShift);
        hitbox.setTopRight(x + rightShift, y + topShift);
    }

    /* How Entity deals with collisions */
    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean isHit, Entity e) {
        this.isHit = isHit;

        // Store current movements
        prevDown = moveDown;
        prevUp = moveUp;
        prevLeft = moveLeft;
        prevRight = moveRight;

        // Change movements into direction of entity that hit
        this.moveDown = e.moveDown;
        this.moveUp = e.moveUp;
        this.moveLeft = e.moveLeft;
        this.moveRight = e.moveRight;

        // Store previous speed and change it
        prevSpeed = speed;
        speed = 2;

        // Change movement if this entity is still not moving
        if (!(moveDown || moveUp || moveLeft || moveRight)) {
            if (e.faceLeft) {
                moveLeft = true;
            }
            else {
                moveRight = true;
            }
        }

        hitTime = applet.millis();
    }

    public void hit() {
        move();

        // Reset variables to previous state once hit is over
        if (applet.millis() - hitTime >= hitWait) {
            isHit = false;
            this.moveDown = prevDown;
            this.moveUp = prevUp;
            this.moveLeft = prevLeft;
            this.moveRight = prevRight;
            speed = prevSpeed;
            hitTime = applet.millis();
        }
    }

    /* Determine if Entity is within boundaries */
    protected boolean inLeftBorder() {
        return room.getLeftBorder() <= x - width / 2;
    }

    protected boolean inRightBorder() {
        return room.getRightBorder() >= x + width / 2;
    }

    protected boolean inUpBorder() {
        return room.getUpBorder() <= y;
    }

    protected boolean inDownBorder() {
        return room.getDownBorder() >= y + height / 2;
    }

    /* Getter Methods */
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isMovingUp() {
        return moveUp;
    }

    public boolean isMovingDown() {
        return moveDown;
    }

    public boolean isMovingLeft() {
        return moveLeft;
    }

    public boolean isMovingRight() {
        return moveRight;
    }

    public boolean isFacingLeft() {
        return faceLeft;
    }

    public HitBox getHitBox() {
        return hitbox;
    }

    public int getHealth() {
        return health;
    }

    /* Setter Methods */
    public void setMoveUp(boolean moveUp) {
        this.moveUp = moveUp;
    }

    public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;

        if (moveRight) {
            faceLeft = false;
        }
    }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;

        if (moveLeft) {
            faceLeft = true;
        }
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void increaseHealth() {
        health++;
    }

    public void decreaseHealth() {
        health--;
    }
}
