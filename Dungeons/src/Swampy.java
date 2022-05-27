import processing.core.PApplet;

public class Swampy extends Enemy {
    public Swampy(PApplet applet, double x, double y, Room room) {
        super(applet, x, y, room, "swampy/swampy");

        // Initialize frames
        frameAmount = 4;
        currentFrame = 0;
        frameCycle = 20;
        createFrames();

        // Initialize speed
        speed = 0.5;
        xSpeed = 0;
        ySpeed = 0;

        // Movement timer
        moveTime = applet.millis();
        moveWait = 1000;

        // Hitbox
        leftShift = -16;
        rightShift = 16;
        bottomShift = -16;
        topShift = 16;

        score = 200;
        health = 2;
    }
}
