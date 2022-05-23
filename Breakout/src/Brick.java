import processing.core.PApplet;

public class Brick {
    private PApplet applet;
    private int x, y;
    private int brickLength, brickHeight;
    private int red, green, blue;
    private static int[] redVals = {250, 196, 146, 100, 57, 8, 0, 8, 33, 42};
    private static int[] greenVals = {250, 236, 220, 201, 180, 159, 137, 115, 93, 72};
    private static int[] blueVals = {110, 116, 126, 135, 142, 143, 138, 127, 110, 88};

    public Brick(PApplet applet_, int x_, int y_, int length, int height) {
        applet = applet_;
        x = x_;
        y = y_;
        brickLength = length;
        brickHeight = height;

        red = redVals[(y - 50) / height];
        green = greenVals[(y - 50) / height];
        blue = blueVals[(y - 50) / height];
    }

    public void display() {
        applet.fill(red, green, blue);
        applet.rect(x, y, brickLength, brickHeight);
    }

    public boolean checkCollision(Ball b) {
        // test whether ball has hit the top
        if(b.getX() > x - b.getDiam() / 2 &&
                b.getX() < x + brickLength + b.getDiam() / 2 &&
                Math.abs(b.getY() - (y - b.getDiam() / 2)) < 3)
        {
            b.bounceVertical();
            return true;
        }

        // test whether ball has hit the bottom
        if(b.getX() > x - b.getDiam() / 2 &&
                b.getX() < x + brickLength + b.getDiam() / 2 &&
                Math.abs(b.getY() - (y + brickHeight + b.getDiam() / 2)) < 3)
        {
            b.bounceVertical();
            return true;
        }

        // test whether ball has hit the left
        if(b.getY() > y - b.getDiam() / 2 &&
                b.getY() < y + brickHeight + b.getDiam() / 2 &&
                Math.abs(b.getX() - (x - b.getDiam() / 2)) < 3)
        {
            b.bounceHorizontal();
            return true;
        }

        // test whether ball has hit the right
        if(b.getY() > y - b.getDiam() / 2 &&
                b.getY() < y + brickHeight + b.getDiam() / 2 &&
                Math.abs(b.getX() - (x + brickLength + b.getDiam() / 2)) < 3)
        {
            b.bounceHorizontal();
            return true;
        }
        return false;
    }
}
