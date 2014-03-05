package signit;

import java.io.IOException;
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

	public class LeapMotionControl {
		 // Create a sample listener and controller
	    SampleListener listener = new SampleListener();
	    
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
