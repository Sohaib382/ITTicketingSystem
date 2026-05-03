package view;

import controller.TicketController;
import model.User;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private TicketController controller;
    private CardLayout       cards;
    private JPanel           cardPanel;
    private LoginPanel       loginPanel;
    private JPanel           dashWrapper;

    public MainFrame(TicketController controller) {
        this.controller = controller;
        setTitle("IT Ticketing System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);

        cards     = new CardLayout();
        cardPanel = new JPanel(cards);

        loginPanel = new LoginPanel(controller, this);
        cardPanel.add(loginPanel, "LOGIN");

        add(cardPanel);
        cards.show(cardPanel, "LOGIN");
    }

    public void showDashboard() {
        if (dashWrapper != null) cardPanel.remove(dashWrapper);

        User user      = controller.getLoggedInUser();
        JTabbedPane dashboard = new JTabbedPane();

        if (user.getRole().equals("End User")) {
            SubmitTicketPanel submitPanel = new SubmitTicketPanel(controller);
            ViewTicketsPanel  viewPanel   = new ViewTicketsPanel(controller);
            FeedbackPanel     fbPanel     = new FeedbackPanel(controller);
            viewPanel.refresh();
            fbPanel.refresh();

            dashboard.addTab("Submit Ticket", submitPanel);
            dashboard.addTab("My Tickets",    viewPanel);
            dashboard.addTab("Feedback",      fbPanel);

            dashboard.addChangeListener(e -> {
                int idx = dashboard.getSelectedIndex();
                if (idx == 1) viewPanel.refresh();
                if (idx == 2) fbPanel.refresh();
            });

        } else {
            ViewTicketsPanel  viewPanel   = new ViewTicketsPanel(controller);
            AssignTicketPanel assignPanel = new AssignTicketPanel(controller);
            viewPanel.refresh();
            assignPanel.refresh();

            dashboard.addTab("All Tickets",   viewPanel);
            dashboard.addTab("Assign Ticket", assignPanel);

            dashboard.addChangeListener(e -> {
                int idx = dashboard.getSelectedIndex();
                if (idx == 0) viewPanel.refresh();
                if (idx == 1) assignPanel.refresh();
            });
        }

        // Top header bar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(34, 49, 63));
        header.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel welcome = new JLabel("  Welcome, " + user.getName() +
                                    "   |   Role: " + user.getRole());
        welcome.setFont(new Font("SansSerif", Font.BOLD, 14));
        welcome.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> {
            controller.logout();
            cards.show(cardPanel, "LOGIN");
        });

        header.add(welcome,   BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);

        dashWrapper = new JPanel(new BorderLayout());
        dashWrapper.add(header,    BorderLayout.NORTH);
        dashWrapper.add(dashboard, BorderLayout.CENTER);

        cardPanel.add(dashWrapper, "DASHBOARD");
        cards.show(cardPanel, "DASHBOARD");
        revalidate();
        repaint();
    }
}