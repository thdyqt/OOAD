package VIEW;

import BLL.ReminderManager;
import DTO.Appointment;
import DTO.Reminder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReminderDialog extends JDialog {
    private JComboBox<String> comboTime;
    private JTextField txtMessage;
    private RoundedButton btnSave;
    private RoundedButton btnSkip;

    private Appointment currentApt;
    private Reminder reminderToEdit;

    private final Color COLOR_PRIMARY = new Color(0, 86, 179);
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_TEXT_DARK = new Color(50, 50, 50);
    private final Color COLOR_BORDER = new Color(210, 215, 220);

    public ReminderDialog(Window parent, boolean modal, Appointment apt, Reminder reminderToEdit) {
        super(parent, modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
        this.currentApt = apt;
        this.reminderToEdit = reminderToEdit;

        setTitle(reminderToEdit == null ? "Thiết lập Nhắc nhở" : "Chỉnh sửa Nhắc nhở");
        setSize(450, 420);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout());
        initComponents();

        if (reminderToEdit != null) {
            fillData();
        }
    }

    private void initComponents() {
        JPanel panelHeader = new JPanel();
        panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
        panelHeader.setBackground(COLOR_PRIMARY);
        panelHeader.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel lblTitle = new JLabel("THIẾT LẬP NHẮC NHỞ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblApptName = new JLabel(currentApt.getName());
        lblApptName.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblApptName.setForeground(new Color(220, 235, 255));
        lblApptName.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelHeader.add(lblTitle);
        panelHeader.add(Box.createVerticalStrut(5));
        panelHeader.add(lblApptName);

        add(panelHeader, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COLOR_BG);
        panelForm.setBorder(new EmptyBorder(25, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblTime = new JLabel("Báo trước thời gian:");
        lblTime.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTime.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblTime, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 15, 0);
        String[] timeOptions = {
            "Tại thời điểm bắt đầu",
            "Trước 10 phút",
            "Trước 30 phút",
            "Trước 1 giờ",
            "Trước 1 ngày"
        };
        comboTime = new JComboBox<>(timeOptions);
        comboTime.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comboTime.setPreferredSize(new Dimension(200, 40));
        comboTime.setBackground(Color.WHITE);
        panelForm.add(comboTime, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(10, 0, 15, 0);
        JLabel lblMessage = new JLabel("Nội dung lời nhắc:");
        lblMessage.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMessage.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblMessage, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 15, 0);
        txtMessage = new JTextField();
        txtMessage.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtMessage.setPreferredSize(new Dimension(200, 40));
        txtMessage.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panelForm.add(txtMessage, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelFooter.setBackground(COLOR_BG);
        panelFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER));

        btnSkip = new RoundedButton("Không báo", 12, COLOR_BORDER, 1);
        btnSkip.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSkip.setBackground(Color.WHITE);
        btnSkip.setForeground(new Color(100, 100, 100));
        btnSkip.setPreferredSize(new Dimension(120, 40));

        btnSave = new RoundedButton("Lưu Nhắc Nhở", 12, COLOR_PRIMARY, 1);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(COLOR_PRIMARY);
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(140, 40));

        panelFooter.add(btnSkip);
        panelFooter.add(btnSave);

        add(panelFooter, BorderLayout.SOUTH);

        btnSkip.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> handleSaveReminder());
    }

    private void fillData() {
        txtMessage.setText(reminderToEdit.getMessage());
        btnSave.setText("Cập nhật");
    }

    private void handleSaveReminder() {
        int selectedIndex = comboTime.getSelectedIndex();
        LocalDateTime targetTime = currentApt.getStartTime();
        String type = "AT_START";

        switch (selectedIndex) {
            case 1:
                targetTime = targetTime.minusMinutes(10);
                type = "10_MIN_BEFORE";
                break;
            case 2:
                targetTime = targetTime.minusMinutes(30);
                type = "30_MIN_BEFORE";
                break;
            case 3:
                targetTime = targetTime.minusHours(1);
                type = "1_HOUR_BEFORE";
                break;
            case 4:
                targetTime = targetTime.minusDays(1);
                type = "1_DAY_BEFORE";
                break;
        }

        String msg = txtMessage.getText().trim();

        if (reminderToEdit != null) {
            reminderToEdit.setReminderType(type);
            reminderToEdit.setTargetTime(targetTime);
            reminderToEdit.setMessage(msg);

            String result = ReminderManager.updateReminder(reminderToEdit);
            if (result.equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Cập nhật nhắc nhở thành công!");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, result, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            Reminder reminder = new Reminder(currentApt.getAppointmentId(), type, targetTime, msg);
            String result = ReminderManager.addReminder(reminder);

            if (result.equals("SUCCESS")) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
                JOptionPane.showMessageDialog(this, "Đã thiết lập nhắc nhở thành công vào lúc:\n" + targetTime.format(dtf));
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, result, "Lỗi Nhắc nhở", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}