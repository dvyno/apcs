import processing.core.PApplet;
import java.util.ArrayList;

public class Breakout extends PApplet {
    private Ball b;
    private Paddle p;

    private int brickRows, bricksPerRow;
    private ArrayList<Brick> bricks;
    private int score;

    public static void main(String[] args) {
        PApplet.main("Breakout");
    }

    public void settings() {
        size(600, 650);
    }

    public void setup() {
        brickRows = 10;
        bricksPerRow = 10;
        resetGame();
    }

    public void draw() {
        displayHUD();
        p.move();
        b.move(p);
        p.display();
        b.display();

        for (int i = bricks.size() - 1; i >= 0; i--) {
            Brick brick = bricks.get(i);
            brick.display();
            if (brick.checkCollision(b)) {
                bricks.remove(brick);
                score++;
            }
        }

        if (keyPressed && key == 'r') {
            resetGame();
        }
    }

    public void resetGame() {
        b = new Ball(this);
        p = new Paddle(this);
        bricks = new ArrayList<Brick>();
        score = 0;

        int brickLength = width / bricksPerRow;
        int brickHeight = 20;

        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < bricksPerRow; j++) {
                int x = brickLength * j;
                int y = brickHeight * i + 50;
                Brick tempBrick = new Brick(this, x, y, brickLength, brickHeight);
                bricks.add(tempBrick);
            }
        }
    }

    public void displayHUD() {
        background(0);
        fill(220);
        rect(0, 40, width, 10);
        textSize(30);
        textAlign(CENTER, CENTER);
        text(score, width / 2, 20);
        textAlign(RIGHT, CENTER);
        textSize(15);
        text("Press 'R' to Reset", width - 10, 20);
    }

    public void keyPressed() {
        if(keyCode == LEFT && !p.isMovingRight()) {
            p.setMovingLeft(true);
        }
        if (keyCode == RIGHT && !p.isMovingLeft()) {
            p.setMovingRight(true);
        }
    }

    public void keyReleased() {
        if (keyCode == LEFT) {
            p.setMovingLeft(false);
        }
        if (keyCode == RIGHT) {
            p.setMovingRight(false);
        }
    }
}
