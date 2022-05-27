import processing.core.PApplet;

public class Enemy extends Entity {
    protected int moveTime, moveWait;
    protected int score;

    public Enemy(PApplet applet, double x, double y, Room room, String file) {
        super(applet, x, y, room, file);
    }

    @Override
    public void move() {
        /* Future Enemy Movement:
         * If enemy is not moving:
         * Check if tiles around it are walls
         * Decide which tile to move to
         * Change movement variables
         * Move to that tile
         */

        // Current Movement: move in random directions every 1.5 seconds
        if (applet.millis() - moveTime >= moveWait) {
            setMoveDown(false);
            setMoveUp(false);
            setMoveLeft(false);
            setMoveRight(false);

            // Vertical Movement
            double rand = Math.random();
            if (rand < 0.45) {
                setMoveUp(true);
            }
            else if (rand < 0.9) {
                setMoveDown(true);
            }

            // Horizontal Movement
            rand = Math.random();
            if (rand < 0.45) {
                setMoveRight(true);
            }
            else if (rand < 0.9) {
                setMoveLeft(true);
            }

            moveTime = applet.millis();
        }
        
        super.move();
    }

    @Override
    public void setHit(boolean isHit, Entity e) {
        super.setHit(isHit, e);
        moveTime += 500;
    }

    public int getScore() {
        return score;
    }
}
