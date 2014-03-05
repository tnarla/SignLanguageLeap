package avoidgame;

public class Plane {
    public float x, y, width, height, speed;
    BoundingBox body, wings;
 
    public Plane(float x, float y, int width, int height, float speed,
            boolean enemy) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        // Horizontal Coordinates
        // Body
        float bodyLeft = x + width / 2 - 5;
        float bodyRight = x + width / 2 + 5;
        // Wing
        float wingLeft = x;
        float wingRight = x + width;
        // Vertical Coordinates
        // Body (enemy=>Bottom Half player=>Top Half)
        float bodyTop = y + height;
        float bodyBottom = y;
        // Wing (enemy=>Top Half player=>Bottom Half)
        float wingBottom = (enemy) ? y + 50 : y + height / 2;
        float wingTop = (enemy) ? y + height / 2 : y + height;
 
        body = new BoundingBox(bodyLeft, bodyRight, bodyBottom, bodyTop);
        wings = new BoundingBox(wingLeft, wingRight, wingBottom, wingTop);
 
    }
 
    public boolean isColliding(Plane b) {
        return body.inRange(b.body) || body.inRange(b.wings)
                || wings.inRange(b.body) || wings.inRange(b.wings);
    }
 
    public void updatePlayer(float x, float y) {
        this.x = x;
        this.y = y;
        body.update(x + width / 2 - 5, x + width / 2 + 5, y, y + height);
        wings.update(x, x + width, y + height / 2, y + height - 20);
 
    }
 
    public void updateEnemy(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        body.update(x + width / 2 - 5, x + width / 2 + 5, y, y + height);
        wings.update(x, x + width, y + 20, y + height / 2);
    }
}