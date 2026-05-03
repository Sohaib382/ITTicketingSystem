package view;

import controller.TicketController;
import model.Ticket;
import model.TicketStatus;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackPanel extends JPanel {
    private JComboBox<Ticket> ticketBox;
    private JSlider           ratingSlider;
    private JTextArea         commentArea;
    private JLabel            statusLabel;
    private TicketController  controller;

    public FeedbackPanel(TicketController controller) {
        this.controller = controller;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Submit Feedback on Resolved Ticket"));
        buildUI();
    }

    private void buildUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Select Resolved Ticket:"), gbc);
        ticketBox = new JComboBox<>();
        gbc.gridx = 1;
        add(ticketBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Rating (1 = Poor, 5 = Excellent):"), gbc);
        ratingSlider = new JSlider(1, 5, 3);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        ratingSlider.setSnapToTicks(true);
        gbc.gridx = 1;
        add(ratingSlider, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.NORTHWEST;
        add(new JLabel("Comments:"), gbc);
        commentArea = new JTextArea(4, 22);
        commentArea.setLineWrap(true);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.CENTER;
        add(new JScrollPane(commentArea), gbc);

        JButton submitBtn = new JButton("Submit Feedback");
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(submitBtn, gbc);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        gbc.gridy = 4;
        add(statusLabel, gbc);

        submitBtn.addActionListener(e -> submit());
    }

    private void submit() {
        Ticket t = (Ticket) ticketBox.getSelectedItem();
        if (t == null) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("No resolved tickets available for feedback.");
            return;
        }
        try {
            controller.submitFeedback(
                t.getTicketId(), ratingSlider.getValue(),
                commentArea.getText().trim());
            statusLabel.setForeground(new Color(0, 130, 0));
            statusLabel.setText("Feedback submitted for " + t.getTicketId() + ". Thank you!");
            commentArea.setText("");
        } catch (Exception ex) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Error: " + ex.getMessage());
        }
    }

    public void refresh() {
        ticketBox.removeAllItems();
        List<Ticket> resolved = controller.getTicketsByCurrentUser().stream()
            .filter(t -> t.getStatus() == TicketStatus.RESOLVED ||
                         t.getStatus() == TicketStatus.CLOSED)
            .collect(Collectors.toList());
        resolved.forEach(ticketBox::addItem);
    }
}