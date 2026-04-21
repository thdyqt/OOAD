package VIEW;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import DTO.Reminder;
import BLL.ReminderManager;

public class Central extends JPanel {
    private DefaultTableModel tableModel;
    private JTable reminderTable;
    private int current_UserID;

    public Central(int userId) {
        this.current_UserID = userId;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE); // Make it blend with the main app

        add(HeaderPanel(), BorderLayout.NORTH);
        add(ReminderListArea(), BorderLayout.CENTER);
        add(RefreshButton(), BorderLayout.SOUTH);

        refreshReminders();
    }

    //title
    private JComponent HeaderPanel() {
        JPanel panel = new JPanel();
        JLabel title = new JLabel("Thông báo trong 24h tới");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(0, 86, 179));
        panel.add(title);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return panel;
    }

    //holder of content
    private JComponent ReminderListArea() {
        String[] columns = {"ID Cuộc Hẹn", "Loại Nhắc Nhở", "Nội Dung", "Thời Gian Phải Báo"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reminderTable = new JTable(tableModel);

        // 4. Styling the table to look clean
        reminderTable.setRowHeight(35);
        reminderTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reminderTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        reminderTable.getTableHeader().setBackground(new Color(240, 240, 240));
        reminderTable.setSelectionBackground(new Color(190, 220, 255)); // Highlight color

        JScrollPane scrollPane = new JScrollPane(reminderTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        return scrollPane;
    }

    //refresh
    private JComponent RefreshButton() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> refreshReminders());
        panel.add(btnRefresh);
        return panel;
    }

    //helpers
    //actually put the contents in there
    public void refreshReminders() {
        tableModel.setRowCount(0);

        List<Reminder> list = ReminderManager.getReminder_24H(this.current_UserID);

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");

        for (Reminder r : list) {
            tableModel.addRow(new Object[]{
                    r.getAppointmentId(),
                    r.getReminderType(),
                    r.getMessage(),
                    r.getTargetTime().format(formatter)
            });
        }
    }

    private JPanel createReminderItem(Reminder r) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        item.add(new JLabel("<html><b>" + r.getReminderType() + ":</b> " + r.getMessage() + "</html>"), BorderLayout.CENTER);

        JLabel timeLabel = new JLabel(r.getTargetTime().toString());
        timeLabel.setForeground(Color.RED);
        item.add(timeLabel, BorderLayout.EAST);

        return item;
    }
}