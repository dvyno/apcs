import processing.core.PApplet;

public class Saucer extends Floater {
    private boolean alive;
    private boolean movingUp;
    private boolean movingDown;
    private boolean movingLeft;
    private int type;
    private int moveTime, moveWait;
    private static double[][] saucerTypes = {
        // Type 0 - Small Saucer
        {-4, 4, 10, -10, -4, 4, 10, 4, 2, -2, -4, -10},
        {-2, -2, 2, 2, 6, 6, 2, -2, -6, -6, -2, 2},
        // Type 1 - Large Saucer
        {-8, 8, 20, -20, -8, 8, 20, 8, 4, -4, -8, -20},
        {-4, -4, 4, 4, 12, 12, 4, -4, -12, -12, -4, 4}
    };

    private static double[] hitRadii = {10.4, 20.8};
    private double hitRadius;

    public Saucer(PApplet applet) {
        super(applet);
        type = (int)(Math.random() * saucerTypes.length / 2);
        xCorners = saucerTypes[2 * type];
        yCorners = saucerTypes[2 * type + 1];
        corners = xCorners.length;

        color = 0xFFFFFFFF;
        xCenter = applet.width - 20;
        yCenter = (Math.random() * (applet.height - 100)) + 50;
        xSpeed = 1.5;
        ySpeed = 0;
        pointDirection = 0;
        alive = movingUp = movingDown = false;
        hitRadius = hitRadii[type];

        if (Math.random() < 0.5) {
            movingLeft = true;
        }
        else {
            movingLeft = false;
            xSpeed *= -1;
        }
        moveTime = applet.millis();
        moveWait = 1500;

        if (type == 0) {
            pointValue = 1000;
        }
        else {
            pointValue = 200;
        }
    }

    public void move(Spaceship ship) {
        super.move();
        if (applet.millis() - moveTime >= moveWait) {
            double angle = 0;
            if (movingUp || movingDown) {
                movingUp = movingDown = false;
                angle = pointDirection * (Math.PI / 180);
            }
            else if (ship.getY() > yCenter) {
                movingUp = true;
                angle = (pointDirection + 45) * (Math.PI / 180);
            }
            else {
                movingDown = true;
                angle = (pointDirection - 45) * (Math.PI / 180);
            }
            xSpeed = 1.5 * Math.cos(angle);
            ySpeed = 1.5 * Math.sin(angle);

            if (movingLeft) {
                xSpeed *= -1;
            }
            
            moveTime = applet.millis();
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean newAlive) {
        alive = newAlive;
    }

    public int getType() {
        return type;
    }
    
    public double getHitRadius() {
        return hitRadius;
    }
}
