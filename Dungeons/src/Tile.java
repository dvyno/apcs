import processing.core.PApplet;
import processing.core.PImage;

public class Tile {
    private PApplet applet;
    private int x;
    private int y;
    private PImage image;

    public Tile(PApplet applet, int x, int y) {
        this.applet = applet;
        this.x = x;
        this.y = y;
    }

    public void show() {
        applet.imageMode(PApplet.CENTER);
        applet.pushMatrix();
        applet.translate(x, y);
        applet.image(image, 0, 0);
        applet.popMatrix();
    }

    public int getRow() {
        return x / 32;
    }

    public int getCol() {
        return y / 32 - 2;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setTile(String files) {
        image = applet.loadImage("assets/tiles/" + files + ".png");
    }
}
