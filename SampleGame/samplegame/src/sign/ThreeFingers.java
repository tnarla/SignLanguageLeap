package sign;

import java.io.IOException;
import java.util.ArrayList;

import sign.Sign;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.Vector;

class SampleListener extends Listener {
	
	public String answerKnown = "";
	private String answer; 
	private float numberTen;
	AppGameContainer app;
	Sign myGame;

	public SampleListener(Sign a) throws SlickException {
        myGame.mouse = false;
        app = new AppGameContainer(myGame);
    }
	
	public void onInit(Controller controller) {
        System.out.println("Initialized");
    }
 
	
    public void onConnect(Controller controller) {
        System.out.println("Connected");
 
    }
 
    public void onDisconnect(Controller controller) {
        System.out.println("Disconnected");
    }
 
    public void onExit(Controller controller) {
        System.out.println("Exited");
    }
    
//    public void answerKnown(String answer) {
//    	
//    	// Uncomment when you want just one answer to show up
//    	
//    	
////    	if (lastNumber != answer) { 
//    		System.out.println(answer);
//    		lastNumber = answer;
////    	}
//
//    }
//    
    
    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        if (!frame.hands().empty()) {
            // Get the first hand
            Hand hand = frame.hands().get(0);
            FingerList fingers = hand.fingers();
            
            
            float left = frame.pointables().leftmost().tipPosition().getX();
            float right = frame.pointables().rightmost().tipPosition().getX();
            float front = frame.pointables().frontmost().tipPosition().getX();
            
           float frontlength =frame.pointables().frontmost().tipPosition().getZ();
           float frontheight = frame.pointables().frontmost().tipPosition().getY();
           
           float frontavg = (frontlength + frontheight + front) / 3;
           
            
           //System.out.println((Math.abs(front - left) - Math.abs(right - front)));
            
            if (!myGame.gameOver){
	            if (fingers.count()== 1) {
	                       	
	            	if ((numberTen - frontavg) < 3) {
	            	answerKnown = "1";
	            	numberTen = frontavg;
	            	answer = "1";
	            	
	            	}
	            	else if (numberTen != frontavg){
	            	answerKnown = "10";
	            	numberTen = frontavg;
	            	answer = "10";
	            	}
	            }
	            else if (fingers.count() == 2){
	            	answerKnown = "2";
	            	answer = "2";
	            }
	            
	            else if (fingers.count() == 5){
	            	answerKnown = "5";
	            	answer = "5";
	            }  	    
	            
	            else if (fingers.count() == 4){
	            	answerKnown = "4";
	            	answer = "4";
	            }  	       
	            
	            else if (fingers.count() == 3){
	            	if (left == front) {
	            		answerKnown = "9";
	            		answer = "9";
	            		
	            	}
	               	else if (right == front){
	            		answerKnown = "3";
	            		answer = "3";
	               	}
	               	else if ((left != front) && (right != front) && (Math.abs(Math.abs(front - left) - Math.abs(right - front)) < 10)){
	            		answerKnown = "6";
	            		answer = "6";
	            	}
	            	else if (Math.abs(front - left) > Math.abs(right - front)){
	            		answerKnown = "8";
	            		answer = "8";
	            	}
	            	else if (Math.abs(front - left) < Math.abs(right - front)){
	            		answerKnown = "7";
	            		answer = "7";
	            	} 
	
	            }
	            
            System.out.println("Num = " + myGame.num);
        	System.out.println("AnswerKnown = " + answerKnown);
	        	
	        if (myGame.num.equals(answerKnown)){
	        	
	        	myGame.score = myGame.score + 10;
	        }
        }
            
        }
        System.out.println(answer);
        

    }

static class ThreeFingers {
	public static void main(String[] args) throws SlickException {
		 
        // Create a sample listener and controller
        SampleListener listener = new SampleListener(new Sign("Sign It!", true, false));
        
        Controller controller = new Controller();
        // Have the sample listener receive events from the controller
        controller.addListener(listener);
        listener.app.setDisplayMode(800, 600, false);
        listener.app.setShowFPS(false);
        listener.app.start();
        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Remove the sample listener when done
        controller.removeListener(listener);
    }
}
}