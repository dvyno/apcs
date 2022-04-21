import processing.core.PApplet;

public class Star extends Floater {
    private static int[] colors = {0x7A5C73FC, 0x7A8893FB, 0x7AC8D2FC,
        0x7AFFFFFF, 0x7AFFEF70, 0x7AFFEF70, 0x7AF5AD5C, 0x7AF07451};

    public Star (PApplet applet, double xPos, double yPos, int radius) {
        super(applet);
        corners = 10;
        xCorners = new double[corners];
        yCorners = new double[corners];
        createPoints(radius);
        color = colors[(int)(Math.random() * colors.length)];

        xCenter = xPos;
        yCenter = yPos;

        xSpeed = 0;
        ySpeed = 0;
        pointDirection = Math.random() * 360;
    }

    // https://math.stackexchange.com/a/3582355
    public void createPoints(int radius) {
        for (int i = 0; i < corners; i += 2) {
            int k = i / 2;
            double angle = (2 * Math.PI * k / 5 + Math.PI / 2);
            xCorners[i] = 2 * radius * Math.cos(angle);
            yCorners[i] = 2 * radius * Math.sin(angle);
            xCorners[i + 1] = radius * Math.cos(angle + Math.PI / 5);
            yCorners[i + 1] = radius * Math.sin(angle + Math.PI / 5);
        }
    }
}
