import processing.core.PApplet;

public class Pong extends PApplet {
    private Ball b;
    private Paddle playerPaddle, autoPaddle;
    private int p1Score, p2Score;

    public static void main(String[] args) {
        PApplet.main("Pong");
    }

    public void settings() {
        size(858, 525);
    }

    public void setup() {
        b = new Ball(this);
        playerPaddle = new Paddle(this);
        autoPaddle = new Paddle(this, width - 85);
        p1Score = p2Score = 0;
    }

    public void draw() {
        displayBackground();
        b.display();
        playerPaddle.display();
        b.move();
        playerPaddle.move();
        autoPaddle.display();
        autoPaddle.autoMove(b);

        if (b.wallXLeftCollision()) {
            p2Score++;
            b.resetPosition();
        }
        else if (b.wallXRightCollision()) {
            p1Score++;
            b.resetPosition();
        }

        if (b.wallYCollision()) {
            b.changeYSpeed();
        }

        if (b.paddleCollision(playerPaddle)) {
            int playerY1 = playerPaddle.getY();
            int playerY2 = playerPaddle.getY() + playerPaddle.getPaddleLength();

            if ((b.getY() < playerY1 && b.getYSpeed() > 0)|| (b.getY() > playerY2 && b.getYSpeed() < 0)) {
                b.changeYSpeed();
            }
            else if (b.getXSpeed() < 0) {
                b.changeXSpeed();
            }
        }
        else if (b.paddleCollision(autoPaddle)) {
            int autoY1 = autoPaddle.getY();
            int autoY2 = autoPaddle.getY() + autoPaddle.getPaddleLength();

            if ((b.getY() < autoY1 && b.getYSpeed() > 0) || (b.getY() > autoY2 && b.getYSpeed() < 0)) {
                b.changeYSpeed();
            }
            else if (b.getXSpeed() > 0) {
                b.changeXSpeed();
            }
        }
    }

    private void displayBackground() {
        background(0);
        stroke(255);
        strokeWeight(1);
        line(width / 2, 0, width / 2, height);
        strokeWeight(1);
        textSize(40);
        text(p1Score, width / 4, 50);
        text(p2Score, 3 * width / 4, 50);
    }
}
