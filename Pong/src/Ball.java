import processing.core.PApplet;

public class Ball {
    private PApplet applet;
    private float xPos, yPos;
    private float xSpeed, ySpeed;
    private int angle;
    private double speed;
    private int radius = 5;

    public Ball(PApplet initApplet) {
        applet = initApplet;
        speed = 6.25;
        resetPosition();
    }

    public void display() {
        applet.ellipse(xPos, yPos, 2 * radius, 2 * radius);
    }

    public void move() {
        xPos += xSpeed;
        yPos += ySpeed;
    }

    public boolean paddleCollision(Paddle p) {
        boolean xHit = (xPos > p.getX() - radius && xPos < p.getX() + p.getPaddleWidth() + radius);
        boolean yHit = (yPos > p.getY() - radius && yPos < p.getY() + p.getPaddleLength() + radius);
        return xHit && yHit;
    }

    public boolean wallXLeftCollision() {
        return (xPos - radius < 0);
    }

    public boolean wallXRightCollision() {
        return xPos + radius > applet.width;
    }

    public boolean wallYCollision() {
        return (yPos - radius < 0) || (yPos + radius > applet.height);
    }

    public void changeXSpeed() {
        xSpeed = -xSpeed;
    }

    public void changeYSpeed() {
        ySpeed = -ySpeed;
    }

    public float getX() {
        return xPos;
    }

    public float getY() {
        return yPos;
    }

    public float getXSpeed() {
        return xSpeed;
    }

    public float getYSpeed() {
        return ySpeed;
    }

    public int getRadius() {
        return radius;
    }

    public void resetPosition() {
        xPos = (float)applet.width / 2;
        yPos = (float)applet.height / 2;

        angle = (int)(Math.random() * 20) + 35;
        xSpeed = (float)(speed * Math.cos(angle * Math.PI / 180));
        if (Math.random() > .5) {
            xSpeed = -xSpeed;
        }

        ySpeed = (float)(speed * Math.sin(angle * Math.PI / 180));
        if (Math.random() > .5) {
            ySpeed = -ySpeed;
        }
    }
}
