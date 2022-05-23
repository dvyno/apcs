import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;
import processing.core.PApplet;
import de.bezier.guido.*;

public class GameOfLife extends PApplet {
    // Declare Game Parameters
    private static int gameWidth, gameHeight, NUM_ROWS, NUM_COLS;

    // 2D array of Life buttons each representing one cell
    private Life[][] buttons;

    // 2D array of booleans to store state of buttons array
    private boolean[][] buffer;

    // Used to start and stop program
    private boolean running = true;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean answered = false;
        int answer = 0;

        clearConsole();
        System.out.println("Number of Rows/Columns:");
        while (!answered) {
            try {
                System.out.print("Enter an integer between 20 and 80: ");
                answer = input.nextInt();
                if (!(answer < 20 || answer > 80)) {
                    answered = true;
                    break;
                }
                System.out.println("Out of Range. Try Again.");
            }
            catch(InputMismatchException e) {
                System.out.println("Invalid Answer. Try Again.");
                input.next();
            }
        }
        input.close();

        NUM_ROWS = NUM_COLS = answer;
        gameWidth = gameHeight = answer * 10;

        PApplet.main("GameOfLife");
    }

    public void settings() {
        size(gameWidth, gameHeight + 20);
    }

    public void setup() {
        frameRate(6);
        // Make the manager
        Interactive.make(this);

        // Initialize Buttons
        buttons = new Life[NUM_ROWS][NUM_COLS];
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                buttons[row][col] = new Life(row, col);
            }
        }

        // Initialize Buffer
        buffer = new boolean[NUM_ROWS][NUM_COLS];

    }

    public void draw() {
        background(0);
        fill(200);
        rect(0, gameHeight, gameWidth, gameHeight + 20);
        textSize(10);
        fill(0);
        textAlign(RIGHT, CENTER);
        text("Press 'R' to Reset", width - 10, height - 10);
        textAlign(LEFT, CENTER);
        text("Press 'P' to Pause", 0 + 10, height - 10);

        // Pause Program
        if (!running) {
            return;
        }
        copyFromButtonsToBuffer();

        // Draw Buttons
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                Life currentLife = buttons[row][col];
                int neighbors = countNeighbors(row, col);
                boolean isAlive = currentLife.getLife();
                if (neighbors == 3 || (neighbors == 2 && isAlive)) {
                    buffer[row][col] = true;
                }
                else {
                    buffer[row][col] = false;
                }
                currentLife.draw();
            }
        }

        copyFromBufferToButtons();
    }

    // https://stackoverflow.com/a/64038023
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ex) {}
    }

    // Reset and Pause game
    public void keyPressed() {
        if (key == 'r') {
            for (int i = 0; i < NUM_ROWS; i++) {
                for (int j = 0; j < NUM_ROWS; j++) {
                    buttons[i][j].setLife(false);
                }
            }
        }
        else if (key == 'p') {
            running = !running;
        }
    }

    public void copyFromBufferToButtons() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                buttons[row][col].setLife(buffer[row][col]);
            }
        }
    }

    public void copyFromButtonsToBuffer() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                buffer[row][col] = buttons[row][col].getLife();
            }
        }
    }

    public boolean isValid(int r, int c) {
        boolean isValidRow = 0 <= r && r < NUM_ROWS;
        boolean isValidCol = 0 <= c && c < NUM_COLS;
        return isValidRow && isValidCol;
    }

    public int countNeighbors(int row, int col) {
        int neighbors = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int r = row + i;
                int c = col + j;
                if (isValid(r, c) && !(i == 0 && j == 0)) {
                    if (buttons[r][c].getLife()) {
                        neighbors++;
                    }
                }
            }
        }
        return neighbors;
    }


    public class Life {
        private int myRow, myCol;
        private float x, y, width, height;
        private boolean alive;

        public Life(int row, int col) {
            width = gameWidth / NUM_COLS;
            height = gameHeight / NUM_ROWS;
            myRow = row;
            myCol = col;
            x = myCol * width;
            y = myRow * height;
            alive = Math.random() < .5;

            // Register with the manager
            Interactive.add(this);
        }

        // Called by manager, turn cell on/off with mouse press
        public void mousePressed() {
            alive = !alive;
        }

        public void draw() {
            if (!alive) {
                fill(0);
            }
            else {
                fill(150);
            }
            rect(x, y, width, height);
        }

        public boolean getLife() {
            return alive;
        }

        public void setLife(boolean newLife) {
            alive = newLife;
        }
    }
}
