package view;

import controller.TicketController;
import model.Ticket;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
public class ViewTicketsPanel extends JPanel {
    private JTable            table;
    private DefaultTableModel tableModel;
    private JTextArea         detailArea;
    private JComboBox<String> actionBox;
    private TicketController  controller;

    public ViewTicketsPanel(TicketController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createTitledBorder("Tickets"));
        buildUI();
    }

    private void buildUI() {
        String[] cols = {"Ticket ID","Title","Priority","Status","Submitted By","Assigned To"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        // Colour rows by status
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t,v,sel,foc,row,col);
                if (!sel) {
                    String st = (String) tableModel.getValueAt(row, 3);
                    c.setBackground(switch (st) {
                        case "NEW"         -> new Color(220, 235, 255);
                        case "PENDING"     -> new Color(255, 245, 200);
                        case "IN_PROGRESS" -> new Color(255, 250, 180);
                        case "RESOLVED"    -> new Color(210, 255, 210);
                        case "CLOSED"      -> new Color(220, 220, 220);
                        case "ESCALATED"   -> new Color(255, 210, 210);
                        case "ON_HOLD"     -> new Color(240, 220, 255);
                        default            -> Color.WHITE;
                    });
                }
                return c;
            }
        });

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(800, 220));

        detailArea = new JTextArea(10, 60);
        detailArea.setEditable(false);
        detailArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane detailScroll = new JScrollPane(detailArea);
        detailScroll.setBorder(BorderFactory.createTitledBorder("Ticket Details & History"));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, detailScroll);
        split.setResizeWeight(0.55);

        // Bottom action bar (only useful for IT Staff)
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionBox = new JComboBox<>(new String[]{"START","RESOLVE","CLOSE","ESCALATE","HOLD","REOPEN"});
        JButton applyBtn   = new JButton("Apply Action");
        JButton refreshBtn = new JButton("Refresh");
        bottomBar.add(new JLabel("Change Status:"));
        bottomBar.add(actionBox);
        bottomBar.add(applyBtn);
        bottomBar.add(refreshBtn);

        boolean isStaff = controller.getLoggedInUser() != null &&
                          controller.getLoggedInUser().getRole().equals("IT Staff");
        bottomBar.setVisible(isStaff);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showDetail();
        });
        applyBtn.addActionListener(e -> applyAction());
        refreshBtn.addActionListener(e -> refresh());

        add(split,     BorderLayout.CENTER);
        add(bottomBar, BorderLayout.SOUTH);
    }

    private void showDetail() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        String id = (String) tableModel.getValueAt(row, 0);
        Ticket t  = getTicketById(id);
        if (t == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Ticket ID    : ").append(t.getTicketId()).append("\n");
        sb.append("Title        : ").append(t.getTitle()).append("\n");
        sb.append("Description  : ").append(t.getDescription()).append("\n");
        sb.append("Priority     : ").append(t.getPriority()).append("\n");
        sb.append("Status       : ").append(t.getStatus()).append("\n");
        sb.append("Component    : ").append(t.getAffectedComponent()).append("\n");
        sb.append("Submitted By : ").append(t.getSubmittedBy().getName()).append("\n");
        sb.append("Created At   : ").append(t.getCreatedAt()).append("\n");
        sb.append("Assigned To  : ").append(
            t.getAssignedTo() != null ? t.getAssignedTo().getName() : "Not yet assigned").append("\n");
        if (t.getFeedback() != null)
            sb.append("Feedback     : ").append(t.getFeedback()).append("\n");
        sb.append("\n--- Notification History ---\n");
        t.getNotifications().forEach(n -> sb.append("  ").append(n).append("\n"));
        detailArea.setText(sb.toString());
        detailArea.setCaretPosition(0);
    }

    private void applyAction() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a ticket from the table first.",
                "No Ticket Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id     = (String) tableModel.getValueAt(row, 0);
        String action = (String) actionBox.getSelectedItem();
        try {
            controller.updateStatus(id, action);
            refresh();
            JOptionPane.showMessageDialog(this,
                "Action '" + action + "' applied to ticket " + id,
                "Done", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(), "Cannot Apply Action", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refresh() {
        tableModel.setRowCount(0);
        List<Ticket> list = controller.getTicketsByCurrentUser();
        for (Ticket t : list) {
            tableModel.addRow(new Object[]{
                t.getTicketId(),
                t.getTitle(),
                t.getPriority(),
                t.getStatus().name(),
                t.getSubmittedBy().getName(),
                t.getAssignedTo() != null ? t.getAssignedTo().getName() : "Unassigned"
            });
        }
        detailArea.setText("");
    }

    private Ticket getTicketById(String id) {
        return controller.getAllTickets().stream()
            .filter(t -> t.getTicketId().equals(id))
            .findFirst().orElse(null);
    }
}