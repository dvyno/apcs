import processing.core.PApplet;

public class Paddle {
    private PApplet applet;
    private int xPos, yPos;
    private int paddleLength, paddleWidth;

    public Paddle(PApplet initApplet) {
        applet = initApplet;
        xPos = 75;
        yPos = applet.height / 2;
        paddleLength = 100;
        paddleWidth = 10;
    }

    public Paddle(PApplet initApplet, int initXPos) {
        applet = initApplet;
        xPos = initXPos;
        yPos = applet.height / 2;
        paddleLength = 100;
        paddleWidth = 10;

    }

    public void display() {
        applet.rect(xPos, yPos, paddleWidth, paddleLength);
    }

    // Paddle moves with player input
    public void move() {
        if(applet.keyPressed) {
            if (applet.keyCode == applet.UP && yPos > 0) {
                yPos-= 5;
            }
            else if (applet.keyCode == applet.DOWN && yPos + paddleLength < applet.height) {
                yPos += 5;
            }
        }
    }

    // Paddle automatically moves depending on the balls position
    /*************************************************
     * Title: Pong AI
     * Author: Matt
     * Date: 2011
     * Code version: 1.1
     * Availability: https://stackoverflow.com/a/4579065
     *************************************************/
    public void autoMove(Ball b) {
        double impactDistance;
        double impactTime;
        double targetY;
        int speed = 3;

        if (b.getXSpeed() < 0) {
            return;
        }

        // Figure out linear trajectory
        impactDistance = xPos - b.getRadius() - b.getX();
        impactTime = impactDistance / (b.getXSpeed() * .25 * 1000);
        targetY = b.getY() + (b.getYSpeed() * .25 * 1000) * impactTime;

        if (Math.abs(targetY - (yPos + (double)paddleLength / 2)) < 10) {
            // AI doesn't need to move
            return;
        }
        if (targetY < yPos + ((double)paddleLength / 2)) {
            // Move up if ball is going above the paddle
            speed = -speed;
        }
            yPos += speed;

        if (yPos < 0) {
            yPos = 0;
        }
        else if (yPos + paddleLength > applet.height) {
            yPos = applet.height - paddleLength;
        }
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public int getPaddleLength() {
        return paddleLength;
    }

    public int getPaddleWidth() {
        return paddleWidth;
    }
}
