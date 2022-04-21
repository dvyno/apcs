import processing.core.PApplet;

public class Floater {
    protected PApplet applet;
    protected int corners;
    protected double[] xCorners, yCorners;
    protected int color;
    protected double xCenter, yCenter;
    protected double xSpeed, ySpeed;
    protected double pointDirection;
    protected int pointValue;

    public Floater(PApplet initApplet) {
        applet = initApplet;
    }

    /**
     * Accelerates <code>Floater</code> in the direction it is pointing
     * (<code>pointDirection</code>)
     * @param dAmount
     *      the magnitude of speed the <code>Floater</code> will increase by
     */
    public void accelerate(double dAmount) {
        double dRadians = pointDirection * (Math.PI / 180);
        xSpeed += ((dAmount) * Math.cos(dRadians));
        ySpeed += ((dAmount) * Math.sin(dRadians));
    }

    /**
     * Rotates <code>Floater</code> by a given number of degrees
     * @param degreesOfRotation
     *      the amount the <code>Floater</code> will rotate by
     */
    public void turn(double degreesOfRotation) {
        pointDirection += degreesOfRotation;
    }

    /**
     * Moves <code>Floater</code> in the current direction of travel
     */
    public void move() {
        xCenter += xSpeed;
        yCenter += ySpeed;

        // Wrap around screen
        if (xCenter > applet.width) {
            xCenter = 20;
        }
        else if (xCenter < 20) {
            xCenter = applet.width - 20;
        }

        if (yCenter > applet.height - 50) {
            yCenter = 50;
        }
        else if (yCenter < 50) {
            yCenter = applet.height - 50;
        }
    }

    /**
     * Draws <code>Floater</code> at the current position
     */
    public void show() {
        applet.noFill();
        applet.stroke(color);

        // Translates center of ship to correct position
        applet.translate((float)xCenter, (float)yCenter);

        // Rotate polygon to correct direction
        float dRadians = (float)(pointDirection * (Math.PI / 180));
        applet.rotate(dRadians);

        // Draw polygon
        applet.beginShape();
        for (int i = 0; i < corners; i++) {
            applet.vertex((float)xCorners[i], (float)yCorners[i]);
        }
        applet.endShape(applet.CLOSE);

        // Reverse transformations
        applet.rotate(-1 * dRadians);
        applet.translate(-1 * (float)xCenter, -1 * (float)yCenter);
    }

    public double[] scaleArray(double scalar, double[] nums) {
        double[] result = new double[nums.length];
        for (int i = 0; i < nums.length; i++) {
            result[i] = scalar * nums[i];
        }
        return result;
    }

    public double getX() {
        return xCenter;
    }

    public double getY() {
        return yCenter;
    }

    public double getXSpeed() {
        return xSpeed;
    }

    public double getYSpeed() {
        return ySpeed;
    }

    public double getPointDirection() {
        return pointDirection;
    }

    public int getPointValue() {
        return pointValue;
    }
}
