import processing.core.PApplet;

public class Bullet extends Floater {
    private Spaceship ship;
    private Saucer saucer;
    private int travelEnd;

    public Bullet(PApplet applet, Spaceship initShip) {
        super(applet);
        ship = initShip;
        color = 0xFFFFFFFF;
        xCenter = ship.getX();
        yCenter = ship.getY();
        xSpeed = ship.getXSpeed();
        ySpeed = ship.getYSpeed();
        pointDirection = ship.getPointDirection();
        super.accelerate(6.0);
        travelEnd = applet.millis() + 1500;
    }

    public Bullet(PApplet applet, Spaceship initShip, Saucer initSaucer) {
        super(applet);
        ship = initShip;
        saucer = initSaucer;
        color = 0xFFFFFFFF;
        xCenter = saucer.getX();
        yCenter = saucer.getY();
        xSpeed = ySpeed = 0;
        double yDiff = ship.getY() - saucer.getY();
        double xDiff = ship.getX() - saucer.getX();
        if (xDiff == 0) {
            pointDirection = 90;
        }
        else {
            pointDirection = Math.atan(yDiff / xDiff) * 180 / Math.PI;
        }
        if (xDiff < 0) {
            pointDirection += 180;
        }
        pointDirection += Math.random() * 61 - 30;
        super.accelerate(6.0);
        travelEnd = applet.millis() + 1000;
    }

    @Override
    public void show() {
        applet.fill(color);
        applet.ellipse((float)xCenter,(float)yCenter,6,6);
    }

    public int endTime() {
        return travelEnd;
    }
}
