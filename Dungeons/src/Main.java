import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

public class Main extends PApplet {
    // Number of Rows and Columns in Room
    private final int ROWS = 15;
    private final int COLS = 19;

    // Objects used while game is running
    private Room room;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Heart> lives;

    // Text Font
    private PFont PressStart2P;

    // Holds the Current Game State
    private int gameState;

    // Game Menu
    private String[] optionTexts;
    private int[] optionColors;
    private int selectIndex;

    // Character Selection Menu
    private PImage[][] characterImages;
    private String[] characterFiles;
    private int charSelectIndex;
    private int frameNum;

    // Weapon Selection Menu
    private PImage[] weaponImages;
    private String[] weaponFiles;
    private int weaponSelectIndex;

    // Variables used in Game
    private int score, roomsCleared, enemiesSpawned, enemiesKilled;

    // Game Over
    private String name;

    // Scoreboard
    private String scoreFile;
    private ArrayList<String> names;
    private ArrayList<Integer> scores;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size(COLS * 32, (ROWS + 2) * 32);
    }

    public void setup() {
        // Create and Set Font
        PressStart2P = createFont("PressStart2P/PressStart2P.ttf", 48);
        textFont(PressStart2P);

        // Start Menu
        optionTexts = new String[3];
        optionColors = new int[3];
        selectIndex = 0;

        // Character Selection Menu
        characterFiles = new String[]{"elf", "elf", "knight", "knight", "lizard", "lizard", "wizard", "wizard"};
        for (int i = 0; i < 8; i++) {
            String gender;
            if (i % 2 == 0) {
                gender = "_m";
            }
            else {
                gender = "_f";
            }
            String str = characterFiles[i];
            characterFiles[i] = str + gender + "/" + str + gender;
        }

        characterImages = new PImage[8][4];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                String path = "assets/" + characterFiles[i] + "_menu_f" + nf(j,1) + ".png";
                characterImages[i][j] = loadImage(path);
            }
        }
        
        frameNum = 0;
        charSelectIndex = 0;

        // Weapon Selection Menu
        weaponFiles = new String[]{"axe", "baton_with_spikes", "golden_sword", "knight_sword", "green_magic_staff", "red_magic_staff", "katana", "spear"};

        weaponImages = new PImage[8];
        for (int i = 0; i < 8; i++) {
            String path = "assets/weapons/" + weaponFiles[i] + "_menu.png";
            weaponImages[i] = loadImage(path);
        }
        weaponSelectIndex = 0;

        // Game Over Screen
        name = "";

        // Scoreboard
        names = new ArrayList<String>();
        scores = new ArrayList<Integer>();
        scoreFile = "src/resources/scores.csv";
        try {
            Scanner reader = new Scanner(new File(scoreFile));
            String line = "";
            String[] values;

            while (reader.hasNext()) {
                line = reader.nextLine();
                values = line.split(",");
                names.add(values[0]);
                scores.add(Integer.valueOf(values[1]));
            }

            reader.close();
        }
        catch (Exception e) {
            System.out.println("Scores cannot be created.");
        }
    }

    public void draw() {
        switch (gameState) {
            case 0:
                startMenu();
                break;
            case 1:
                settingsMenu();
                break;
            case 2:
                characterSelectionMenu();
                break;
            case 3:
                weaponSelectionMenu();
                break;
            case 4:
                playGame();
                break;
            case 5:
                gameOver();
                break;
            case 6:
                scoreBoard();
                break;
            case 7:
                creditsMenu();
                break;
        }
    }

    public void keyPressed() {
        if (key == 'p') {
            gameState = (gameState + 1) % 8;
        }
        switch (gameState) {
            // Start Menu
            case 0:
                if (key == 'w') {
                    selectIndex = (selectIndex + 2) % 3;
                }

                if (key == 's') {
                    selectIndex = (selectIndex + 1) % 3;
                }

                if (key == ' ') {
                    if (selectIndex == 0) {
                        gameState = 2;
                    }
                    else if (selectIndex == 1) {
                        gameState = 1;
                    }
                    else {
                        gameState = 7;
                    }
                }
                break;
            // Settings Menu
            case 1:
                if (key == ' ') {
                    HitBox.toggle();
                }

                if (key == BACKSPACE) {
                    gameState = 0;
                }
                break;
            // Character Select Menu
            case 2:
                if (key == 'w' || key == 's') {
                    if (selectIndex % 2 == 0) {
                        selectIndex++;
                    }
                    else {
                        selectIndex--;
                    }
                    frameNum = 0;
                }
                else if (key == 'a') {
                    selectIndex = (selectIndex + 6) % 8;
                    frameNum = 0;
                }
                else if (key == 'd') {
                    selectIndex = (selectIndex + 2) % 8;
                    frameNum = 0;
                }

                if (key == ' ') {
                    gameState = 3;
                    charSelectIndex = selectIndex;
                    selectIndex = 0;
                }
                break;
            // Weapon Select Menu
            case 3:
                if (key == 'w' || key == 's') {
                    if (selectIndex % 2 == 0) {
                        selectIndex++;
                    }
                    else {
                        selectIndex--;
                    }
                    frameNum = 0;
                }
                else if (key == 'a') {
                    selectIndex = (selectIndex + 6) % 8;
                    frameNum = 0;
                }
                else if (key == 'd') {
                    selectIndex = (selectIndex + 2) % 8;
                    frameNum = 0;
                }

                if (key == ' ') {
                    gameState = 4;
                    weaponSelectIndex = selectIndex;
                    selectIndex = 0;
                    createGame();
                }
                break;
            // Game
            case 4:
                if (!player.isHit()) {
                    if (key == 'w' && !player.isMovingDown()) {
                        player.setMoveUp(true);
                        player.setRecentKey('w');
                    }
                    if (key == 'a' && !player.isMovingRight()) {
                        player.setMoveLeft(true);
                        player.setRecentKey('a');
                    }
                    if (key == 's' && !player.isMovingUp()) {
                        player.setMoveDown(true);
                        player.setRecentKey('s');
                    }
                    if (key == 'd' && !player.isMovingLeft()) {
                        player.setMoveRight(true);
                        player.setRecentKey('d');
                    }
        
                    if (key == ' ') {
                        player.setAttack(true);
                    }
                }
                break;
            // Game Over
            case 5:
                if (key == ' ') {
                    int i = scores.size() - 1;
                    while (i >= 0 && (score > scores.get(i))) {
                        i--;
                    }
                    names.add(i + 1, name);
                    scores.add(i + 1, score);

                    saveScores();
                    gameState = 6;
                }
                else if (key != CODED && name.length() > 0 && key == BACKSPACE) {
                    name = name.substring(0, name.length() - 1);
                }
                else if (key != CODED && name.length() < 3 && key != BACKSPACE) {
                    name = (name + key).toUpperCase();
                }
                break;
            // Score Board
            case 6:
                if (key == ' ') {
                    gameState = 0;
                }
                break;
            // Credits Menu
            case 7:
                if (key == ' ') {
                    gameState = 0;
                }
                break;
        }
    }

    public void keyReleased() {
        switch (gameState) {
            case 4:
                if (!player.isHit()) {
                    if (key == 'w' && !player.isMovingDown()) {
                        player.setMoveUp(false);
                    }
                    if (key == 'a' && !player.isMovingRight()) {
                        player.setMoveLeft(false);
                    }
                    if (key == 's' && !player.isMovingUp()) {
                        player.setMoveDown(false);
                    }
                    if (key == 'd' && !player.isMovingLeft()) {
                        player.setMoveRight(false);
                    }

                    if (key == ' ') {
                        player.setAttack(false);
                    }
                }
                break;
        }
    }
    
    /**
     * Save scores to a <code>csv</code> file
     */
    private void saveScores() {
        try {
            PrintWriter writer = new PrintWriter(new File(scoreFile));
            String line = "";
            for (int i = 0; i < scores.size(); i++) {
                line = names.get(i) + "," + scores.get(i);
                writer.println(line);
            }
            writer.close();
        }
        catch (Exception e) {
            System.out.println("Unable to save scores.");
        }
    }

    /* Game creation functions */
    private void createGame() {
        // Create Room and Player
        room = new Room(this, 0, 64, ROWS, COLS);
        player = new Player(this, COLS * 16, (ROWS + 3) * 16, room, characterFiles[charSelectIndex], weaponFiles[weaponSelectIndex]);

        // Create Enemies
        enemiesSpawned = 10;
        enemies = new ArrayList<Enemy>();
        createEnemies();

        // Set up 3 Lives
        lives = new ArrayList<Heart>();
        for (int i = 0; i < 3; i++) {
            addHeart();
        }

        // Set up game variables
        score = 0;
        roomsCleared = 0;
        enemiesKilled = 0;
    }

    private void createEnemies() {
        for (int i = 0; i < enemiesSpawned; i++) {
            double xPos = Math.random() * 32 * 6 + 64;
            double yPos = Math.random() * 32 * 4 + 128;

            if (Math.random() < .5) {
                xPos += 32 * 9;
            }

            if (Math.random() < .5) {
                yPos += 32 * 7;
            }

            int rand = (int)(Math.random() * 3);

            switch (rand) {
                case 0:
                    enemies.add(new BigZombie(this, xPos, yPos, room));
                    break;
                case 1:
                    enemies.add(new Swampy(this, xPos, yPos, room));
                    break;
                case 2:
                    enemies.add(new Goblin(this, xPos, yPos, room));
                    break;
            }
        }
    }

    private void createDungeon() {
        createEnemies();
        room.closeDoors();
        room.createFloor();
        roomsCleared++;
        
        if (roomsCleared % 10 == 0) {
            if (enemiesSpawned < 30) {
                enemiesSpawned++;
            }

            if (lives.size() < 18) {
                addHeart();
            }
        }
    }

    /* Change the amount of lives */
    private void increaseLives() {
        for (int i = 0; i < lives.size(); i++) {
            if (lives.get(i).getState() != 2) {
                lives.get(i).increaseState();
                player.increaseHealth();
                break;
            }
        }
    }

    private void decreaseLives() {
        for (int i = lives.size() - 1; i >= 0; i--) {
            if (lives.get(i).getState() != 0) {
                lives.get(i).decreaseState();
                player.decreaseHealth();
                break;
            }
        }
    }

    private void addHeart() {
        int xPos = 32 + 16 * (lives.size() / 2);
        int yPos = 32 + 16 * (lives.size() % 2);
        lives.add(new Heart(this, xPos, yPos));
        for (int i = 0; i < 2; i++) {
            increaseLives();
        }
    }

    /* Calcualte the distance between player and tile */
    private double playerTileDistance(Player p, Tile t) {
        double xDistance = Math.pow(p.getX() - t.getX(), 2);
        double yDistance = Math.pow(p.getY() - t.getY(), 2);

        return Math.sqrt(xDistance + yDistance);
    }

    /* Different Game States */
    private void menuBackground() {
        background(0xFF483B3A);
        fill(0);
        rect(32, 32, width - 64, height - 64);
        fill(0xFFFFFFFF);
    }

    // State 0
    private void startMenu() {
        menuBackground();
        textAlign(CENTER, CENTER);
        textSize(32);
        text("Dungeons,\nDungeons and\nMore Dungeons", width / 2, height / 6 + 24);

        // Menu options
        textSize(24);
        optionTexts[0] = "Start";
        optionTexts[1] = "Settings";
        optionTexts[2] = "Credits";

        optionColors[0] = 0xFFFFFFFF;
        optionColors[1] = 0xFFFFFFFF;
        optionColors[2] = 0xFFFFFFFF;

        // Change color and String depending on selectIndex
        optionTexts[selectIndex] = "> " + optionTexts[selectIndex] + " <";
        optionColors[selectIndex] = 0xFF918FFA;

        for (int i = 0; i < 3; i++) {
            fill(optionColors[i]);
            text(optionTexts[i], width / 2, height / 2 + 48 * (i - 1));
        }

        // Directions
        textSize(16);
        fill(0xFFFFFFFF);
        text("Press W or S to Scroll", width / 2, 3 * height / 4);
        text("Press SPACE to Select", width / 2, 3 * height / 4 + 24);
    }
    
    // State 1
    private void settingsMenu() {
        menuBackground();
        textAlign(CENTER, CENTER);
        textSize(48);
        text("Settings", width / 2, height / 8 + 16);

        textSize(24);
        textAlign(RIGHT, CENTER);
        int xShift = 128;
        text("Show Hit Boxes:", width / 2 + xShift, height / 2);

        textAlign(LEFT, CENTER);
        if (HitBox.canShowHitBox()) {
            fill(0xFFBFD5A2);
        }
        else {
            fill(0xFFDD4A68);
        }
        text("" + HitBox.canShowHitBox(), width / 2 + xShift, height / 2);

        textSize(16);
        fill(0xFFFFFFFF);
        textAlign(CENTER, CENTER);
        text("Press SPACE to Toggle Settings", width / 2, 3 * height / 4);
        text("Press BACKSPACE to Return to Menu", width / 2, 3 * height / 4 + 24);
    }

    // State 2
    private void characterSelectionMenu() {
        menuBackground();
        textAlign(CENTER, CENTER);
        textSize(24);
        text("Select Character", width / 2, height / 8 + 16);

        for (int i = 0; i < 8; i++) {
            int length = 128;
            int xPos = width / 2 + (length + 8) * (i/ 2) - (2 * length + 12);
            int yPos = height / 2 + (length + 8) * (i % 2) - (length);
            if (i == selectIndex) {
                fill(0xFFB6CBCF);
            }
            else {
                fill(0xFF111111);
            }
            rect(xPos, yPos, length, length);
            imageMode(CENTER);

            xPos += length / 2;
            yPos += length / 2 - 16;
            if (i == selectIndex) {
                image(characterImages[i][frameNum / 5], xPos, yPos);
                frameNum = (frameNum + 1) % 20;
            }
            else {
                image(characterImages[i][0], xPos, yPos);
            }

            textSize(16);
            fill(0xFFFFFFFF);
            text("Press WASD to Change Selection", width / 2, 3 * height / 4 + 32);
            text("Press SPACE to Select", width / 2, 3 * height / 4 + 56);
        }
    }

    // State 3
    private void weaponSelectionMenu() {
        menuBackground();
        textAlign(CENTER, CENTER);
        textSize(24);
        text("Select Weapon", width / 2, height / 8 + 16);

        for (int i = 0; i < 8; i++) {
            int length = 128;
            int xPos = width / 2 + (length + 8) * (i/ 2) - (2 * length + 12);
            int yPos = height / 2 + (length + 8) * (i % 2) - (length);
            if (i == selectIndex) {
                fill(0xFFB6CBCF);
            }
            else {
                fill(0xFF111111);
            }
            rect(xPos, yPos, length, length);
            imageMode(CENTER);

            xPos += length / 2;
            yPos += length / 2;
            image(weaponImages[i], xPos, yPos);
        }

        textSize(16);
        fill(0xFFFFFFFF);
        text("Press WASD to Change Selection", width / 2, 3 * height / 4 + 32);
        text("Press SPACE to Select", width / 2, 3 * height / 4 + 56);
    }

    // State 4
    private void playGame() {
        // HUD
        background(0);
        textSize(16);

        textAlign(RIGHT, CENTER);
        text("Score:" + nf(score, 6), width - 16, 16);
        text("Room:" + nf(roomsCleared + 1, 3), width - 16, 48);

        textAlign(CENTER, CENTER);
        text("-LIVES-", 16 * 6, 16);
        for (Heart h : lives) {
            h.show();
        }
        
        // Show Room and Player
        room.show();
        player.show();

        // Determine if Weapon and Enemy HitBox overlaps
        if (player.isAttacking()) {
            HitBox wBox = player.getWeapon().getHitBox();
            for (int i = enemies.size() - 1; i >= 0; i--) {
                Enemy e = enemies.get(i);

                if (wBox.isOverlapping(e.getHitBox())) {
                    if (!e.isHit()) {
                        e.decreaseHealth();
                        e.setHit(true, player);
                    }

                    if (e.getHealth() == 0) {
                        enemies.remove(i);
                        enemiesKilled++;
                        score += e.getScore();
                        if (enemiesKilled % 4 == 0) {
                            increaseLives();
                        }
                    }
                }
            }

            if (enemies.size() == 0 && room.doorsClosed()) {
                room.openDoors();
            }
        }

        // Show each enemy, determine if HitBox overlaps
        for (Enemy e : enemies) {
            e.show();
            if (e.getHitBox().isOverlapping(player.getHitBox())) {
                if (!player.isHit()) {
                    decreaseLives();
                    player.setHit(true, e);
                }
            }

            if (e.isHit()) {
                e.hit();
            }
            else {
                e.move();
            }
        }

        if (player.isHit()) {
            player.hit();
        }
        else {
            player.move();
        }

        // Determine if player is nearby open door
        if (!room.doorsClosed()) {
            if (playerTileDistance(player, room.getTile(0, COLS / 2)) < 20 && player.isMovingUp()) {
                createDungeon();
            }
            else if (playerTileDistance(player, room.getTile(ROWS / 2, 0)) < 40 && player.isMovingLeft()) {
                createDungeon();
            }
            else if (playerTileDistance(player, room.getTile(ROWS / 2, COLS - 1)) < 40 && player.isMovingRight()) {
                createDungeon();
            }
            else if (playerTileDistance(player, room.getTile(ROWS - 1, COLS / 2)) < 60 && player.isMovingDown()) {
                createDungeon();
            }
        }

        if (player.getHealth() <= 0 && gameState == 4) {
            gameState = 5;
        }
    }

    // State 5
    private void gameOver() {
        menuBackground();
        textAlign(CENTER, CENTER);
        textSize(48);
        fill(0xFFDA4E37);
        text("Game Over", width / 2, height / 4);

        textSize(24);
        fill(0xFFFFFFFF);
        text("Score: " + nf(score, 6), width / 2, height / 2 - 24 - 16);
        text("Rooms Cleared:" + roomsCleared, width / 2, height / 2 + 24 - 16);

        int xShift = 16 * 6;
        textAlign(RIGHT, CENTER);
        text("Enter Name:", width / 2 + xShift, height / 2 + 96 - 16);
        textAlign(LEFT, CENTER);
        text(name, width / 2 + xShift, height / 2 + 96 - 16);

        textSize(16);
        textAlign(CENTER, CENTER);
        text("Press SPACE to Enter Name", width / 2, 3 * height / 4 + 24);
    }

    // State 6
    private void scoreBoard() {
        menuBackground();
        textAlign(CENTER, CENTER);
        textSize(48);
        fill(0xFF52894C);
        text("Top Scores", width / 2, height / 4);

        textSize(24);
        fill(0xFFFFFFFF);
        for (int i = 0; i < 5; i++) {
            String line = names.get(i) + " " + nf(scores.get(i), 6);
            textAlign(LEFT, CENTER);
            text(line, width / 2 - 16 * 7, height / 2 - 16 * 4 + 48 * i);
        }

        textAlign(CENTER, CENTER);
        textSize(16);
        text("Press SPACE to Return to Menu", width / 2, 3 * height / 4 + 48);
    }

    // State 7
    private void creditsMenu() {
        menuBackground();
        textAlign(CENTER, CENTER);
        textSize(48);
        text("Credits", width / 2, height / 4);

        textSize(16);
        textAlign(LEFT, CENTER);
        int xShift = width / 10;
        text("Game Assets from 16x16", xShift, height / 2 - 48);
        text("    DungeonTileSet II by 0x72", xShift, height / 2 - 24);
        text("Sound Effects by artisticdude", xShift, height / 2 + 12);
        text("Font by The Press Start 2P", xShift, height / 2 + 48);
        text("    Project Authors", xShift, height / 2 + 72);
        text("Game by Cedric David", xShift, height / 2 + 108);

        textAlign(CENTER, CENTER);
        textSize(16);
        text("Press SPACE to Return to Menu", width / 2, 3 * height / 4 + 48);
    }

}
