package avoidgame;

public class BoundingBox {
	float left;
	float right;
	float top;
	float bottom;
	float width;
	float height; 
	
	public BoundingBox(float left, float right, float bottom, float top) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.width = right - left;
        this.height = top - bottom;
    }
 
    public void update(float left, float right, float bottom, float top) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.width = right - left;
        this.height = top - bottom;
    }
 
    public boolean inHorizontalRange(float x) {
        return x > left && x < right;
    }
 
    public boolean inVerticalRange(float y) {
        return y > top && y < bottom;
    }
 
    public boolean inRange(BoundingBox r) {
        // find the box with the bigger height
        BoundingBox biggerHeight = (r.height >= height) ? r : this;
        BoundingBox smallerHeight = (biggerHeight == this) ? r : this;
        // find the box with the bigger width
        BoundingBox biggerWidth = (r.width >= width) ? r : this;
        BoundingBox smallerWidth = (biggerWidth == this) ? r : this;
 
        boolean hCollide= (smallerWidth.left >= biggerWidth.left
                               && smallerWidth.left <= biggerWidth.right)
                               ||(smallerWidth.right >= biggerWidth.left 
                               && smallerWidth.right <= biggerWidth.right);
 
        boolean vCollide= (smallerHeight.top >= biggerHeight.bottom 
                            && smallerHeight.top <= biggerHeight.top)
                            ||(smallerHeight.bottom >= biggerHeight.bottom
                            && smallerHeight.bottom <= biggerHeight.top);
        return hCollide&& vCollide;
    }
	
}
