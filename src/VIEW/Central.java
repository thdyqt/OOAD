package VIEW;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import DTO.Reminder;
import BLL.ReminderManager;

public class Central extends JFrame {
    private JPanel listContainer;

    public void start() {
        setTitle("Trung Tâm Thông Báo");
        setSize(500, 700);
        setLayout(new BorderLayout(15, 15));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(HeaderPanel(), BorderLayout.NORTH);
        add(ReminderListArea(), BorderLayout.CENTER);
        add(FooterButtons(), BorderLayout.SOUTH);

        refreshReminders();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // --- Component Methods ---

    private JComponent HeaderPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Thông báo trong 24h tới"));
        panel.setBackground(new Color(240, 240, 240));
        return panel;
    }

    private JComponent ReminderListArea() {
        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        return new JScrollPane(listContainer);
    }

    private JComponent FooterButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> refreshReminders());
        panel.add(btnRefresh);
        return panel;
    }

    // --- Helper Functions ---

    private void refreshReminders() {
        listContainer.removeAll();
        // Assuming you add a BLL method for the 24-hour filter
        List<Reminder> list = ReminderManager.getReminder_24H();

        for (Reminder r : list) {
            listContainer.add(createReminderItem(r));
        }

        listContainer.revalidate();
        listContainer.repaint();
    }

    private JPanel createReminderItem(Reminder r) {
        // This is a "Mini-Component" for a single reminder row
        JPanel item = new JPanel(new BorderLayout());
        item.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        item.add(new JLabel(r.getMessage()), BorderLayout.CENTER);
        item.add(new JLabel(r.getTargetTime().toString()), BorderLayout.EAST);
        return item;
    }

    public static void main(String[] args) {
        new Central().start();
    }
}