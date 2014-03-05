package avoidgame;

public class Laser {
    private float energy;
 
    public Laser() {
        energy = 100;
    }
 
    public float getEnergy() {
        return energy;
    }
 
    public void resetEnergy() {
        energy = 100;
    }
 
    public boolean isDepleted() {
        return energy == 0;
    }
    public boolean canFire(){
        return energy > 20; 
    }
    public void deplete() {
        energy = Math.max(energy - 4, 0);
    }
 
    public void regen() {
        energy = Math.min(energy + .1f, 100);
    }
 
}