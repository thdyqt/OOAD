package VIEW;

import BLL.AppointmentManager;
import DTO.Appointment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;

public class EditAppointmentWindow extends JDialog {
    private JTextField txtName, txtLocation;
    private JSpinner spinStartHour, spinStartMinute, spinEndHour, spinEndMinute;
    private JCheckBox chkGroup;
    private RoundedButton btnSave, btnCancel;

    private Appointment currentApt;

    private final Color COLOR_PRIMARY = new Color(0, 86, 179);
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_BORDER = new Color(210, 215, 220);

    public EditAppointmentWindow(Frame parent, boolean modal, Appointment apt) {
        super(parent, modal);
        this.currentApt = apt;

        setTitle("Sửa Cuộc Hẹn");
        setSize(480, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout());

        initComponents();
        loadDataToForm();
    }

    private void initComponents() {
        // --- HEADER --- (Bạn có thể tái sử dụng thiết kế của AddAppointmentWindow ở đây để đồng bộ giao diện)
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setBorder(new EmptyBorder(25, 0, 25, 0));
        JLabel lblTitle = new JLabel("SỬA THÔNG TIN CUỘC HẸN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        panelHeader.add(lblTitle);
        add(panelHeader, BorderLayout.NORTH);

        // --- FORM ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COLOR_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(12, 10, 12, 10);

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Tên cuộc hẹn:"), gbc);
        gbc.gridx = 1; txtName = new JTextField(); txtName.setPreferredSize(new Dimension(200, 35)); panelForm.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Địa điểm:"), gbc);
        gbc.gridx = 1; txtLocation = new JTextField(); txtLocation.setPreferredSize(new Dimension(200, 35)); panelForm.add(txtLocation, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Giờ bắt đầu:"), gbc);
        gbc.gridx = 1;
        JPanel pnlStart = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); pnlStart.setBackground(COLOR_BG);
        spinStartHour = new JSpinner(new SpinnerNumberModel(8, 0, 23, 1));
        spinStartMinute = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        pnlStart.add(spinStartHour); pnlStart.add(new JLabel(":")); pnlStart.add(spinStartMinute);
        panelForm.add(pnlStart, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(new JLabel("Giờ kết thúc:"), gbc);
        gbc.gridx = 1;
        JPanel pnlEnd = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); pnlEnd.setBackground(COLOR_BG);
        spinEndHour = new JSpinner(new SpinnerNumberModel(9, 0, 23, 1));
        spinEndMinute = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        pnlEnd.add(spinEndHour); pnlEnd.add(new JLabel(":")); pnlEnd.add(spinEndMinute);
        panelForm.add(pnlEnd, gbc);

        gbc.gridx = 1; gbc.gridy = 4; chkGroup = new JCheckBox("Đây là buổi họp nhóm"); chkGroup.setBackground(COLOR_BG);
        panelForm.add(chkGroup, gbc);

        add(panelForm, BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        panelFooter.setBackground(COLOR_BG);
        btnCancel = new RoundedButton("Hủy bỏ", 12, COLOR_BORDER, 1); btnCancel.setBackground(Color.WHITE);
        btnSave = new RoundedButton("Cập Nhật", 12, COLOR_PRIMARY, 1); btnSave.setBackground(COLOR_PRIMARY); btnSave.setForeground(Color.WHITE);
        panelFooter.add(btnCancel); panelFooter.add(btnSave);
        add(panelFooter, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> handleUpdate());
    }

    private void loadDataToForm() {
        txtName.setText(currentApt.getName());
        txtLocation.setText(currentApt.getLocation());
        chkGroup.setSelected(currentApt.isGroupMeeting());
        spinStartHour.setValue(currentApt.getStartTime().getHour());
        spinStartMinute.setValue(currentApt.getStartTime().getMinute());
        spinEndHour.setValue(currentApt.getEndTime().getHour());
        spinEndMinute.setValue(currentApt.getEndTime().getMinute());
    }

    private void handleUpdate() {
        currentApt.setName(txtName.getText().trim());
        currentApt.setLocation(txtLocation.getText().trim());
        currentApt.setIsGroupMeeting(chkGroup.isSelected());

        LocalDateTime start = currentApt.getStartTime().toLocalDate().atTime((int)spinStartHour.getValue(), (int)spinStartMinute.getValue());
        LocalDateTime end = currentApt.getEndTime().toLocalDate().atTime((int)spinEndHour.getValue(), (int)spinEndMinute.getValue());

        currentApt.setStartTime(start);
        currentApt.setEndTime(end);

        String result = AppointmentManager.updateAppointment(currentApt);
        if ("SUCCESS".equals(result)) {
            JOptionPane.showMessageDialog(this, "Cập nhật cuộc hẹn thành công!");
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}