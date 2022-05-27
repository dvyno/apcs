import processing.core.PApplet;
import processing.core.PImage;

public class Heart {
    private PImage[] states;
    private PApplet applet;
    private int x, y, state;

    public Heart(PApplet applet, int x, int y) {
        this.applet = applet;
        this.x = x;
        this.y = y;
        state = 0;
        String path = "src/assets/ui_heart/ui_heart_";
        states = new PImage[3];
        states[0] = applet.loadImage(path + "empty.png");
        states[1] = applet.loadImage(path + "half.png");
        states[2] = applet.loadImage(path + "full.png");
    }

    public void show() {
        applet.pushMatrix();
        applet.translate(x, y);
        applet.image(states[state], 0, 0);
        applet.popMatrix();
    }

    public int getState() {
        return state;
    }

    public void decreaseState() {
        state--;
    }

    public void increaseState() {
        state++;
    }
}
