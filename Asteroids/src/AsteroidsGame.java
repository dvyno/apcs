import processing.core.PApplet;
import java.util.ArrayList;
import processing.sound.*;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

public class AsteroidsGame extends PApplet {
    /* Game Variables */
    private ArrayList<String> names;
    private ArrayList<Integer> scores;
    private String name;
    private int score, highScore;
    private boolean gameOver, gameRunning, scoreEntered;
    private String scoreFile;
    private int round;
    
    /* Background Music */
    private SoundFile beat1, beat2;
    private int beatTime, beatWait;
    private boolean beat1Played;

    /* Lives */
    private ArrayList<Spaceship> lives;
    private int livesCount;
    private int lifeSoundCount;
    private boolean playSound;
    private SoundFile extraLife;

    /* Player Spaceship */
    private Spaceship ship;
    private ArrayList<Bullet> shipBullets;
    private SoundFile fireFile, thrust;
    private int fireTime, fireWait;

    /* Stars */
    private Star[] stars;

    /* Asteroids */
    private ArrayList<Asteroid> asteroids;
    private int asteroidAmount;
    private int largeAsteroidCount;
    private SoundFile bangLarge, bangMedium, bangSmall;

    /* Saucer */
    private Saucer saucer;
    private ArrayList<Bullet> saucerBullets;
    private int saucerSpawnTime, saucerSpawnWait;
    private int saucerBulletTime, saucerBulletWait;
    private SoundFile saucerSmall, saucerBig;
    private boolean saucerSpawned;


    public static void main(String[] args) {
        PApplet.main("AsteroidsGame");
    }

    public void settings() {
        size(20 + 1024 + 20,50 + 768 + 50);
    }

    public void setup() {
        /* Initialize Game Variables */
        names = new ArrayList<>();
        scores = new ArrayList<>();
        name = "";
        score = 0;
        gameOver = false;
        gameRunning = false;
        scoreEntered = false;
        scoreFile = "src/resources/scores.csv";

        // Add scores to ArrayLists
        try {
            Scanner reader = new Scanner(new File(scoreFile));
            String line = "";
            String[] values;

            while(reader.hasNext()) {
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

        highScore = scores.get(0);

        /* Initialize Background Music */
        beat1 = new SoundFile(this, "beat1.wav");
        beat2 = new SoundFile(this, "beat2.wav");
        beatTime = millis();
        beatWait = 1000;
        beat1Played = false;

        /* Initialize Lives */
        lives = new ArrayList<>();
        livesCount = 3;
        setLives();
        lifeSoundCount = 10;
        playSound = false;
        extraLife = new SoundFile(this, "extraShip.wav");

        /* Initialize Player Spaceship */
        ship = new Spaceship(this);
        shipBullets = new ArrayList<>();
        fireFile = new SoundFile(this, "fire.wav");
        thrust = new SoundFile(this, "thrust.wav");
        fireTime = 0;
        fireWait = 200;

        /* Initialize Stars */
        stars = new Star[100];
        for (int i = 0; i < stars.length; i++) {
            float xPos = (float)(Math.random() * (width - 40)) + 20;
            float yPos = (float)(Math.random() * (height - 100)) + 50;
            stars[i] = new Star(this, xPos, yPos, (int)(Math.random() * 6) + 3);
        }

        /* Initialize Asteroids */
        asteroids = new ArrayList<>();
        asteroidAmount = 4;
        largeAsteroidCount = asteroidAmount + round;
        setAsteroids();
        bangLarge = new SoundFile(this, "bangLarge.wav");
        bangMedium = new SoundFile(this, "bangMedium.wav");
        bangSmall = new SoundFile(this, "bangSmall.wav");

        /* Initialize Saucer */
        saucer = new Saucer(this);
        saucerBullets = new ArrayList<>();
        saucerSpawnTime = millis();
        saucerSpawnWait = (int)(Math.random() * 11 + 10) * 1000;
        saucerBulletTime = millis();
        saucerBulletWait = 2000;
        saucerSmall = new SoundFile(this, "saucerSmall.wav");
        saucerBig = new SoundFile(this, "saucerBig.wav");
        saucerSpawned = false;
    }

    public void draw() {
        background(0);
        /* Menu Screen */
        if (!gameRunning && !gameOver) {
            textAlign(CENTER, CENTER);
            textSize(100);
            text("ASTEROIDS", width / 2, height / 2 - 50);
            textSize(20);
            text("Press Any Key To Start", width / 2, height / 2 + 50);
        }

        /* Game Screen */
        else if (gameRunning) {
            // Background Audio
            if (millis() - beatTime >= beatWait) {
                if (beat1Played) {
                    beat2.play(1, (float)0.75);
                }
                else {
                    beat1.play(1, (float)0.75);
                }
                beat1Played = !beat1Played;
                beatTime = millis();
            }

            // Stars
            for (Star s : stars) {
                s.show();
            }

            // Asteroid Collisions
            for (int i = asteroids.size() - 1; i >= 0; i--) {
                Asteroid a = asteroids.get(i);
                boolean alive = ship.isAlive();
    
                // Calculate distance between bullets and asteroid
                float[] bulletDist = new float[shipBullets.size()];
                for (int j = 0; j < bulletDist.length; j++) {
                    Bullet b = shipBullets.get(j);
                    bulletDist[j] = distance(a, b);
                }

                // Distance of asteroid to closest bullet (if it exists)
                int index = minIndex(bulletDist);
                if (index != -1) {
                    Bullet minBullet = shipBullets.get(index);

                    if (distance(a, minBullet) < a.getHitRadius()) {
                        destroyAsteroid(a, minBullet);
                        shipBullets.remove(index);
                        continue;
                    }
                }
    
                // Distance of asteroid relative to ship
                if (alive && distance(a, ship) < a.getHitRadius()) {
                    destroyAsteroid(a, ship);
                    ship.setAlive(false);
                    continue;
                }
    
                updateFloater(a);
            }

            if (playSound) {
                if (!extraLife.isPlaying()) {
                    extraLife.play(1, (float)0.75);
                    lifeSoundCount--;
                }

                if (lifeSoundCount == 0) {
                    playSound = false;
                    lifeSoundCount = 10;
                }
            }

            // Check if ship collided with saucer bullet
            if (ship.isAlive()) {
                for (int i = saucerBullets.size() - 1; i >= 0; i--) {
                    Bullet b = saucerBullets.get(i);
    
                    // Distance of saucer bullet relative to ship
                    if (distance(b, ship) < 11) {
                        ship.setAlive(false);
                        saucerBullets.remove(i);
                        break;
                    }
                }
            }

            // Check if saucer collided with ship bullet
            if (saucer.isAlive()) {
                for (int i = shipBullets.size() - 1; i >= 0; i--) {
                    Bullet b = shipBullets.get(i);

                    // Distance of ship bullet relative to saucer
                    if (distance(b, saucer) < saucer.getHitRadius()) {
                        destroySaucer();
                        shipBullets.remove(i);
                        break;
                    }
                }
            }
    
            // Check if ship collided with saucer
            if (ship.isAlive() && saucer.isAlive()) {
                // Distance of ship relative to saucer
                if (distance(ship, saucer) < saucer.getHitRadius()) {
                    destroySaucer();
                    ship.setAlive(false);
                }
            }

            // Spawn Saucer
            if (!saucerSpawned && millis() - saucerSpawnTime >= saucerSpawnWait) {
                saucer = new Saucer(this);
                saucer.setAlive(true);
                saucerSpawned = true;
            }

            // Update ship and ship bullets
            if (ship.isAlive()) {
                updateFloater(ship);

                if (ship.isAccelerating()) {
                    ship.accelerate(0.05);
                    if (!thrust.isPlaying()) {
                        thrust.play(1, (float)0.75);
                    }
                }
        
                if (ship.isTurningLeft()) {
                    ship.turn(-ship.getTurnSpeed());
                }
        
                if (ship.isTurningRight()) {
                    ship.turn(ship.getTurnSpeed());
                }
    
                if (ship.isFiring() && shipBullets.size() < 8 && millis() - fireTime >= fireWait) {
                    fireFile.play(1, (float)0.75);
                    fireTime = millis();
                    shipBullets.add(new Bullet(this, ship));
                }
            }
            updateBullets(shipBullets);

            // Update saucer and saucer bullets
            if (saucer.isAlive()) {
                saucer.show();
                saucer.move(ship);

                if (saucer.getType() == 1 && !saucerBig.isPlaying()) {
                    saucerBig.play(1, (float)0.75);
                }
                else if (saucer.getType() == 0 && !saucerSmall.isPlaying()) {
                    saucerSmall.play(1, (float)0.75);
                }
                
                if (millis() - saucerBulletTime >= saucerBulletWait) {
                    if (ship.isAlive()) {
                        saucerBullets.add(new Bullet(this, ship, saucer));
                    }
                    saucerBulletTime = millis();
                }
            }
            updateBullets(saucerBullets);
    
            if (asteroids.size() == 0) {
                round++;
                setAsteroids();
            }
        }

        /* Game Over Screen */
        else {
            textAlign(CENTER, CENTER);
            textSize(100);
            text("GAME OVER", width / 2, height / 6);
            if (!scoreEntered) {
                textSize(50);
                textAlign(RIGHT, CENTER);
                text("Your Score: ", width / 2 - 30, height / 2 - 50);
                text("Enter Name: ", width / 2 - 30, height / 2 + 50);
                textAlign(LEFT, CENTER);
                text(score, width / 2 + 30, height / 2 - 50);
                text(name, width / 2 + 30, height / 2 + 50);
            }
            else {
                textSize(40);
                for (int i = 0; i < 5; i++) {
                    textAlign(RIGHT, CENTER);
                    text(names.get(i), width / 2 - 20, height / 2 + 50 * i - 100);
                    textAlign(LEFT, CENTER);
                    text(scores.get(i), width / 2 + 20, height / 2 + 50 * i - 100);
                }

                textSize(20);
                textAlign(LEFT,CENTER);
                text("Press 'r' to Reset", width / 2 - 100, 3 * height / 4 - 20);
                text("Press 'q' to Go Back to Menu", width / 2 - 100, 3 * height / 4 + 20);
            }
        }

        drawHUD();
    }

    public void keyPressed() {
        /* Home Screen */
        if (!gameRunning && !gameOver) {
            gameRunning = true;
            saucerSpawnTime = millis();
        }

        /* Game Screen */
        else if (gameRunning) {
            if(ship.isAlive()) {
                // Accelerate
                if (key == 'w') {
                    ship.setAccelerating(true);
                }

                // Turn Left
                if (key == 'a' && !ship.isTurningRight()) {
                    ship.setTurningLeft(true);
                }

                // Turn Right
                if (key == 'd' && !ship.isTurningLeft()) {
                    ship.setTurningRight(true);
                }

                // Fire
                if (key == ' ') {
                    ship.setFiring(true);
                }

                // Hyperspace
                if (key == CODED && keyCode == SHIFT) {
                    ship.setAccelerating(false);
                    ship.setTurningLeft(false);
                    ship.setTurningRight(false);
                    ship.hyperspace();
                    ship.setJumpedSpace(true);
                }
            }

            // Reset Ship
            if (key == 'r' && !ship.isAlive()) {
                ship = new Spaceship(this);
                shipBullets.clear();
                if (lives.size() != 0) {
                    if (lives.size() == 2) {
                        beatWait = 750;
                    }
                    else if (lives.size() == 1) {
                        beatWait = 500;
                    }
                    lives.remove(lives.size() - 1);
                }
                else {
                    gameRunning = false;
                    gameOver = true;
                }
            }
        }

        /* Game Over */
        else {
            // Enter Score
            if (!scoreEntered) {
                if (key == ENTER) {
                    int i = scores.size() - 1;
                    while (i >= 0 && (score > scores.get(i))) {
                        i--;
                    }
                    names.add(i + 1, name);
                    scores.add(i + 1, score);
                    scoreEntered = true;

                    saveScores();
                }
                else if (key != CODED && name.length() > 0 && key == BACKSPACE) {
                    name = name.substring(0, name.length() - 1);
                }
                else if (key != CODED && name.length() < 3 && key != BACKSPACE) {
                    name = (name + key).toUpperCase();
                }
            }

            // Reset Game
            else {
                if (key == 'r') {
                    gameRunning = true;
                    gameOver = false;
                    setUpGame();
                }
                
                if (key == 'q') {
                    gameRunning = false;
                    gameOver = false;
                    setUpGame();
                }
    
            }
        }
    }

    public void keyReleased() {
        if (gameRunning) {
            // Stop Accelerating
            if (key == 'w') {
                ship.setAccelerating(false);
            }
    
            // Stop Turning Left
            if (key == 'a') {
                ship.setTurningLeft(false);
            }
    
            // Stop Turning Right
            if (key == 'd') {
                ship.setTurningRight(false);
            }

            // Stop Firing
            if (key == ' ') {
                ship.setFiring(false);
            }

            // Stop Hyperspace
            if (key == CODED && keyCode == SHIFT) {
                ship.setJumpedSpace(false);
            }
        }
    }

    /**
     * Draw border and text surrounding main game
     */
    public void drawHUD() {
        fill(102);
        stroke(102);
        rect(0, 0, width, 50);
        rect(width - 20, 0, width, height);
        rect(0, height - 50, width, height);
        rect(0, 0, 20, height);

        fill(204);
        textAlign(CENTER, CENTER);
        textSize(15);
        text("By: Cedric David", width / 2, height - 25);
        textAlign(RIGHT, CENTER);
        text("Movement: WASD | Fire: Space", width - 20, height - 36);
        text("Hyperspace: SHIFT | Reset: R", width - 20, height - 14);

        if (gameRunning) {
            for (Spaceship life : lives) {
                life.show();
            }
            textSize(20);
            textAlign(LEFT, CENTER);
            text("High Score: " + highScore, 20, 25);
            textAlign(RIGHT, CENTER);
            text(score, width - 20, 25);
        }
    }

    /**
     * Returns the index of the minimum value of a <code>float</code> array
     * @param nums
     *      a <code>float</code> array
     * @return
     *      <code>-1</code> if array is length 0, otherwise the index of the
     *      minimum value
     */
    public int minIndex(float[] nums) {
        if (nums.length == 0) {
            return -1;
        }

        int minIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[minIndex] > nums[i]) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    /**
     * Return the distance between two <code>Floater</code> objects
     * @param obj1
     *      a <code>Floater</code> object
     * @param obj2
     *      a <code>Floater</code> object
     * @return
     *      distance
     */
    public float distance(Floater obj1, Floater obj2) {
        float obj1X = (float)obj1.getX();
        float obj1Y = (float)obj1.getY();

        float obj2X = (float)obj2.getX();
        float obj2Y = (float)obj2.getY();

        return dist(obj1X, obj1Y, obj2X, obj2Y);
    }
    
    /**
     * Show and move <code>Floater</code> object.
     * @param obj a <code>Floater</code> object
     */
    public void updateFloater(Floater obj) {
        obj.show();
        obj.move();
    }

    /**
     * Determine if bullet has traveled longer than 1 second. If so, remove it.
     * Otherwise, show and move it.
     * @param bullets
     *      an <code>ArrayList</code> of bullets
     */
    public void updateBullets(ArrayList<Bullet> bullets) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            if (millis() >= b.endTime()) {
                bullets.remove(i);
            }
            else {
                updateFloater(b);
            }
        }
    }
    
    /**
     * Destroy <code>Asteroid</code> object and add its point value to
     * <code>score</code>.
     * @param a
     *      an <code>Asteroid</code> object
     * @param obj
     *      a <code> Floater</code> object, usually a <code>Bullet</code> or
     *      <code>Spaceship</code>
     */
    public void destroyAsteroid(Asteroid a, Floater obj) {
        int size = a.getSize();
        if (size == 4) {
            largeAsteroidCount--;
            asteroids.add(new Asteroid(this, a, obj));
            asteroids.add(new Asteroid(this, a, obj));
            bangLarge.play(1, (float)0.75);
        }
        else if (size == 2) {
            asteroids.add(new Asteroid(this, a, obj));
            asteroids.add(new Asteroid(this, a, obj));
            bangMedium.play(1, (float)0.75);
        }
        else {
            bangSmall.play(1, (float)0.75);
        }
        increaseScore(a);
        asteroids.remove(a);
    }

    /**
     * Reset asteroids in <code>asteroids</code>
     */
    public void setAsteroids() {
        for (int i = 0; i < asteroidAmount + round; i++) {
            asteroids.add(new Asteroid(this));
        }
        largeAsteroidCount = asteroidAmount + round;
    }

    /**
     * Reset lives to <code>livesCount</code>
     */
    public void setLives() {
        for (int i = 1; i <= livesCount; i++) {
            lives.add(new Spaceship(this, 30 * i, height - 20));
        }
    }

    /**
     * Reset class variables
     */
    public void setUpGame() {
        /* Game Variables */
        name = "";
        score = 0;
        scoreEntered = false;
        highScore = scores.get(0);
        round = 0;

        /* Background Music */
        beat1Played = false;
        beatWait = 1000;

        /* Lives */
        setLives();
        
        /* Player Spaceship */
        shipBullets.clear();

        /* Stars */
        for (int i = 0; i < stars.length; i++) {
            float xPos = (float)(Math.random() * (width - 40)) + 20;
            float yPos = (float)(Math.random() * (height - 100)) + 50;
            stars[i] = new Star(this, xPos, yPos, (int)(Math.random() * 6) + 3);
        }

        /* Asteroids */
        asteroids.clear();
        largeAsteroidCount = asteroidAmount + round;
        setAsteroids();

        /* Saucer */
        saucer = new Saucer(this);
        saucerSpawned = false;
        saucerSpawnTime = millis();
        saucerSpawnWait = (int)(Math.random() * 11 + 10) * 1000;
        saucerBullets.clear();
    }

    /**
     * Increase <code>score</code> by a <code>Floater</code> object's point
     * value. Every <code>10,000</code> points, an extra life is added.
     * @param obj
     *      a <code>Floater</code> object
     */
    public void increaseScore(Floater obj) {
        int increase = obj.getPointValue();
        if ((score + increase) % 10000 < score % 10000) {
            playSound = true;
            lives.add(new Spaceship(this, 30 * (lives.size() + 1), height - 20));
        }
        score += increase;
    }

    /**
     * Destroy <code>Saucer</code>, increase <code>score</code>, and set new
     * <code>saucerSpawnTime</code> and <code>saucerSpawnWait</code>
     */
    public void destroySaucer() {
        increaseScore(saucer);
        saucer.setAlive(false);
        saucerSpawnTime = millis();
        saucerSpawnWait = (int)(Math.random() * 11 + 10) * 1000;
        saucerSpawned = false;
    }

    /**
     * Save scores to a <code>csv</code> file
     */
    public void saveScores() {
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
}
