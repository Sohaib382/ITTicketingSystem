package view;

import controller.TicketController;
import model.ITComponent;
import model.Ticket;
import javax.swing.*;
import java.awt.*;

public class SubmitTicketPanel extends JPanel {
    private JTextField         titleField;
    private JTextArea          descArea;
    private JComboBox<String>      priorityBox;
    private JComboBox<ITComponent> componentBox;
    private JLabel             statusLabel;
    private TicketController   controller;

    public SubmitTicketPanel(TicketController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Report a New IT Issue"));
        buildUI();
    }

    private void buildUI() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Title:"), gbc);
        titleField = new JTextField(28);
        gbc.gridx = 1;
        form.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Priority:"), gbc);
        priorityBox = new JComboBox<>(new String[]{"LOW", "MEDIUM", "HIGH", "CRITICAL"});
        gbc.gridx = 1;
        form.add(priorityBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Affected Component:"), gbc);
        componentBox = new JComboBox<>();
        for (ITComponent c : controller.getComponents()) componentBox.addItem(c);
        gbc.gridx = 1;
        form.add(componentBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Description:"), gbc);
        descArea = new JTextArea(5, 28);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.CENTER;
        form.add(new JScrollPane(descArea), gbc);

        JButton submitBtn = new JButton("Submit Ticket");
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        form.add(submitBtn, gbc);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        gbc.gridy = 5;
        form.add(statusLabel, gbc);

        submitBtn.addActionListener(e -> submitTicket());
        add(form, BorderLayout.CENTER);
    }

    private void submitTicket() {
        String title    = titleField.getText().trim();
        String desc     = descArea.getText().trim();
        String priority = (String) priorityBox.getSelectedItem();
        ITComponent comp = (ITComponent) componentBox.getSelectedItem();

        try {
            Ticket t = controller.submitTicket(
                title, desc, priority,
                comp != null ? comp.getComponentId() : null);
            statusLabel.setForeground(new Color(0, 130, 0));
            statusLabel.setText("Success! Ticket created: " + t.getTicketId());
            titleField.setText("");
            descArea.setText("");
        } catch (Exception ex) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Error: " + ex.getMessage());
        }
    }
}