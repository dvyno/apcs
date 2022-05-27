import processing.core.PApplet;
import processing.core.PImage;
import processing.sound.*;

public class Player extends Entity {
    // Steps
    private SoundFile step1, step2;
    private int stepTime, stepWait;
    private boolean step1Played;

    // Weapon
    private Weapon weapon;

    // Attacking Variables
    private boolean isAttacking;
    private char recentKey;

    // Hit Animation
    private PImage hitFrame;

    // Swing Sounds
    private SoundFile[] swingSounds;
    private int swingNum;

    public Player(PApplet applet, double x, double y, Room room, String charFile, String weaponFile) {
        super(applet, x, y, room, charFile);

        // Create Frames
        frameAmount = 4;
        frameCycle = 20;
        currentFrame = 0;
        createFrames();

        speed = 3.5;
        moveUp = moveDown = moveLeft = moveRight = false;
        faceLeft = false;

        // Steps
        step1 = new SoundFile(applet, "chainmail/chainmail1.wav");
        step2 = new SoundFile(applet, "chainmail/chainmail2.wav");

        stepTime = applet.millis();
        stepWait = 500;
        step1Played = false;

        // Weapon
        weapon = new Weapon(applet, this, weaponFile);

        // HitBox (30, 40)
        // hitbox = new HitBox(applet, x - 17, y - 13, x + 17, y + 28);
        leftShift = -17;
        rightShift = 17;
        bottomShift = -13;
        topShift = 28;
        
        // Attack
        isAttacking = false;
        recentKey = ' ';

        // health
        health = 0;

        // Hit Frame
        hitFrame = applet.loadImage("assets/" + file + "_hit_anim_f0.png");

        // Swings
        swingSounds = new SoundFile[3];
        for (int i = 1; i < 4; i++) {
            swingSounds[i - 1] = new SoundFile(applet, "battle/swing" + i + ".wav");
        }
        swingNum = 0;
    }

    @Override
    public void show() {
        if (isHit) {
            applet.imageMode(PApplet.CENTER);
            int scaleX = 1;
            if (isFacingLeft()) {
                scaleX *= -1;
            }

            applet.pushMatrix();
            applet.translate((float)x, (float)y);
            applet.scale(scaleX, 1);
            applet.image(hitFrame, 0, 0);
            applet.popMatrix();
        }
        else if (moveUp || moveDown || moveLeft || moveRight) {
            playStep();
            showFrame(runFrames);
        }
        else {
            showFrame(idleFrames);
        }

        if (isAttacking) {
            weapon.attack();
        }
        else {
            weapon.show();
        }

        hitbox.show();
    }

    @Override
    public void setHit(boolean isHit, Entity e) {
        super.setHit(isHit, e);
        prevDown = false;
        prevUp = false;
        prevLeft = false;
        prevRight = false;
        isAttacking = false;
    }

    private void playStep() {
        if (applet.millis() - stepTime >= stepWait) {
            if (step1Played) {
                step2.play(1, (float)0.25);
            }
            else {
                step1.play(1, (float)0.25);
            }
            step1Played = !step1Played;
            stepTime = applet.millis();
        }
    }

    public void setAttack(boolean isAttacking) {
        if (isAttacking && !swingSounds[(swingNum + 2) % 3].isPlaying() && this.isAttacking != isAttacking) {
            swingSounds[swingNum].play();
            swingNum = (swingNum + 1) % 3;
        }

        this.isAttacking = isAttacking;
    }

    public void setRecentKey(char recentKey) {
        this.recentKey = recentKey;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public char getRecentKey() {
        return recentKey;
    }
    
    public Weapon getWeapon() {
        return weapon;
    }
}
