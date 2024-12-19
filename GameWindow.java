onecartrackwith obstacle
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GameWindow extends JFrame {

    private String playerName;
    private String selectedCar;
    private int selectedCarIndex = -1;  // Index of the selected car
    private int carWidth = 60, carHeight = 100;  // Standard car size
    private Timer movementTimer;
    private boolean gameStarted = false;
    private final int TRACK_LENGTH = 10000;
    private JTextField playerNameField;

    // Flags for controlling horizontal movement
    private boolean moveLeft = false;
    private boolean moveRight = false;
    final int MOVE_INCREMENT = 5; // Small step for gradual movement (in pixels)

    // Variables for the scrolling road
    private int roadPositionY1 = 0;
    private int roadPositionY2 = -600;
    private double distanceTraveled = 0;

    private Color[] carColors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};  // 4 Car colors (one per lane)
    private int carPositionY = 450; // The vertical position for the car (fixed)

    // Vertical speed modifiers
    private double speed = 120;  // Initial speed (default 120)
    private final double SPEED_UP_INCREMENT = 0.10;  // 10% increment
    private final double SPEED_DOWN_INCREMENT = 0.05;  // 5% decrement

    private int lapCount = 0;  // Lap count
    private boolean gameEnded = false; // Flag for game end
    private final int FINISH_LINE_Y = 200; // Position for the finish line (horizontal line after 3 laps)

    // List to hold obstacles
    private ArrayList<Obstacle> obstacles;
    private Random random;

    public GameWindow(String playerName, String selectedCar) {
        this.playerName = playerName;
        this.selectedCar = selectedCar;

        obstacles = new ArrayList<>();
        random = new Random();

        setTitle("Car Race Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTrack(g);           // Draw the track
                drawCars(g);            // Draw the car
                drawSpeedAndDistance(g); // Display speed and distance
                drawObstacles(g);       // Draw obstacles
            }
        };

        add(gamePanel);
        gamePanel.setBackground(new Color(50, 50, 50)); // Dark gray for background

        JButton startButton = new JButton("Start Game");
        startButton.setBounds(350, 250, 100, 50);
        gamePanel.setLayout(null);
        gamePanel.add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
                startButton.setVisible(false);
            }
        });

        // Speed control buttons
        JButton speedUpButton = new JButton("Speed Up");
        speedUpButton.setBounds(650, 100, 100, 30);
        JButton speedDownButton = new JButton("Speed Down");
        speedDownButton.setBounds(650, 150, 100, 30);
        gamePanel.add(speedUpButton);
        gamePanel.add(speedDownButton);

        // Speed Up Button Action Listener
        speedUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Increase speed by 10% and move the car upwards
                speed += speed * SPEED_UP_INCREMENT;
                carPositionY -= speed / 10;  // Move car up based on new speed
            }
        });

        // Speed Down Button Action Listener
        speedDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Decrease speed by 5% and move the car downwards
                speed -= speed * SPEED_DOWN_INCREMENT;
                carPositionY += speed / 10;  // Move car down based on new speed
            }
        });

        // Key listener for car movement
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    moveLeft = true;  // Start moving left
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    moveRight = true;  // Start moving right
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    moveLeft = false;  // Stop moving left
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    moveRight = false;  // Stop moving right
                }
            }
        });
        gamePanel.setFocusable(true);

        playerNameField = new JTextField(playerName);
        playerNameField.setBounds(50, 50, 200, 30);
        gamePanel.add(playerNameField);
    }

    // Method to set selected car index
    public void setSelectedCarIndex(int selectedCarIndex) {
        this.selectedCarIndex = selectedCarIndex;  // Set the car index
    }

    private void startGame() {
        gameStarted = true;

        movementTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameEnded) {
                    moveRoad(); // Move the road
                    moveCars(); // Move the car horizontally within its lane
                    checkLapCompletion();  // Check if lap is completed
                    checkFinishLineCrossed(); // Check if car crossed the finish line
                    generateObstacles(); // Generate obstacles at random intervals
                }
            }
        });
        movementTimer.start();
    }

    // Method to generate obstacles at random X positions
    private void generateObstacles() {
        if (random.nextInt(100) < 5) {  // 5% chance to generate a new obstacle
            int x = random.nextInt(600) + 100;  // Random X position on the track
            int y = -40;  // Start above the screen
            obstacles.add(new Obstacle(x, y));  // Add the obstacle to the list
        }
    }

    // Method to move the road
    private void moveRoad() {
        if (gameStarted) {
            roadPositionY1 += 10;  // Speed of road scrolling
            roadPositionY2 += 10;

            // Keep road segments moving indefinitely
            if (roadPositionY1 >= getHeight()) {
                roadPositionY1 = -600;  // Reset road to the bottom
            }
            if (roadPositionY2 >= getHeight()) {
                roadPositionY2 = -600;  // Reset road to the bottom
            }

            distanceTraveled += 0.1;  // Simulate the distance the car has traveled as the road scrolls
            repaint();
        }
    }

    // Method to draw the race tracks with lanes and footpaths
    private void drawTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw the road background
        g2d.setColor(new Color(50, 50, 50));  // Dark gray for the road
        g2d.fillRect(100, roadPositionY1, 600, getHeight());
        g2d.fillRect(100, roadPositionY2, 600, getHeight());

        // Draw footpath (sidewalks) on both sides
        g2d.setColor(new Color(169, 169, 169));  // Light gray for the footpath
        g2d.fillRect(0, roadPositionY1, 100, getHeight());
        g2d.fillRect(700, roadPositionY1, 100, getHeight());
        g2d.fillRect(0, roadPositionY2, 100, getHeight());
        g2d.fillRect(700, roadPositionY2, 100, getHeight());
    }

    private void drawCars(Graphics g) {
        if (selectedCarIndex >= 0 && selectedCarIndex < 4) {
            int laneX = 200 + (selectedCarIndex * 100); // Lanes X positions: 200, 300, 400, 500 for 4 lanes
            int carX = laneX + 20;  // Move the car 20 pixels inside the lane
            g.setColor(carColors[selectedCarIndex]);  // Set car color based on selected index
            g.fillRect(carX, carPositionY, carWidth, carHeight);  // Draw the selected car
        }
    }

    // Method to move only the selected car horizontally
    private void moveCars() {
        if (moveLeft && selectedCarIndex > 0) {
            selectedCarIndex--;  // Move left to the previous lane
        }
        if (moveRight && selectedCarIndex < 3) {
            selectedCarIndex++;  // Move right to the next lane
        }
    }

    private void checkLapCompletion() {
        if (carPositionY <= FINISH_LINE_Y) {
            lapCount++;
            carPositionY = 450; // Reset car position
        }
    }

    private void checkFinishLineCrossed() {
        if (lapCount >= 3) {
            gameEnded = true;
            JOptionPane.showMessageDialog(this, "You Win! Final Score: " + distanceTraveled);
            movementTimer.stop();
        }
    }

    private void drawSpeedAndDistance(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Speed: " + speed + " km/h", 650, 50);
        g.drawString("Distance: " + distanceTraveled + " m", 650, 70);
    }

    // Draw obstacles
    private void drawObstacles(Graphics g) {
        for (Obstacle obstacle : obstacles) {
            obstacle.moveObstacle();
            obstacle.drawObstacle(g);
        }
    }

    public static void main(String[] args) {
        String playerName = "Player";  // Replace with user input
        String selectedCar = "Car 1";  // Replace with selected car
        GameWindow gameWindow = new GameWindow(playerName, selectedCar);
        gameWindow.setSelectedCarIndex(0);  // Set default car index (first car)
        gameWindow.setVisible(true);
    }
}


