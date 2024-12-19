import java.awt.Color;

public class Obstacle {
    public static final int WIDTH = 40;
    public static final int HEIGHT = 40;
    private int x, y;
    private Color color;

    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = Color.RED;  // Default obstacle color
    }

    // Method to move the obstacle down the track
    public void move() {
        y += 10;  // Move the obstacle down by 10 pixels
        if (y > 600) {  // If the obstacle goes off the screen, reset it
            y = -40;  // Reset to the top of the screen
            x = (int)(Math.random() * 600) + 100;  // Randomize the X position
        }
    }

    // Getter methods for x, y, and color
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }
}
