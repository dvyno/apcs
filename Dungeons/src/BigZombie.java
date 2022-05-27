import processing.core.PApplet;

public class BigZombie extends Enemy {
    
    public BigZombie(PApplet applet, double x, double y, Room room) {
        super(applet, x, y, room, "big_zombie/big_zombie");

        // Initialize frames
        frameAmount = 4;
        currentFrame = 0;
        frameCycle = 20;
        createFrames();

        // Initialize speed
        speed = 1;
        xSpeed = 0;
        ySpeed = 0;

        // Movement timer
        moveTime = applet.millis();
        moveWait = 1500;

        // Hitbox
        leftShift = -18;
        rightShift = 18;
        bottomShift = -21;
        topShift = 33;

        score = 300;
        health = 3;
    }
}
