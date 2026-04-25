package VIEW;

import BLL.AppointmentManager;
import BLL.ReminderManager;
import DTO.Reminder;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReminderListPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Reminder> currentList;
    private int current_UserID;

    private final Color COLOR_PRIMARY = new Color(0, 86, 179);
    private final Color COLOR_BORDER = new Color(226, 232, 240);

    public ReminderListPanel(int userId) {
        this.current_UserID = userId;
        initComponents();
        refreshReminders();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Trung tâm Thông báo (24h tới)", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Cấu hình bảng
        String[] columns = {"ID", "Nội dung", "Thời điểm báo", "Loại", "Cuộc hẹn"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        // Căn giữa Header và Cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getTableHeader().setDefaultRenderer(centerRenderer);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Ẩn cột ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        // Footer với các nút to, đẹp và căn giữa
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        panelFooter.setBackground(Color.WHITE);

        RoundedButton btnEdit = new RoundedButton("✎ Chỉnh Sửa", 15, new Color(0, 86, 179), 1);
        btnEdit.setBackground(new Color(0, 86, 179));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setPreferredSize(new Dimension(160, 45));

        RoundedButton btnDelete = new RoundedButton("✖ Xóa Bỏ", 15, new Color(220, 53, 69), 1);
        btnDelete.setBackground(new Color(220, 53, 69));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setPreferredSize(new Dimension(160, 45));

        panelFooter.add(btnEdit);
        panelFooter.add(btnDelete);
        add(panelFooter, BorderLayout.SOUTH);

        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());

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

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một thông báo để sửa!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Reminder selectedReminder = currentList.get(row);
        DTO.Appointment currentApt = AppointmentManager.getAppointmentById(selectedReminder.getAppointmentId());
        if (currentApt == null) {
            JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy Cuộc hẹn liên kết với thông báo này!", "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ReminderDialog dialog = new ReminderDialog(SwingUtilities.getWindowAncestor(this), true, currentApt, selectedReminder);
        dialog.setVisible(true);

        refreshReminders();
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int reminderId = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa thông báo này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (ReminderManager.deleteReminder(reminderId)) {
                refreshReminders();
            }
        }
    }
}