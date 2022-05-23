import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import processing.core.PApplet;
import processing.core.PImage;
import de.bezier.guido.*;

public class Minesweeper extends PApplet {

    // Declare and initialize all potential game types
    private static int[] allRows = {8, 16, 16, 35};
    private static int[] allCols = {8, 16, 30, 45};
    private static int[] allMines = {10, 40, 99, 324};
    private static int index = -1;

    // Declare NUM_ROWS, NUM_COLS, NUM_MINES
    private static int NUM_ROWS, NUM_COLS, NUM_MINES;

    // Declare border properties
    private static final int topBorder = 40;
    private static final int sideBorder = 15;
    private static final int bottomBorder = 30;
    private PImage img;

    private boolean isGameOver = false;
    private boolean firstClickOver = false;

    // 2D array of minesweeper buttons
    private MSButton[][] buttons;
    // ArrayList of mined buttons
    private ArrayList<MSButton> mines;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String answer;
        boolean answered = false;

        clearConsole();
        // Determine game difficulty
        System.out.println("< Modes: Beginner Intermediate Expert Impossible >");
        while (!answered) {
            System.out.print("Difficulty: ");
            answer = input.nextLine();
            answered = true;
            if (answer.equalsIgnoreCase("Beginner")) {
                index = 0;
            }
            else if (answer.equalsIgnoreCase("Intermediate")) {
                index = 1;
            }
            else if (answer.equalsIgnoreCase("Expert")) {
                index = 2;
            }
            else if (answer.equalsIgnoreCase("Impossible")) {
                index = 3;
            }
            else {
                answered = false;
                System.out.println("Invalid Answer. Try Again.");
            }
        }
        input.close();

        // Initialize NUM_ROWS, NUM_COLS, NUM_MINES
        NUM_ROWS = allRows[index];
        NUM_COLS = allCols[index];
        NUM_MINES = allMines[index];

        PApplet.main("Minesweeper");
    }

    public void settings() {
        int windowWidth = 20 * NUM_COLS + 2 * sideBorder;
        int windowHeight = 20 * NUM_ROWS + topBorder + bottomBorder;
        size(windowWidth, windowHeight);
    }

    public void setup() {
        imageMode(CENTER);

        // Make the manager
        Interactive.make(this);

        // Initialize buttons
        buttons = new MSButton[NUM_ROWS][NUM_COLS];
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                buttons[row][col] = new MSButton(row, col);
            }
        }

        // Initialize the game
        img = loadImage("HappyFace.png");
        setMines();
    }

    public void draw() {
        background(200);
        image(img, width / 2, topBorder / 2);
        textAlign(RIGHT, CENTER);
        textSize(15);
        text("Press 'R' to Reset", width - 10, height - 15);

        if (isWon()) {
            displayWinningMessage();
        }
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

    // Reset game
    public void keyPressed() {
        if (key == 'r') {
            isGameOver = firstClickOver = false;
            for (MSButton[] buttonRow : buttons) {
                for (MSButton button : buttonRow) {
                    button.resetButton();
                }
            }
            img = loadImage("HappyFace.png");
            setMines();
        }
    }

    // Determine if game is won
    public boolean isWon() {
        int numClicked = 0;
        for (MSButton[] buttonRow : buttons) {
            for (MSButton button : buttonRow) {
                boolean isClicked = button.isClicked();
                boolean isFlagged = button.isFlagged();
                boolean isMine = mines.contains(button);

                if (isClicked && !isFlagged && !isMine) {
                    numClicked++;
                }
            }
        }
        return NUM_ROWS * NUM_COLS - numClicked - mines.size() == 0;
    }

    // Change mine labels to X and img to SadFace.png
    public void displayLosingMessage() {
        isGameOver = true;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
               if (mines.contains(buttons[row][col])) {
                   buttons[row][col].setLabel("X");
               }
            }
        }
        img = loadImage("SadFace.png");
    }

    // Change img to ShadesFace.png
    public void displayWinningMessage() {
        isGameOver = true;
        img = loadImage("ShadesFace.png");
    }

    // Create ArrayList of mines
    public void setMines() {
        mines = new ArrayList<MSButton>();
        while (mines.size() <= NUM_MINES) {
            int row = (int)(Math.random() * NUM_ROWS);
            int col = (int)(Math.random() * NUM_COLS);

            MSButton tempButton = buttons[row][col];
            if (!mines.contains(tempButton)) {
                mines.add(tempButton);
            }
        }
    }

    // Determine if location is Valid
    public boolean isValid(int r, int c) {
        boolean isValidRow = 0 <= r && r < NUM_ROWS;
        boolean isValidCol = 0 <= c && c < NUM_COLS;
        return isValidRow && isValidCol;
    }

    // Determine amount of mines around a button
    public int countMines(int row, int col) {
        int numMines = 0;
        for (int rowShift = -1; rowShift <= 1; rowShift++) {
            for (int colShift = -1; colShift <= 1; colShift++) {
                int x = row + rowShift;
                int y = col + colShift;

                if (isValid(x, y) && mines.contains(buttons[x][y])) {
                    numMines++;
                }
            }
        }
        return numMines;
    }

    public class MSButton {

        private int myRow, myCol, numMines;
        private float x, y, width, height;
        private boolean clicked, flagged;
        private String myLabel;

        private int[] colors = {0xFF000000, 0xFF0000FF, 0xFF008100, 0xFFFF1300,
            0xFF000083, 0xFF810500, 0xFF2A9494, 0xFF000000, 0xFF808080};

        public MSButton(int row, int col) {
            numMines = 0;
            width = height = 20;
            myRow = row;
            myCol = col;
            x = myCol * width + sideBorder;
            y = myRow * height + topBorder;
            myLabel = "";
            flagged = clicked = false;

            // Register it with manager
            Interactive.add(this);
        }

        // Called by manager
        public void mousePressed() {
            // Ensures first click isn't a mine or has neighboring mines
            if (!firstClickOver && mouseButton == LEFT && !flagged) {
                while (mines.contains(this) || countMines(myRow, myCol) != 0) {
                    setMines();
                }
                firstClickOver = true;
            }

            if (isGameOver) {
                return;
            }
            else if (mouseButton == RIGHT && !clicked) {
                flagged = !flagged;
                img = loadImage("SurprisedFace.png");
            }
            else if (!flagged) {
                clicked = true;
                if (mines.contains(this)) {
                    displayLosingMessage();
                }
                else if (countMines(myRow, myCol) > 0) {
                    numMines = countMines(myRow, myCol);
                    setLabel(numMines);
                }
                else {
                    for (int row = myRow - 1; row <= myRow + 1; row++) {
                        for (int col = myCol - 1; col <= myCol + 1; col++) {
                            if (isValid(row, col) && !buttons[row][col].isClicked()) {
                                buttons[row][col].mousePressed();
                            }
                        }
                    }
                }
            }
        }

        public void mouseReleased() {
            if (!isGameOver && mouseButton == RIGHT && !clicked) {
                img = loadImage("HappyFace.png");
            }
        }


        public void draw() {
            if (flagged) {
                if (isGameOver && !mines.contains(this)) {
                    fill(0x75FF0000);
                }
                else {
                    fill(0xCF000000);
                }
            }
            else if (clicked && mines.contains(this)) {
                fill(255, 0, 0);
            }
            else if (clicked) {
                fill(200);
            }
            else {
                fill(100);
            }

            rect(x, y, width, height);
            fill(0);
            textAlign(CENTER, CENTER);
            fill(colors[numMines]);
            if (isGameOver && flagged) {
                fill(255);
            }
            textSize(12);
            text(myLabel, x + width / 2, y + height / 2);
            fill(0);
        }

        public void resetButton() {
            myLabel = "";
            numMines = 0;
            clicked = flagged = false;
        }

        public void setLabel(String newLabel) {
            myLabel = newLabel;
        }

        public void setLabel(int newLabel) {
            myLabel = "" + newLabel;
        }

        public void setClick(boolean newClick) {
            clicked = newClick;
        }

        public void setFlagged(boolean newFlag) {
            flagged = newFlag;
        }

        public boolean isFlagged() {
            return flagged;
        }

        public boolean isClicked() {
            return clicked;
        }
    }
}
