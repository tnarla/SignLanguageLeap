package avoidgame;

import java.util.ArrayList;
import java.util.Random;

public class Game2 {
	private ArrayList<Number> numbers;
	private int score;
	
	public Game2() {
		
	}
	
	public int getScore() {
        return score;
    }
 
    public void resetScore() {
        score = 0;
    }
	
	public static int randInt(int min, int max) {
		Random rand = new Random();
		min = 0;
		max = 9;
		int randomNum = rand.nextInt((max - min));
		return randomNum;	
		
	}
}
