package VIEW;

import BLL.AppointmentManager;
import DTO.Appointment;
import DTO.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentListPanel extends JPanel {
    private final RoundedButton btnEdit;
    private final RoundedButton btnDelete;
    private JTable table;
    private DefaultTableModel tableModel;
    private User currentUser;
    private List<Appointment> currentList;

    public AppointmentListPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Danh sách Cuộc hẹn Sắp tới", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 86, 179));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Khởi tạo Bảng (Table)
        String[] columns = {"ID", "Tên cuộc hẹn", "Địa điểm", "Bắt đầu", "Kết thúc", "Phân loại"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Không cho sửa trực tiếp trên ô
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(240, 240, 240));

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(190, 220, 255));
        table.setSelectionForeground(new Color(33, 37, 41));
        table.setShowVerticalLines(false);

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        panelFooter.setBackground(Color.WHITE);

        btnEdit = new RoundedButton("Chỉnh Sửa", 15, new Color(0, 86, 179), 1);
        btnEdit.setBackground(new Color(0, 86, 179)); // THÊM DÒNG NÀY ĐỂ TÔ MÀU NỀN
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setPreferredSize(new Dimension(160, 45));

        btnDelete = new RoundedButton("Xóa Bỏ", 15, new Color(220, 53, 69), 1);
        btnDelete.setBackground(new Color(220, 53, 69)); // THÊM DÒNG NÀY ĐỂ TÔ MÀU NỀN
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setPreferredSize(new Dimension(160, 45));

        panelFooter.add(btnEdit);
        panelFooter.add(btnDelete);

        add(panelFooter, BorderLayout.SOUTH);

        btnDelete.addActionListener(e -> handleDelete());
        btnEdit.addActionListener(e -> handleEdit());

        loadData();
    }

    public void loadData() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        currentList = AppointmentManager.getUpcomingAppointments(currentUser.getUserId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        for (Appointment apt : currentList) {
            Object[] row = {
                    apt.getAppointmentId(),
                    apt.getName(),
                    apt.getLocation(),
                    apt.getStartTime().format(formatter),
                    apt.getEndTime().format(formatter),
                    apt.isGroupMeeting() ? "Họp nhóm" : "Cá nhân"
            };
            tableModel.addRow(row);
        }
    }

    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một cuộc hẹn để xóa!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int aptId = (int) tableModel.getValueAt(selectedRow, 0);
        String aptName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa cuộc hẹn: '" + aptName + "'?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = AppointmentManager.deleteAppointment(aptId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEdit() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một cuộc hẹn để sửa!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Appointment selectedApt = currentList.get(selectedRow);
        AppointmentDialog editForm = new AppointmentDialog((Frame) SwingUtilities.getWindowAncestor(this), true, currentUser, null, 0, selectedApt);
        editForm.setVisible(true);
        loadData();
    }
}