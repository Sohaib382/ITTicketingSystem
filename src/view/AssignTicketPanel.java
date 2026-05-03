package view;

import controller.TicketController;
import model.ITStaff;
import model.Ticket;
import javax.swing.*;
import java.awt.*;

public class AssignTicketPanel extends JPanel {
    private JComboBox<Ticket>  ticketBox;
    private JComboBox<ITStaff> staffBox;
    private JLabel             statusLabel;
    private TicketController   controller;

    public AssignTicketPanel(TicketController controller) {
        this.controller = controller;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Assign Ticket to IT Staff"));
        buildUI();
    }

    private void buildUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Select Unassigned Ticket:"), gbc);
        ticketBox = new JComboBox<>();
        gbc.gridx = 1;
        add(ticketBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Assign To (IT Staff):"), gbc);
        staffBox = new JComboBox<>();
        gbc.gridx = 1;
        add(staffBox, gbc);

        JButton assignBtn = new JButton("Assign Now");
        assignBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(assignBtn, gbc);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        gbc.gridy = 3;
        add(statusLabel, gbc);

        assignBtn.addActionListener(e -> assign());
    }

    private void assign() {
        Ticket  t = (Ticket)  ticketBox.getSelectedItem();
        ITStaff s = (ITStaff) staffBox.getSelectedItem();
        if (t == null || s == null) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Please select both a ticket and a staff member.");
            return;
        }
        try {
            controller.assignTicket(t.getTicketId(), s.getUserId());
            statusLabel.setForeground(new Color(0, 130, 0));
            statusLabel.setText(t.getTicketId() + " assigned to " + s.getName());
            refresh();
        } catch (Exception ex) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Error: " + ex.getMessage());
        }
    }

    public void refresh() {
        ticketBox.removeAllItems();
        staffBox.removeAllItems();
        controller.getAllTickets().stream()
            .filter(t -> t.getAssignedTo() == null)
            .forEach(ticketBox::addItem);
        controller.getAllStaff().forEach(staffBox::addItem);
    }
}