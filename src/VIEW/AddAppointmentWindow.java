package VIEW;

import DTO.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddAppointmentWindow extends JDialog {

    private JTextField txtName;
    private JTextField txtLocation;
    private JSpinner spinStartHour;
    private JSpinner spinStartMinute;
    private JSpinner spinEndHour;
    private JSpinner spinEndMinute;
    private JCheckBox chkGroup;
    private RoundedButton btnSave;
    private RoundedButton btnCancel;

    private User currentUser;
    private LocalDate selectedDate;

    private final Color COLOR_PRIMARY = new Color(0, 86, 179);
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_TEXT_DARK = new Color(50, 50, 50);
    private final Color COLOR_BORDER = new Color(210, 215, 220);

    public AddAppointmentWindow(Frame parent, boolean modal, User user, LocalDate date) {
        super(parent, modal);
        this.currentUser = user;
        this.selectedDate = date;

        setTitle("Thêm Cuộc Hẹn");
        setSize(480, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        JPanel panelHeader = new JPanel();
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setBorder(new EmptyBorder(25, 0, 25, 0));

        JLabel lblTitle = new JLabel("THÊM CUỘC HẸN MỚI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        JLabel lblDate = new JLabel("Ngày: " + selectedDate.format(formatter));
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblDate.setForeground(new Color(220, 235, 255));
        lblDate.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelHeader.add(lblTitle);
        panelHeader.add(Box.createVerticalStrut(8));
        panelHeader.add(lblDate);

        add(panelHeader, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COLOR_BG);
        panelForm.setBorder(new EmptyBorder(25, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 12, 15);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        JLabel lblName = new JLabel("Tên cuộc hẹn:");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblName, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.65;
        txtName = new JTextField();
        styleTextField(txtName);
        panelForm.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblLocation = new JLabel("Địa điểm:");
        lblLocation.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblLocation.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblLocation, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        txtLocation = new JTextField();
        styleTextField(txtLocation);
        panelForm.add(txtLocation, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblStart = new JLabel("Giờ bắt đầu:");
        lblStart.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStart.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblStart, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JPanel panelStartTime = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelStartTime.setBackground(COLOR_BG);
        spinStartHour = new JSpinner(new SpinnerNumberModel(8, 0, 23, 1));
        spinStartMinute = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));
        styleSpinner(spinStartHour);
        styleSpinner(spinStartMinute);
        panelStartTime.add(spinStartHour);
        JLabel sep1 = new JLabel(" : ");
        sep1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelStartTime.add(sep1);
        panelStartTime.add(spinStartMinute);
        panelForm.add(panelStartTime, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblEnd = new JLabel("Giờ kết thúc:");
        lblEnd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEnd.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblEnd, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        JPanel panelEndTime = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelEndTime.setBackground(COLOR_BG);
        spinEndHour = new JSpinner(new SpinnerNumberModel(9, 0, 23, 1));
        spinEndMinute = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));
        styleSpinner(spinEndHour);
        styleSpinner(spinEndMinute);
        panelEndTime.add(spinEndHour);
        JLabel sep2 = new JLabel(" : ");
        sep2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelEndTime.add(sep2);
        panelEndTime.add(spinEndMinute);
        panelForm.add(panelEndTime, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.insets = new Insets(5, 0, 10, 15);
        chkGroup = new JCheckBox("Đây là buổi họp nhóm");
        chkGroup.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        chkGroup.setBackground(COLOR_BG);
        chkGroup.setForeground(COLOR_TEXT_DARK);
        chkGroup.setFocusPainted(false);
        chkGroup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelForm.add(chkGroup, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        panelFooter.setBackground(COLOR_BG);
        panelFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER));

        btnCancel = new RoundedButton("Hủy bỏ", 12, COLOR_BORDER, 1);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(Color.WHITE);
        btnCancel.setForeground(new Color(100, 100, 100));
        btnCancel.setPreferredSize(new Dimension(120, 45));

        btnSave = new RoundedButton("Lưu Cuộc Hẹn", 12, COLOR_PRIMARY, 1);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(COLOR_PRIMARY);
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(150, 45));

        panelFooter.add(btnCancel);
        panelFooter.add(btnSave);

        add(panelFooter, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> handleSave());
    }

    private void styleTextField(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setPreferredSize(new Dimension(200, 40));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setPreferredSize(new Dimension(75, 28));
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField txt = ((JSpinner.DefaultEditor) editor).getTextField();
            txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            txt.setHorizontalAlignment(SwingConstants.CENTER);
            txt.setBorder(BorderFactory.createEmptyBorder());
        }
    }
    
    public int getDuration(int startH, int startM, int endH, int endM) {
        int startTimeMinutes = startH * 60 + startM;
        int endTimeMinutes = endH * 60 + endM;
        return endTimeMinutes - startTimeMinutes;
    }
    
    private void handleSave() {
        String name = txtName.getText().trim();
        String location = txtLocation.getText().trim();
        int startH = (int) spinStartHour.getValue();
        int startM = (int) spinStartMinute.getValue();
        int endH = (int) spinEndHour.getValue();
        int endM = (int) spinEndMinute.getValue();
        boolean isGroup = chkGroup.isSelected();
        
        if (name.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (getDuration(startH, startM, endH, endM) < 0) {
            JOptionPane.showMessageDialog(rootPane, "Thời gian kết thúc phải sau thời gian bắt đầu!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        
    }
}