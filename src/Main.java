import controller.TicketController;
import view.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            TicketController controller = new TicketController();
            MainFrame frame = new MainFrame(controller);
            frame.setVisible(true);
        });
    }
}