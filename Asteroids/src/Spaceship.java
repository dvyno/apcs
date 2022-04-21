import processing.core.PApplet;

public class Spaceship extends Floater {
    private boolean alive;
    private boolean accelerating;
    private boolean turningLeft;
    private boolean turningRight;
    private boolean jumpedSpace;
    private boolean firing;
    private double turnSpeed;

    public Spaceship(PApplet applet) {
        super(applet);
        corners = 4;
        xCorners = new double[]{-7.5, 16.5, -7.5, -1.5};
        yCorners = new double[]{-8, 0, 8, 0};

        color = 0xFFFFFFFF;
        xCenter = applet.width / 2;
        yCenter = applet.height / 2;
        xSpeed = 0;
        ySpeed = 0;
        pointDirection = Math.random() * 360;
        turnSpeed = 5;
        alive = true;
        accelerating = turningLeft = turningRight = jumpedSpace = false;
    }

    public Spaceship(PApplet applet, double xPos, double yPos) {
        super(applet);
        corners = 4;
        xCorners = new double[]{-7.5, 16.5, -7.5, -1.5};
        yCorners = new double[]{-8, 0, 8, 0};

        color = 0xFFFFFFFF;
        xCenter = xPos;
        yCenter = yPos;
        pointDirection = 270;
    }

    // Reset ship position and speed
    public void hyperspace() {
        if (jumpedSpace) {
            return;
        }

        xCenter = Math.random() * (applet.width - 40) + 20;
        yCenter = Math.random() * (applet.height - 100) + 50;
        xSpeed = 0;
        ySpeed = 0;
        pointDirection = Math.random() * 360;
    }

    @Override
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

        // Draw "rockets" when accelerating
        if (accelerating) {
            applet.line(-18, -6, -8, -4);
            applet.line(-18, 0, -8, 0);
            applet.line(-18, 6, -8, 4);
        }

        // Reverse transformations
        applet.rotate(-1 * dRadians);
        applet.translate(-1 * (float)xCenter, -1 * (float)yCenter);
    }

    public void setAlive(boolean newAlive) {
        alive = newAlive;
    }

    public void setAccelerating(boolean newAccelerating) {
        accelerating = newAccelerating;
    }

    public void setTurningLeft(boolean newTurningLeft) {
        turningLeft = newTurningLeft;
    }

    public void setTurningRight(boolean newTurningRight) {
        turningRight = newTurningRight;
    }

    public void setJumpedSpace(boolean newJumpedSpace) {
        jumpedSpace = newJumpedSpace;
    }

    public void setFiring(boolean newFiring) {
        firing = newFiring;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isAccelerating() {
        return accelerating;
    }

    public boolean isTurningLeft() {
        return turningLeft;
    }

    public boolean isTurningRight() {
        return turningRight;
    }

    public double getTurnSpeed() {
        return turnSpeed;
    }

    public boolean isFiring() {
        return firing;
    }
}
