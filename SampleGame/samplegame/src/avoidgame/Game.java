package avoidgame;

import java.util.ArrayList;
import java.util.Random;
 
public class Game {
    private ArrayList<Plane> planes;
    private int score, width, height;
 
    public Game(int numPlanes, int width, int height) {
        this.width = width;
        this.height = height;
        int x = width;
        planes = new ArrayList<Plane>();
        for (int i = 0; i < numPlanes; i++) {
            planes.add(new Plane(genLocation(), -height, width, height,
                    genSpeed(), true));
        }
    }
 
    /**Adds a new plane to the game by adding it to list of planes**/
    public void addPlane() {
        planes.add(new Plane(genLocation(), 0, width, height, 
                          genSpeed(), true));
    }
 
    public int getScore() {
        return score;
    }
 
    public void resetScore() {
        score = 0;
    }
 
    public ArrayList<Plane> planes() {
        return planes;
    }
 
    /** Generates a random speed for a plane **/
    private float genSpeed() {
        return new Random().nextFloat() + 15f;
    }
 
    /**
     * Checks to see if you can place a plane so that this x coordinate 
     * does not collide horizontally with any plane
     **/
    public boolean canPlace(int x) {
        for (int i = 0; i < planes.size(); i++) {
            if (planes.get(i).wings.inHorizontalRange(x)) {
                return false;
            }
        }
        return true;
    }
 
    /**
     * Generates a valid x position in a horizontal range that is not 
         * occupied by a current plane
     **/
    public int genLocation() {
        int x = 0;
        do {
            x = new Random().nextInt(Avoid.SCREEN_W - height);
        } while (!canPlace(x));
        return x;
    }
 
    public void movePlanes(boolean laser, float laserX, 
                    float laserWidth, float laserHeight) {
        /**
         * Create a bounding box for the laser if there is a laser. 
         * Otherwise, the bounding box reference is null
        **/
        BoundingBox laserRange = (laser) ? new BoundingBox(laserX, 
               laserX +  laserWidth, 0, laserHeight) : null;
        // for all planes
        for (int i = 0; i < planes.size(); i++) {
            // Get the current plane
            Plane plane = planes.get(i);
            /**
             * Check if the plane is hit by a laser or if this plane 
             * is below is below the bottom of the screen
            **/
            if ((laserRange != null && laserRange.inRange(plane.wings))
                    || plane.y > Avoid.SCREEN_H) {
                // moves plane to the top with new x coordinate/speed
                plane.updateEnemy(genLocation(), -plane.height, 
                                                  genSpeed());
            } else {
                // Moves the plane by its speed
                plane.updateEnemy(plane.x, plane.y + plane.speed, 
                                                  plane.speed);
            }
        }
        score += 1;
 
    }
 
    public boolean checkLose(Plane player) {
        for (int i = 0; i < planes.size(); i++) {
            if (planes.get(i).isColliding(player)) {
                return true;
            }
        }
        return false;
    }
}