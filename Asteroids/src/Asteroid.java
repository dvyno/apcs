import processing.core.PApplet;

public class Asteroid extends Floater {
    private double rotationSpeed;
    private int type, size;
    private static double[][] asteroidTypes = {
        // Type 0
        {7.7, 0.7, -4.3, -8.3, -8.3, -4.3, -0.3, 3.7, 7.7, 5.7},
        {-5.6, -8.6, -8.6, -3.6, 3.4, 8.4, 3.4, 8.4, 3.4, -0.6},
        // Type 1
        {3.6666666666666665, -1.3333333333333333, -4.333333333333333,
            -6.333333333333333, -4.333333333333333, -6.333333333333333,
            -3.3333333333333335, 0.6666666666666667, 4.666666666666667,
            6.666666666666667, 3.6666666666666665, 6.666666666666667},
        {-7.5, -5.5, -7.5, -3.5, -0.5, 3.5, 6.5, 4.5, 6.5, 3.5, 1.5, -1.5},
        // Type 2
        {0.25, -3.75, -2.75, -6.75, -5.75, -2.75, 0.25, 3.25, 5.25, 6.25, 4.25,
            2.25},
        {-7.083333333333333, -6.083333333333333, -4.083333333333333,
            -1.0833333333333333, 4.916666666666667, 7.916666666666667,
            3.9166666666666665, 6.916666666666667, 3.9166666666666665,
            -3.0833333333333335, -4.083333333333333, -2.0833333333333335},
        // Type 3
        {-2.2727272727272725, 3.7272727272727275, 2.7272727272727275,
            4.7272727272727275, 6.7272727272727275, 3.7272727272727275,
            -3.2727272727272725, -7.2727272727272725, -5.2727272727272725,
            -1.2727272727272727, -2.2727272727272725},
        {-5.090909090909091, -5.090909090909091, -0.09090909090909091,
            -2.090909090909091, 0.9090909090909091, 7.909090909090909,
            8.909090909090908, 3.909090909090909, -3.090909090909091,
            -1.0909090909090908, -5.090909090909091}
    };
    private static double[] hitRadii = {9.503288904374106, 8.32165848854662,
        7.685199004386563, 7.826237921249264};
    private double hitRadius;

    public Asteroid(PApplet applet) {
        super(applet);
        type = (int)(Math.random() * 4);
        size = 4;
        xCorners = scaleArray(size, asteroidTypes[2 * type]);
        yCorners = scaleArray(size, asteroidTypes[2 * type + 1]);
        hitRadius = 4 * hitRadii[type];
        corners = xCorners.length;

        color = 0xFFFFFFFF;
        xCenter = applet.width / 2;
        yCenter = applet.height / 2;
        while (insideBounds()) {
            xCenter = (Math.random() * (applet.width - 40) + 20);
            yCenter = (Math.random() * (applet.height - 50) + 50);
        }
        pointDirection = Math.random() * 360;
        xSpeed = ySpeed = 0;
        super.accelerate(1.25);
        rotationSpeed = Math.random() + 0.5;
        if (Math.random() < 0.5) {
            rotationSpeed *= -1;
        }
        pointValue = 20;
    }

    public Asteroid(PApplet applet, Asteroid newAsteroid, Floater obj) {
        super(applet);
        type = newAsteroid.type;
        size = newAsteroid.size / 2;
        xCorners = scaleArray(size, asteroidTypes[2 * type]);
        yCorners = scaleArray(size, asteroidTypes[2 * type + 1]);
        hitRadius = 4 * hitRadii[type];
        corners = xCorners.length;

        color = 0xFFFFFFFF;
        xCenter = newAsteroid.xCenter + Math.random() * 30;
        yCenter = newAsteroid.yCenter + Math.random() * 30;
        pointDirection = newAsteroid.pointDirection + Math.random() * 61 - 31;
        xSpeed = newAsteroid.xSpeed + Math.random() - 0.5 + 0.1 * obj.getXSpeed();
        ySpeed = newAsteroid.ySpeed + Math.random() - 0.5 + 0.1 * obj.getYSpeed();
        super.accelerate(0.01);
        rotationSpeed = 1.5;
        if (size == 2) {
            pointValue = 50;
        }
        else {
            pointValue = 100;
        }
    }

    @Override
    public void move() {
        super.turn(rotationSpeed);
        super.move();
    }

    public double getHitRadius() {
        return hitRadius;
    }

    public int getSize() {
        return size;
    }

    private boolean insideBounds() {
        boolean insideX = xCenter > 80 && xCenter < applet.width - 80;
        boolean insideY = yCenter > 100 && yCenter < applet.height - 100;
        return insideX && insideY;
    }
}
