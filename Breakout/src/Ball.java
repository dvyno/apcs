import processing.core.PApplet;

public class Ball {

    private PApplet applet;
    private float x, y;
    private float speedX, speedY;
    private int diam;

    public Ball(PApplet applet_) {
        applet = applet_;
        x = applet.width * 3 / 4;
        y = applet.height / 2;
        speedX = 0.5f;
        speedY = -4.0f;
        diam = 16;
    }

    public void display() {
        applet.fill(255);
        applet.ellipse(x, y, diam, diam);
    }

    public void move(Paddle p) {
        x += speedX;
        y += speedY;
        if (x > applet.width - diam / 2 || x < diam / 2) {
            speedX *= -1;
        }
        if (y < diam / 2 + 50) {
            speedY *= -1;
        }
        if (hitPaddle(p)) {
            // to make game progressively more difficult
            speedY += .05;

            // Divide paddle into 5 sections to get different angle of hit for
            // different sections.
            int hitboxWidth = p.getPaddleLength() + diam;
            float relativeX = x - (p.getX() - diam / 2);
            if (relativeX < hitboxWidth / 5) {
                speedX -=2;
            }
            else if (relativeX < hitboxWidth * 2 / 5) {
                speedX -= 1;
            }
            else if (relativeX < hitboxWidth * 3 / 5) {
                // don't change speedX
            }
            else if (relativeX < hitboxWidth * 4 / 5) {
                speedX += 1;
            }
            else {
                speedX += 2;
            }
            speedY *= -1;
        }
    }

    private boolean hitPaddle(Paddle p) {
        boolean hitX = x < (p.getX() + p.getPaddleLength() + diam / 2) && x > (p.getX() - diam / 2);
        boolean hitY = speedY > 0 && y < (p.getY() + p.getPaddleHeight() + diam / 2) && y > (p.getY() - diam / 2);
        return hitX && hitY;
    }

    public void bounceVertical() {
        speedY = -speedY;
    }

    public void bounceHorizontal() {
        speedX = -speedX;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getDiam() {
        return diam;
    }
}
