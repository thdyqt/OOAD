package VIEW;

import BLL.AppointmentManager;
import DTO.Appointment;
import DTO.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MeetingDetailDialog extends JDialog {
    private Appointment currentApt;
    private final Color COLOR_PRIMARY = new Color(0, 86, 179);
    private final Color COLOR_BG = Color.WHITE;

    public MeetingDetailDialog(Window parent, boolean modal, Appointment apt) {
        super(parent, modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
        this.currentApt = apt;

        setTitle("Chi Tiết Cuộc Hẹn");
        setSize(700, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        // --- HEADER ---
        JPanel panelHeader = new JPanel();
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel lblTitle = new JLabel(currentApt.getName());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblType = new JLabel(currentApt.isGroupMeeting() ? "(Họp Nhóm)" : "(Cuộc Hẹn Cá Nhân)");
        lblType.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblType.setForeground(new Color(220, 235, 255));
        lblType.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelHeader.add(lblTitle);
        panelHeader.add(Box.createVerticalStrut(5));
        panelHeader.add(lblType);
        add(panelHeader, BorderLayout.NORTH);

        // --- CENTER INFO ---
        JPanel panelCenter = new JPanel(new BorderLayout());
        panelCenter.setBackground(COLOR_BG);
        panelCenter.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel panelInfo = new JPanel(new GridLayout(3, 1, 0, 10));
        panelInfo.setBackground(COLOR_BG);
        panelInfo.setBorder(new EmptyBorder(0, 0, 20, 0));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");
        JLabel lblLoc = new JLabel("Địa điểm: " + currentApt.getLocation());
        JLabel lblStart = new JLabel("Bắt đầu: " + currentApt.getStartTime().format(dtf));
        JLabel lblEnd = new JLabel("Kết thúc: " + currentApt.getEndTime().format(dtf));

        Font fontInfo = new Font("Segoe UI", Font.PLAIN, 16);
        lblLoc.setFont(fontInfo); lblStart.setFont(fontInfo); lblEnd.setFont(fontInfo);

        panelInfo.add(lblLoc); panelInfo.add(lblStart); panelInfo.add(lblEnd);
        panelCenter.add(panelInfo, BorderLayout.NORTH);

        if (currentApt.isGroupMeeting()) {
            JPanel panelTable = new JPanel(new BorderLayout());
            panelTable.setBackground(COLOR_BG);

            JLabel lblParticipants = new JLabel("Danh sách người tham gia:");
            lblParticipants.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblParticipants.setBorder(new EmptyBorder(0, 0, 10, 0));
            panelTable.add(lblParticipants, BorderLayout.NORTH);

            String[] cols = {"ID", "Tên thành viên", "Username", "Thời gian tham gia"};
            DefaultTableModel tableModel = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int row, int col) { return false; }
            };
            JTable table = new JTable(tableModel);

            table.setRowHeight(35);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
            table.getTableHeader().setBackground(new Color(240, 240, 240));

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 1; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

            table.setGridColor(new Color(230, 230, 230));
            table.setSelectionBackground(new Color(190, 220, 255));
            table.setSelectionForeground(new Color(33, 37, 41));
            table.setShowVerticalLines(false);

            table.getColumnModel().getColumn(0).setMinWidth(0);
            table.getColumnModel().getColumn(0).setMaxWidth(0);
            table.getColumnModel().getColumn(0).setWidth(0);

            DateTimeFormatter dtfJoin = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");
            List<User> participants = AppointmentManager.getMeetingParticipants(currentApt.getAppointmentId());
            for (User u : participants) {
                String joinTimeStr = (u.getJoinedAt() != null) ? u.getJoinedAt().format(dtfJoin) : "Không xác định";
                tableModel.addRow(new Object[]{u.getUserId(), u.getName(), u.getUsername(), joinTimeStr});
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
            panelTable.add(scrollPane, BorderLayout.CENTER);

            panelCenter.add(panelTable, BorderLayout.CENTER);
        } else {
            JLabel lblNoParticipants = new JLabel("Đây là cuộc hẹn cá nhân, không có người tham gia khác.");
            lblNoParticipants.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblNoParticipants.setForeground(Color.GRAY);
            panelCenter.add(lblNoParticipants, BorderLayout.CENTER);
        }

        add(panelCenter, BorderLayout.CENTER);

        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelFooter.setBackground(COLOR_BG);
        panelFooter.setBorder(new EmptyBorder(10, 0, 20, 0));

        RoundedButton btnClose = new RoundedButton("Đóng", 15, COLOR_PRIMARY, 1);
        btnClose.setBackground(COLOR_PRIMARY);
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnClose.setPreferredSize(new Dimension(120, 40));
        btnClose.addActionListener(e -> dispose());

        panelFooter.add(btnClose);
        add(panelFooter, BorderLayout.SOUTH);
    }
}