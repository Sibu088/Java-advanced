package SoccerAppRanking;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Class representing a Swing-based GUI application for displaying soccer league rankings
public class LeagueApp extends JFrame {
    // Instance variables for the GUI components and data model
    private JTextArea displayArea; // Text area to display the league rankings
    private LeagueTable leagueTable; // Data model to manage team rankings and match data
    private JButton loadButton, clearButton; // Buttons for loading match files and clearing the table

    // Constructor to initialize the GUI
    public LeagueApp() {
        // Set the window title
        super("⚽ Soccer League Rankings");
        // Initialize the SoccerAppRanking.LeagueTable object to manage team data
        leagueTable = new LeagueTable();

        // === Main Window Setup ===
        // Set the layout manager to BorderLayout with 10-pixel gaps
        setLayout(new BorderLayout(10, 10));
        // Set the window size to 600x500 pixels
        setSize(600, 500);
        // Set the window background color to a light gray-blue
        getContentPane().setBackground(new Color(245, 247, 250));
        // Center the window on the screen
        setLocationRelativeTo(null);
        // Close the application when the window is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // === Header Panel ===
        // Create a panel for the header section
        JPanel headerPanel = new JPanel();
        // Set the header background to a dark blue color
        headerPanel.setBackground(new Color(0, 102, 204));
        // Add padding around the header content
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Create and configure the title label
        JLabel title = new JLabel("⚽ Soccer League Ranking Table");
        title.setForeground(Color.WHITE); // Set text color to white
        title.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Set font to bold Segoe UI, size 24
        // Add the title to the header panel
        headerPanel.add(title);
        // Add the header panel to the top (NORTH) of the main window
        add(headerPanel, BorderLayout.NORTH);

        // === Display Area (Center) ===
        // Initialize the text area for displaying rankings
        displayArea = new JTextArea();
        displayArea.setEditable(false); // Make the text area read-only
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 16)); // Use Consolas font, size 16
        displayArea.setBackground(Color.WHITE); // Set background to white
        // Add a compound border with a blue outline and padding
        displayArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        // Add the text area to a scroll pane and place it in the center of the window
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // === Footer Panel with Buttons ===
        // Create a panel for the footer section
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(245, 247, 250)); // Match the window background
        // Use FlowLayout to arrange buttons horizontally with 20-pixel gaps
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Create styled buttons for loading and clearing data
        loadButton = createStyledButton("📂 Load Matches File", new Color(0, 102, 204));
        clearButton = createStyledButton("🧹 Clear Table", new Color(220, 53, 69));

        // Add buttons to the footer panel
        footerPanel.add(loadButton);
        footerPanel.add(clearButton);
        // Add the footer panel to the bottom (SOUTH) of the window
        add(footerPanel, BorderLayout.SOUTH);

        // === Button Actions ===
        // Add action listener for the load button to trigger file loading
        loadButton.addActionListener(e -> loadFile());
        // Add action listener for the clear button to reset the table
        clearButton.addActionListener(e -> {
            displayArea.setText(""); // Clear the text area
            leagueTable = new LeagueTable(); // Reset the league table
            // Show a confirmation message
            JOptionPane.showMessageDialog(this, "Table cleared!", "Reset", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // Method to create a styled JButton with custom text and background color
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text); // Create a new button with the given text
        button.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Set font to bold Segoe UI, size 14
        button.setForeground(Color.WHITE); // Set text color to white
        button.setBackground(bgColor); // Set the background color
        button.setFocusPainted(false); // Remove focus outline
        // Add padding to the button
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Show hand cursor on hover
        button.setOpaque(true); // Make the background color visible
        button.setBorderPainted(false); // Remove default border
        return button;
    }

    // Method to handle file loading for match data
    private void loadFile() {
        // Create a file chooser dialog
        JFileChooser fileChooser = new JFileChooser();
        // Show the dialog and check if a file was selected
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Get the absolute path of the selected file
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            // Load match data into the league table
            leagueTable.loadMatches(filePath);
            // Show a success message
            JOptionPane.showMessageDialog(this, "✅ Matches file loaded successfully!");
            // Update the display with the new rankings
            displayResults();
        }
    }

    // Method to display the ranked teams in the text area
    private void displayResults() {
        // Get the list of ranked teams from the league table
        List<Team> rankedTeams = leagueTable.getRankedTeams();
        displayArea.setText(""); // Clear the current content
        int rank = 1; // Initialize rank counter
        // Append each team's rank, name, and points to the text area
        for (Team team : rankedTeams) {
            displayArea.append(rank + ". " + team.getName() + " — " + team.getPoints() + " pts\n");
            rank++;
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> {
            LeagueApp app = new LeagueApp(); // Create an instance of the application
            app.setVisible(true); // Make the window visible
        });
    }
}