package view;

import controller.TicketController;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField    emailField;
    private JPasswordField passwordField;
    private JLabel        statusLabel;
    private TicketController controller;
    private MainFrame     mainFrame;

    public LoginPanel(TicketController controller, MainFrame mainFrame) {
        this.controller = controller;
        this.mainFrame  = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("IT Ticketing System", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        JLabel sub = new JLabel("Please login to continue", SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(Color.GRAY);
        gbc.gridy = 1;
        add(sub, gbc);

        // Email
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Email:"), gbc);
        emailField = new JTextField(22);
        gbc.gridx = 1;
        add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(22);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginBtn.setPreferredSize(new Dimension(200, 36));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(loginBtn, gbc);

        // Status
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        gbc.gridy = 5;
        add(statusLabel, gbc);

        // Hint
        JLabel hint = new JLabel(
            "<html><center><b>Demo Accounts:</b><br>" +
            "End User &nbsp;→&nbsp; alice@org.com &nbsp;/&nbsp; alice123<br>" +
            "IT Staff &nbsp;&nbsp;→&nbsp; charlie@org.com &nbsp;/&nbsp; charlie123" +
            "</center></html>", SwingConstants.CENTER);
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.setForeground(new Color(100, 100, 100));
        gbc.gridy = 6;
        add(hint, gbc);

        loginBtn.addActionListener(e -> attemptLogin());
        passwordField.addActionListener(e -> attemptLogin());
    }

    private void attemptLogin() {
        String email = emailField.getText().trim();
        String pass  = new String(passwordField.getPassword());

        if (email.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("Please enter both email and password.");
            return;
        }
        if (controller.login(email, pass)) {
            statusLabel.setText("");
            mainFrame.showDashboard();
        } else {
            statusLabel.setText("Invalid email or password. Try again.");
            passwordField.setText("");
        }
    }
}