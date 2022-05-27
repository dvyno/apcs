import processing.core.PApplet;
import processing.core.PImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Weapon {
    private PApplet applet;
    private Player player;
    private PImage sprite;
    private double x, y;
    private int width, height;
    private HitBox hitbox;

    public Weapon(PApplet applet, Player player, String file) {
        this.applet = applet;
        this.player = player;
        String path = "assets/weapons/" + file + ".png";
        sprite = applet.loadImage(path);
        try {
            BufferedImage img = ImageIO.read(new File("src/" + path));
            width = img.getWidth();
            height = img.getHeight();
        }
        catch (Exception e) {
            System.out.println("Couldn't read image");
        }

        x = player.getX();
        y = player.getY();

        double xLeft = x - width / 2;
        double yBottom = y - height / 2;
        hitbox = new HitBox(applet, xLeft, yBottom, xLeft + width, yBottom + height);
    }

    public void show() {
        applet.imageMode(PApplet.CENTER);

        x = player.getX();
        y = player.getY();

        int xScale = 1;
        int xShift = 10;

        if (player.isFacingLeft()) {
            xScale *= -1;
            xShift *= -1;
        }

        x -= xShift;
        y -= 5;

        applet.pushMatrix();
        applet.translate((float)x, (float)y);
        applet.scale(xScale, 1);
        applet.image(sprite, 0, 0);
        applet.popMatrix();

        hitbox.setBottomLeft(x - width / 2, y - height / 2);
        hitbox.setTopRight(x + width / 2, y + height / 2);
        hitbox.show();
    }

    /**
     * Displays <code>Weapon</code> depending on how <code>Player</code> is
     * moving
     */
    public void attack() {
        applet.imageMode(PApplet.CENTER);

        x = player.getX();
        y = player.getY();

        // Set default attack to right and horizontal
        int xScale = 1;
        int xShift = 40;
        int yShift = 15;
        double angle = Math.PI / 2;

        boolean moveHorizontal = player.isMovingLeft() || player.isMovingRight();

        if (player.isMovingUp() && (player.getRecentKey() == 'w' || !moveHorizontal)) {
            // Rotate sword upwards
            angle = 0;
            xShift = 0;
            yShift = -40;
        }
        else if (player.isMovingDown() && (player.getRecentKey() == 's' || !moveHorizontal)) {
            // Rotate sword downwards
            angle = Math.PI;
            xShift = 0;
            yShift = 55;
        }

        // Flip sword if Player is facing left
        if (player.isFacingLeft()) {
            xScale *= -1;
            xShift *= -1;
        }

        x += xShift;
        y += yShift;

        applet.pushMatrix();
        applet.translate((float)x, (float)y);
        applet.scale(xScale, 1);
        applet.rotate((float)angle);
        applet.image(sprite, 0, 0);
        applet.popMatrix();

        // Set hitbox based on if sword if vertical or horizontal
        if (angle == Math.PI || angle == 0) {
            hitbox.setBottomLeft(x - width / 2, y - height / 2);
            hitbox.setTopRight(x + width / 2, y + height / 2);
        }
        else {
            hitbox.setBottomLeft(x - height / 2, y - width / 2);
            hitbox.setTopRight(x + height / 2, y + width / 2);
        }

        hitbox.show();
    }

    public HitBox getHitBox() {
        return hitbox;
    }
}
