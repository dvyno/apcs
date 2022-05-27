import processing.core.PApplet;
import processing.sound.*;

public class Room {
    // 2D Tile Array Variables
    private Tile[][] tiles;
    private int ROWS, COLS;

    // Boundary variables for entities
    private int leftBorder;
    private int rightBorder;
    private int upBorder;
    private int downBorder;

    // Check if Doors are closed
    private boolean doorsClosed;

    // Doors Open
    private SoundFile doorOpen;

    public Room(PApplet applet, int xShift, int yShift, int ROWS, int COLS) {
        this.ROWS = ROWS;
        this.COLS = COLS;
        tiles = new Tile[ROWS][COLS];

        String file;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (row > 0 && row < ROWS - 1 && col > 0 && col < COLS - 1) {
                    if (Math.random() < .8) {
                        file = "floor_1";
                    }
                    else {
                        file = "floor_" + PApplet.nf((int) (Math.random() * 7) + 2, 1);
                    }
                }
                else {
                    file = "wall_mid";
                }

                tiles[row][col] = new Tile(applet, col * 32 + 16 + xShift, row * 32 + 16 + yShift);
                tiles[row][col].setTile(file);
            }

        }

        closeDoors();

        doorOpen = new SoundFile(applet, "door/door.wav");
        leftBorder = xShift + 32;
        rightBorder = xShift + (COLS - 1) * 32;
        upBorder = yShift + 16;
        downBorder = yShift + (ROWS - 1) * 32;
    }

    public void show() {
        for (Tile[] row : tiles) {
            for (Tile t: row) {
                t.show();
            }
        }
    }

    public int getLeftBorder() {
        return leftBorder;
    }

    public int getRightBorder() {
        return rightBorder;
    }

    public int getDownBorder() {
        return downBorder;
    }

    public int getUpBorder() {
        return upBorder;
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    public void openDoors() {
        doorsClosed = false;
        String door = "doors_leaf_open";
        doorOpen.play(1, (float) 0.75);
        tiles[0][COLS / 2].setTile(door);
        tiles[ROWS / 2][0].setTile(door);
        tiles[ROWS / 2][COLS - 1].setTile(door);
        tiles[ROWS - 1][COLS / 2].setTile(door);
    }

    public void closeDoors() {
        doorsClosed = true;
        String door = "doors_leaf_closed";
        tiles[0][COLS / 2].setTile(door);
        tiles[ROWS / 2][0].setTile(door);
        tiles[ROWS / 2][COLS - 1].setTile(door);
        tiles[ROWS - 1][COLS / 2].setTile(door);
    }

    public void createFloor() {
        String file;
        for (int row = 1; row < ROWS - 1; row++) {
            for (int col = 1; col < COLS - 1; col++) {
                if (Math.random() < .8) {
                    file = "floor_1";
                }
                else {
                    file = "floor_" + PApplet.nf((int) (Math.random() * 7) + 2, 1);
                }

                tiles[row][col].setTile(file);
            }
        }
    }

    public boolean doorsClosed() {
        return doorsClosed;
    }
}
