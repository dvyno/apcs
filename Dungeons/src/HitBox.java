import processing.core.PApplet;

public class HitBox {
    private PApplet applet;

    // Bottom Left Coordinate
    private double xLeft, yBottom;

    // Top Right Coordinate
    private double xRight, yTop;

    // Toggled Variable to Show HitBox
    private static boolean showHitBox;

    public HitBox(PApplet applet, double xLeft, double yBottom, double xRight, double yTop) {
        this.applet = applet;
        this.xLeft = xLeft;
        this.yBottom = yBottom;
        this.xRight = xRight;
        this.yTop = yTop;
        showHitBox = false;
    }

    public static void toggle() {
        showHitBox = !showHitBox;
    }

    public static boolean canShowHitBox() {
        return showHitBox;
    }

    public void show() {
        if (showHitBox) {
            double width = xRight - xLeft;
            double height = yTop - yBottom;

            applet.noFill();
            applet.stroke(255);
            applet.rect((float)xLeft, (float)yBottom, (float)width, (float)height);
            applet.stroke(0);
            applet.fill(255);
        }
    }

    /**
     * Determine whether two rectangular <code>HitBox</code>es overlap each
     * other
     * @param h A <code>HitBox</code>
     * @return Whether <code>h</code> is nether above nor left of
     *      <code>this</code> <code>HitBox</code>
     */
    public boolean isOverlapping(HitBox h) {
        boolean isAbove = this.yTop < h.yBottom || h.yTop < this.yBottom;
        boolean isLeft = this.xRight < h.xLeft || h.xRight < this.xLeft;
        return !(isAbove || isLeft);
    }

    public double[] getBottomLeft() {
        return new double[]{xLeft, yBottom};
    }

    public double[] getTopRight() {
        return new double[]{xRight, yTop};
    }

    public void setBottomLeft(double xLeft, double yBottom) {
        this.xLeft = xLeft;
        this.yBottom = yBottom;
    }

    public void setTopRight(double xRight, double yTop) {
        this.xRight = xRight;
        this.yTop = yTop;
    }
}
