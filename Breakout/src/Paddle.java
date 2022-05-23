import processing.core.PApplet;

public class Paddle {

    private PApplet applet;
    private int paddleLength, paddleHeight;
    private float x, y;
    private float speed;
    private boolean movingLeft, movingRight;

    public Paddle(PApplet applet_) {
        applet = applet_;
        paddleLength = 80;
        paddleHeight = 15;
        x = applet.width / 2 - paddleLength / 2;
        y = applet.height - 40;
        speed = 4;
        movingLeft = movingRight = false;
    }

    public void display() {
        applet.fill(255);
        applet.rect(x, y, paddleLength, paddleHeight);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getPaddleLength() {
        return paddleLength;
    }

    public int getPaddleHeight() {
        return paddleHeight;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingLeft(boolean newMovingLeft) {
        movingLeft = newMovingLeft;
    }

    public void setMovingRight(boolean newMovingRight) {
        movingRight = newMovingRight;
    }

    public void move() {
        if(movingLeft && x > 0) {
            x -= speed;
        }
        else if (movingRight && x < applet.width - paddleLength) {
                x += speed;
        }
    }
}
