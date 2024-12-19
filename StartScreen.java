import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartScreen extends JFrame {

    private String selectedCar = null;  // Holds the selected car
    private String playerName = null;   // Holds the player's name
    private Rectangle[] carRectangles = new Rectangle[4];  // Areas where cars will be drawn
    private boolean[] isHovered = new boolean[4];  // Array to track hover status for each car
    private JTextField nameField;  // Name input field
    private GameWindow gameWindow;  // Reference to GameWindow to pass the selected car index

    public StartScreen() {
        setTitle("Car Race Game - Start Screen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Apply a smoother, abstract background with a gradient
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 0, 128), 800, 600, new Color(255, 99, 71)); // Deep blue to orange gradient
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        });

        // Name input field (modern design with dark background and white text)
        JLabel nameLabel = new JLabel("Enter Your Name:");
        nameLabel.setBounds(150, 20, 200, 30);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        add(nameLabel);

        nameField = new JTextField(20);
        nameField.setBounds(150, 50, 200, 30);
        nameField.setOpaque(true);
        nameField.setBackground(new Color(40, 40, 40)); // Dark background for the name field
        nameField.setForeground(Color.WHITE); // White text
        nameField.setFont(new Font("Arial", Font.PLAIN, 18));
        nameField.setBorder(BorderFactory.createLineBorder(new Color(255, 99, 71), 2)); // Light orange border
        add(nameField);

        // Create and display 4 custom cars (2 cars per row)
        createCarButtons();

        // Start Game Button (large with dark background and light hover effect)
        JButton startButton = new JButton("Start Game");
        startButton.setBounds(150, 460, 500, 80); // Larger button size
        startButton.setBackground(new Color(0, 0, 0)); // Dark background color (black)
        startButton.setFont(new Font("Arial", Font.BOLD, 24)); // Larger font size
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true)); // White border with rounded corners
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerName = nameField.getText();
                if (selectedCar != null && !playerName.isEmpty()) {
                    // Hide Start Screen and open Game Window with the selected car
                    setVisible(false);

                    // Pass selected car index to GameWindow
                    int selectedCarIndex = getSelectedCarIndex();
                    gameWindow = new GameWindow(playerName, selectedCar);
                    gameWindow.setSelectedCarIndex(selectedCarIndex);  // Set the selected car index
                    gameWindow.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter your name and select a car.");
                }
            }
        });
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setBackground(new Color(255, 99, 71));  // Lighter color on hover (light orange)
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setBackground(new Color(0, 0, 0));  // Original dark color when not hovered
            }
        });
        add(startButton);
    }

    // Method to create and display 4 custom cars (2 cars per row)
    private void createCarButtons() {
        // Get the width of the window to center the car boxes
        int windowWidth = getWidth();

        // Car box dimensions
        int carBoxWidth = 180;  // Width of each car box
        int carBoxHeight = 180; // Height of each car box
        int margin = 40; // Margin between the car boxes

        // Calculate starting X position to center the car boxes
        int totalWidth = 2 * carBoxWidth + margin; // Total width for two cars per row
        int startX = (windowWidth - totalWidth) / 2;  // Center the cars horizontally

        // Y position (vertical positioning remains fixed)
        int startY = 150;  // Starting Y-position for the cars

        // Create the car rectangles and add mouse listeners for hover and selection
        for (int i = 0; i < 4; i++) {
            int xPos = startX + (i % 2) * (carBoxWidth + margin);  // Adjust X for 2 cars per row
            int yPos = startY + (i / 2) * (carBoxHeight + margin); // Adjust Y for 2 cars per row
            carRectangles[i] = new Rectangle(xPos, yPos, carBoxWidth, carBoxHeight);

            // Mouse listener to handle hover and car selection
            final int index = i;  // Capture the index for car selection
            final String[] carNames = {"Car 1", "Car 2", "Car 3", "Car 4"};

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    // Check if the mouse click is inside any of the car rectangles
                    if (carRectangles[index].contains(e.getPoint())) {
                        selectedCar = carNames[index];  // Set the selected car
                        repaint();  // Repaint to show the darkening effect on the selected car
                    }
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    // Check if mouse is hovering over a car
                    for (int i = 0; i < 4; i++) {
                        if (carRectangles[i].contains(e.getPoint())) {
                            isHovered[i] = true;
                        } else {
                            isHovered[i] = false;
                        }
                    }
                    repaint();  // Trigger repaint if hovering over any car
                }
            });
        }
    }

    // Method to get the selected car index
    private int getSelectedCarIndex() {
        switch (selectedCar) {
            case "Car 1": return 0;
            case "Car 2": return 1;
            case "Car 3": return 2;
            case "Car 4": return 3;
            default: return -1;
        }
    }

    // Method to draw cars on the screen with a vertical design inside square boxes
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Cast Graphics to Graphics2D to use setStroke and other advanced rendering features
        Graphics2D g2d = (Graphics2D) g;

        // Draw only the cars themselves without the background box (just the car itself)
        for (int i = 0; i < 4; i++) {
            drawCar(g2d, carRectangles[i].x + 10, carRectangles[i].y + 10, i, isHovered[i], selectedCar != null && selectedCar.equals("Car " + (i + 1)));
        }
    }

    // Helper method to choose a color for each car (based on index)
    private Color getCarColor(int index) {
        switch (index) {
            case 0: return Color.RED;
            case 1: return Color.BLUE;
            case 2: return Color.GREEN;
            case 3: return Color.YELLOW;
            default: return Color.BLACK;
        }
    }

    // Method to draw a vertical car structure with 4 tires, sleek body, and windows
    private void drawCar(Graphics2D g2d, int x, int y, int index, boolean isHovered, boolean isSelected) {
        // Select the color based on the car index
        Color carColor = getCarColor(index);
        Color boxColor = isHovered ? Color.CYAN : new Color(100, 100, 100);  // Lighter box when hovered
        if (isSelected) {
            boxColor = new Color(50, 50, 50);  // Darker color when selected
        }

        // Draw the box around the car
        g2d.setColor(boxColor);
        g2d.fillRect(x, y, 160, 160);  // Box size for the car (fixed size)

        // Draw the car body
        g2d.setColor(carColor);
        int carWidth = 60;  // Standard width of the car body
        int carHeight = 100; // Standard height of the car body
        g2d.fillRect(x + (160 - carWidth) / 2, y + (160 - carHeight) / 2, carWidth, carHeight);  // Main body of the car

        // Draw car windows (a simple rectangle)
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x + (160 - 40) / 2, y + (160 - carHeight) / 2 + 20, 40, 20); // Window

        // Draw tires (4 simple black circles)
        g2d.setColor(Color.BLACK);
        int tireDiameter = 30;
        g2d.fillOval(x + 20, y + 140 - tireDiameter, tireDiameter, tireDiameter);  // Left tire
        g2d.fillOval(x + 100, y + 140 - tireDiameter, tireDiameter, tireDiameter); // Right tire
    }
}
