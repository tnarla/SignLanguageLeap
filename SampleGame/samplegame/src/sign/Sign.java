package sign;

//import java.io.IOException;
import java.util.Scanner;
import java.util.prefs.Preferences;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class Sign extends BasicGame implements KeyListener {


	// Mouse Control (for testing)=============================
    boolean mouse;
    
	
	 int score;
	 
	

    // High Score=============================================
    // Preferences to store high score
    private Preferences prefs;
    
    public Sign(String title, boolean leap, boolean diagnose) {
        super(title);
        // Initialize preferences for high score
        prefs = Preferences.userRoot().node(this.getClass().getName());
        mouse = (leap) ? false : true;
    }
    
	

	// Game States============================================
	boolean gameOver = true, menu = true, inGame = true, resuming;

	public static final int SCREEN_W = 800;
	public static final int SCREEN_H = 600;

	private Image background;

	

	private boolean countdown;

	// Static Text
	// Title for main menu
	private final static String title = "Signing Game";
	// Paused message for pause screen
	private final static String pausedMessage = "<Space> to Resume. <Enter> for Menu.";
	// Start option for main menu
	private final static String startFromMenu = "<Enter> to Start";
	// Widths of the static strings to be able to center on the screen
	private int titleWidth, pausedMessageWidth, startFromMenuWidth;

	// Dynamic Text
	private String countdownTime;

	private String signNumber;
	// Widths of the dynamic strings to be able to center on the screen
	private int countdownTimeWidth, signNumberWidth, scoreWidth;
	private long curPauseTime;

	// Strings=================================================
	// Font to use
	UnicodeFont font, font2;

	 int rand = (int) ((Math.random() * 10) +1);
	
	public String num = "" + rand;
	String numberSigned = "";
	
	public String getNum()
	{
		return num;
	}
	
	public void numberSigned(){
		try {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner (System.in);
		System.out.println("ENTER THE NUMBER: ");
		numberSigned = scanner.next();	
		} catch (Exception e) { System.out.println("TOMATO");}

	}
	

	private void randomNumber() 
	{
		long timeRemaining2 = 20 - (System.currentTimeMillis() / 1000 - curPauseTime);
		
		while (timeRemaining2 < 20) {
			
			// Display num on screen			
			
			if (num.equals(numberSigned)) {
				score = score + 10;
				//System.out.println("Score is: " + score);
			
			}
			
		}
		
		//System.out.println("Score is: " + Integer.toString(score));
		
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		// TODO Auto-generated method stub
		// Load font (used to center the text)

		// enterButton.setForeground(new Color(255,255,255));

		font = new UnicodeFont("fonts/Tribeca.ttf", 44, false, false);
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect());
		font.loadGlyphs();

		font2 = new UnicodeFont("fonts/Tribeca.ttf", 300, false, false);
		font2.addAsciiGlyphs();
		font2.getEffects().add(new ColorEffect());
		font2.loadGlyphs();

		countdownTime = "20";
		signNumber = num;
		countdownTimeWidth = font.getWidth(countdownTime);
		scoreWidth = font.getWidth(Integer.toString(score));
		signNumberWidth = font2.getWidth(signNumber);

		background = new Image("res/background.gif");
	}
	
	
	private void startCountdown() {
		// get the current time in milliseconds for reference use
		curPauseTime = System.currentTimeMillis() / 1000;
		// this tells the program to start counting
		getNum();
		countdown = true;
		
	}

	// checks the number of seconds left until game starts
	private void countDown() {

		// how many seconds left from 20 second count down
		long timeRemaining = 20 - (System.currentTimeMillis() / 1000 - curPauseTime);
		// updates the count down time string to render
		countdownTime = "" + timeRemaining;
		countdownTimeWidth = font.getWidth(countdownTime);

		if (timeRemaining < 1) {
			// this tells the program to stop counting
			countdown = false;
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		if (inGame) {
			renderGame(gc, g);
		}
		Display.sync(60);
	}

	public void renderGame(GameContainer gc, Graphics g) throws SlickException {
		// background.draw(0, 0);
		// draw the count down number
		
		if (inGame && countdown) {
			// g.setColor(Color.white);
			font.drawString((SCREEN_W / 2) - (countdownTimeWidth / 2),
					SCREEN_H / 20, countdownTime);
			font.drawString((SCREEN_W / 25) - (scoreWidth / 2),
					SCREEN_H / 20, "Score: " + Integer.toString(score));
			font2.drawString((SCREEN_W / 2) - (signNumberWidth / 2),
					SCREEN_H / 6, signNumber);
		
		}
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		// TODO Auto-generated method stub 
		if (countdown) {
			countDown();
		
		} else {
			startCountdown();
		}
		
			

	}

	public static void main(String[] args) throws SlickException {
		 AppGameContainer app = new AppGameContainer(new Sign("Sign It!",
	                false, false));
	        // set the dimensions of the game
	        app.setDisplayMode(800, 600, false);
	        // don't show the diagnostic FPS
	        app.setShowFPS(false);
	        // launch the game
	        app.start();
	}

}
