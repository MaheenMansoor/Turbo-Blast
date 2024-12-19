import javax.swing.*;

public class CarRaceGame {

    public static void main(String[] args) {
        // Launch the StartScreen when the game starts
        SwingUtilities.invokeLater(() -> {
            new StartScreen().setVisible(true);  // Display the start screen
        });
    }
}

