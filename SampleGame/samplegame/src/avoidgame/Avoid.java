package avoidgame;

import java.util.prefs.Preferences;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
 
public class Avoid extends BasicGame implements KeyListener {
 
    // Window Size============================================
    // window width
    public static final int SCREEN_W = 1280;
    // window height
    public static final int SCREEN_H = 768;
 
    // Images=================================================
    private Image playerImage, enemyImage, spacebackground;
 
    // Game Object============================================
    private Game game;
 
    // Player=================================================
    private Plane player;
 
    // player position
    private float playerX;
    private float playerY;
 
    // player sprite width and height
    private int playerWidth;
    private int playerHeight;
 
    // Enemy==================================================
    private int enemyWidth;
    private int enemyHeight;
 
    // Level==================================================
    private int level = 1;
    // initial level up threshold
    private int threshold = 200;
 
    // Game States============================================
    boolean gameOver = true, menu = true, inGame, resuming;
 
    // Draws bounding boxes if true=========================
    boolean diagnostics;
 
    // Mouse Control (for testing)=============================
    boolean mouse;
 
    // Lasers=================================================
    // laser object
    Laser laser = new Laser();
    // laser reference time for depletion
    private long curLaserTime;
    // width of the laser in pixels
    public static final int LASER_W = 100;
    // whether or not the laser is firing
    boolean laserFiring;
 
    // Pausing================================================
    // reference time for pausing
    private long curPauseTime;
    // whether or not the game is paused
    private boolean paused;
    // whether or not there is a count down
    private boolean countdown;
 
    // High Score=============================================
    // Preferences to store high score
    private Preferences prefs;
    // Key for high score for preferences
    private String highScoreKey = "highscore";
 
    // Strings=================================================
    // Font to use
    UnicodeFont font;
 
    // Static Text
    // Title for main menu
    private final static String title = "Avoid Game";
    // Paused message for pause screen
    private final static String pausedMessage = "<Space> to Resume. <Enter> for Menu.";
    // Start option for main menu
    private final static String startFromMenu = "<Enter> to Start";
    // Widths of the static strings to be able to center on the screen
    private int titleWidth, pausedMessageWidth, startFromMenuWidth;
 
    // Dynamic Text
    private String inGameOptions, countdownTime, gameOverMessage,
            highScoreMessage;
    // Widths of the dynamic strings to be able to center on the screen
    private int inGameOptionsWidth, countdownTimeWidth, gameOverMessageWidth,
            highScoreMessageWidth;
 
    public Avoid(String title, boolean leap, boolean diagnose) {
        super(title);
        // Initialize preferences for high score
        prefs = Preferences.userRoot().node(this.getClass().getName());
        mouse = (leap) ? false : true;
        diagnostics = diagnose;
 
    }
 
    @Override
    public void init(GameContainer gc) throws SlickException {
        // Load font (used to center the text)
        font = new UnicodeFont("fonts/font.ttf", 44, false, false);
        font.addAsciiGlyphs();
        font.getEffects().add(new ColorEffect());
        font.loadGlyphs();
 
        // Load the sprite images
        playerImage = new Image("res/jet.png");
        spacebackground = new Image("res/spacebackground.png");
        enemyImage = new Image("res/enemy.png");
 
        // Set the dimensions of the internal representation of planes
        enemyWidth = enemyImage.getWidth();
        enemyHeight = enemyImage.getHeight();
        playerWidth = playerImage.getWidth();
        playerHeight = playerImage.getHeight();
 
        // Player
        playerX = SCREEN_W / 2 - playerWidth / 2;
        playerY = SCREEN_H - 1.5f * playerHeight;
        // create a new player plane object
        player = new Plane(playerX, playerY, playerWidth, playerHeight, -1,
                false);
 
        // Game object for enemy planes
        game = new Game(level, enemyWidth, enemyHeight);
 
        // Initialize text
        highScoreMessage = "High Score: " + prefs.getInt(highScoreKey, 0);
        inGameOptions = " Score: 0 <Space> for Pause";
        countdownTime = "3";
 
        // Update text width
        highScoreMessageWidth = font.getWidth(highScoreMessage);
        inGameOptionsWidth = font.getWidth(inGameOptions);
        pausedMessageWidth = font.getWidth(pausedMessage);
        startFromMenuWidth = font.getWidth(startFromMenu);
        countdownTimeWidth = font.getWidth(countdownTime);
        titleWidth = font.getWidth(title);
 
    }
 
    // This moves the player plane
    public void movePlayer(float x, float y) {
        // makes sure you dont move it while the game
        // is in a pause state
        if (!paused && !resuming) {
            // makes sure the player doesn't move past the game window
            // both horizontally and vertically
            if (x < 0) {
                x = 0;
            }
            if (x > SCREEN_W - playerWidth) {
                x = SCREEN_W - playerWidth;
            }
            if (y < 0) {
                y = 0;
            }
            if (y > SCREEN_H - playerHeight) {
                y = SCREEN_H - playerHeight;
            }
            // updates the x and y coordinates for the sprite
            playerX = x;
            playerY = y;
            // updates the player plane position in the internal game
            player.updatePlayer(playerX, playerY);
        }
    }
 
    // This shoots a laser
    public void shootLaser() {
        if (laser.canFire() && !laserFiring) {
            laserFiring = true;
            startLaserCountdown();
        }
    }
 
    // stops the laser from shooting
    public void stopShootLaser() {
        laserFiring = false;
    }
 
    // Levels up the game
    private void levelUp() {
        level++;
        // every level requires a higher score threshold score to advance
        threshold += level * 150;
        // after 9 planes, stop adding planes to make it possible to have
        // somewhere to go to dodge the planes if there is not laser
        if (level < 9) {
            game.addPlane();
        }
    }
 
    // resets the levels back to 1
    private void resetLevels() {
        level = 1;
        threshold = 200;
    }
 
    // creates a new game
    private void reset() {
        game = new Game(level, enemyWidth, enemyHeight);
        resetLevels();
        laserFiring = false;
        laser.resetEnergy();
        // reset the text to indicate 0 score
        // (used for width to center text)
        inGameOptions = "Score: 0 <Space> for Pause";
        inGameOptionsWidth = font.getWidth(inGameOptions);
    }
 
    // Resume/Start Game Countdown==========================
    // the countdown by setting a reference timepoint
    private void startCountdown() {
        // get the current time in milliseconds for reference use
        curPauseTime = System.currentTimeMillis() / 1000;
        // this tells the program to start counting
        countdown = true;
    }
 
    // checks the number of seconds left until game starts
    private void countDown() {
        // how many seconds left from 3 second countdown
        long timeRemaining = 3 - (System.currentTimeMillis() / 1000 - curPauseTime);
        // updates the countdown time string to render
        countdownTime = "" + timeRemaining;
        countdownTimeWidth = font.getWidth(countdownTime);
        // countdown is done
        if (timeRemaining < 1) {
            // starting over after a game over
            if (!resuming) {
                reset();
                gameOver = false;
            } else { // resuming from a pause
                resuming = false;
                gameOver = false;
            }
            // this tells the program to stop counting
            countdown = false;
        }
    }
 
    // Laser Countdown=======================================
    // starts the countdown by setting a reference time point
    private void startLaserCountdown() {
        // get the current time in milliseconds for reference use
        curLaserTime = System.currentTimeMillis();
    }
 
    // this manages the laser/every 10 ms depletes the laser by the set amount
    private void laserCount() {
        // the number of seconds since you last fired the laser
        long laserElapsedTime = System.currentTimeMillis() - curLaserTime;
        // laser is depleted, stop firing
        if (laser.isDepleted()) {
            laserFiring = false;
        }
        // every 10 milliseconds, the laser depletes by the set amount defined
        // in the Laser class
        else if (laserFiring && laserElapsedTime > 10) {
            // update the reference time to detect the next 10 ms
            curLaserTime = System.currentTimeMillis();
            // deplete the laser by the amount
            laser.deplete();
        }
    }
 
    private void renderPause(GameContainer gc, Graphics g)
            throws SlickException {
        // draw the background image
        spacebackground.draw(0, 0);
        // set the text color to white
        g.setColor(Color.white);
        // draw the pause screen text
        font.drawString(gc.getWidth() / 2 - pausedMessageWidth / 2,
                gc.getHeight() / 2, pausedMessage);
    }
 
    private void renderMenu(GameContainer gc, Graphics g) throws SlickException {
        // draw the background image
        spacebackground.draw(0, 0);
        // set the text color to white
        g.setColor(Color.white);
        // Draw the title, start option, and high score text
        font.drawString(SCREEN_W / 2 - titleWidth / 2, SCREEN_H / 2 - 100,
                title);
        font.drawString(SCREEN_W / 2 - startFromMenuWidth / 2, SCREEN_H / 2,
                startFromMenu);
        font.drawString(SCREEN_W / 2 - highScoreMessageWidth / 2, 480,
                highScoreMessage);
 
    }
 
    private void renderGame(GameContainer gc, Graphics g) throws SlickException {
        // draw the background
        spacebackground.draw(0, 0);
        // Draw the level number
        font.drawString(0, 0, "Level : " + level);
        // green for laser
        g.setColor(Color.green);
        playerImage.draw(playerX, playerY);
        // Diagnostics for Bounding Box
        if (diagnostics) {
            // Draw bounding boxes
            g.drawRect(player.body.left, player.body.bottom, player.body.width,
                    player.body.height);
            g.drawRect(player.wings.left, player.wings.bottom,
                    player.wings.width, player.wings.height);
        }
        // draw the laser if firing
        if (laserFiring) {
            g.fillRect(playerX, 0, LASER_W, SCREEN_H - (SCREEN_H - playerY));
 
        }
        // go through all the enemy planes
        for (Plane plane : game.planes()) {
            // draw the planes in their current positions
            enemyImage.draw(plane.x, plane.y);
            // Diagnostics for Bounding Box
            if (diagnostics) {
                // Draw bounding boxes
                g.drawRect(plane.body.left, plane.body.bottom,
                        plane.body.width, plane.body.height);
                g.drawRect(plane.wings.left, plane.wings.bottom,
                        plane.wings.width, plane.wings.height);
            }
        }
        // draw the countdown number
        if ((resuming || gameOver) && countdown) {
            font.drawString(SCREEN_W / 2 - countdownTimeWidth / 2,
                    SCREEN_H / 3, countdownTime);
        }
        // if there is enough energy to shoot laser, make the laser
        // energy bar green
        if (laser.canFire()) {
            g.setColor(Color.green);
        } else { // red for not enough energy
            g.setColor(Color.red);
        }
        // draw the energy bar for laser
        g.fillRect(0, SCREEN_H - 50, laser.getEnergy() * 3, 50);
        // game over message
        if (gameOver && gameOverMessage != null) {
            font.drawString(SCREEN_W / 2 - gameOverMessageWidth / 2,
                    SCREEN_H / 2, gameOverMessage);
        } else { // draw the current score and the option to pause
            font.drawString(SCREEN_W / 2 - inGameOptionsWidth / 2, 0,
                    inGameOptions);
        }
    }
 
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        // Draw paused screen
        if (paused) {
            renderPause(gc, g);
        }
        // Draw the game
        else if (inGame) {
            renderGame(gc, g);
        }
        // Draw the menu
        else if (menu) {
            renderMenu(gc, g);
        }
        // Limit to 60 fps
        Display.sync(60);
 
    }
 
    @Override
    public void keyPressed(int key, char c) {
        switch (key) {
        case Input.KEY_ENTER:
            // go from menu to start a new game
            if (menu) {
                reset();
                startCountdown();
                menu = false;
                inGame = true;
            }// leave from pause screen to menu
            else if (paused || (gameOver && !countdown)) {
                reset();
                inGame = false;
                gameOver = true;
                menu = true;
                paused = false;
            }
 
            break;
        case Input.KEY_SPACE:
            // restart after game over
            if (inGame && gameOver && !countdown) {
                startCountdown();
            }
            // pause
            else if (inGame) {
                paused = true;
                inGame = false;
            }
            // unpause
            else if (paused) {
                menu = false;
                inGame = true;
                paused = false;
                resuming = true;
                startCountdown();
            }
 
            break;
 
        }
         
    }
 
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        // if you are in a game and you are resuming/starting a new game after
        // game over, count down to when the game start
        if (inGame && (gameOver || resuming) && countdown) {
            countDown();
        }
        if (!gameOver && inGame && !paused) {
            // manages the laser depletion
            if (laserFiring) {
                laserCount();
            } else { // regenerate the laser if laser not firing
                laser.regen();
            }
            // Mouse control
            if (mouse) {
                // Get the mouse input
                Input g = gc.getInput();
                // Get the x and y coordinates of the mouse
                int x = g.getMouseX();
                int y = g.getMouseY();
                // move the player plane
                movePlayer(x, y);
                // Triggers the laser
                if (g.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    shootLaser();
                } else { // Stops the laser if mouse not pressed
                    stopShootLaser();
                }
 
            }
 
            // Move enemy planes (the parameters wont do anything if
            // no lasers are firing (laserFiring == false)
            game.movePlanes(laserFiring, playerX, LASER_W, SCREEN_H
                    - (SCREEN_H - playerY));
 
            // Update the score text and its width
            inGameOptions = "Score: " + game.getScore() + "<Space> for Pause";
            inGameOptionsWidth = font.getWidth(inGameOptions);
            // Check for level up
            if (game.getScore() > threshold) {
                levelUp();
            }
            // Check for loss
            if (game.checkLose(player)) {
                // set game over to be true to stop the game
                gameOver = true;
                // If you have broken a high score, update it (stored
                // persistently)
                int curHighScore = prefs.getInt(highScoreKey, 0);
                int curGameScore = game.getScore();
                if (curGameScore > curHighScore) {
                    prefs.putInt(highScoreKey, curGameScore);
                    highScoreMessage = "High Score: "
                            + prefs.getInt(highScoreKey, 0);
                    highScoreMessageWidth = font.getWidth(highScoreMessage);
                }
                // Update the game over text and its width
                gameOverMessage = "Game Over. Score: " + game.getScore()
                        + ".<Space> to restart and <Enter> to menu";
                gameOverMessageWidth = font.getWidth(gameOverMessage);
                
            }
        }
 
    }
 
    public static void main(String[] args) throws SlickException {
        // Creates a new game
        AppGameContainer app = new AppGameContainer(new Avoid("Avoid Game",
                false, false));
        // set the dimensions of the game
        app.setDisplayMode(1280, 768, false);
        // don't show the diagnostic FPS
        app.setShowFPS(false);
        // launch the game
        app.start();
    }
}