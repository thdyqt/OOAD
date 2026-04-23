package VIEW;

import BLL.ReminderManager;
import DTO.Reminder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Central extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private int current_UserID;

    private final Color COLOR_PRIMARY = new Color(0, 86, 179);
    private final Color COLOR_BORDER = new Color(226, 232, 240);

    public Central(int userId) {
        this.current_UserID = userId;
        initComponents();
        refreshReminders();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("Trung tâm Thông báo (24h tới)", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"ID Cuộc Hẹn", "Loại Nhắc Nhở", "Nội Dung Lời Nhắc", "Thời Gian Phải Báo"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setOpaque(true);

        table.setSelectionBackground(new Color(190, 220, 255));
        table.setSelectionForeground(Color.BLACK);

        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(COLOR_BORDER);
        // -------------------------

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshReminders() {
        tableModel.setRowCount(0);

        List<Reminder> list = ReminderManager.getReminder_24H(this.current_UserID);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");

        for (Reminder r : list) {
            String typeStr = r.getReminderType();
            switch (typeStr) {
                case "AT_START": typeStr = "Tại lúc bắt đầu"; break;
                case "10_MIN_BEFORE": typeStr = "Trước 10 phút"; break;
                case "30_MIN_BEFORE": typeStr = "Trước 30 phút"; break;
                case "1_HOUR_BEFORE": typeStr = "Trước 1 giờ"; break;
                case "1_DAY_BEFORE": typeStr = "Trước 1 ngày"; break;
                default: break;
            }

            tableModel.addRow(new Object[]{
                    r.getAppointmentId(),
                    typeStr,
                    r.getMessage(),
                    r.getTargetTime().format(formatter)
            });
        }
    }
}