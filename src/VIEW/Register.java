package VIEW;

import BLL.UserManager;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Register extends JFrame {

    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private RoundedButton btnRegister;
    private RoundedButton btnBack;

    // Định nghĩa các màu sắc đồng bộ với hệ thống
    private final Color COLOR_PRIMARY = new Color(0, 86, 179);
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_TEXT_DARK = new Color(50, 50, 50);
    private final Color COLOR_BORDER = new Color(210, 215, 220);

    private UserManager userBLL = new UserManager();

    public Register() {
        setTitle("Đăng ký tài khoản");
        setSize(480, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        // --- HEADER ---
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(COLOR_BG);
        panelHeader.setBorder(new EmptyBorder(25, 0, 10, 0));

        JLabel lblTitle = new JLabel("ĐĂNG KÝ TÀI KHOẢN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_PRIMARY);
        panelHeader.add(lblTitle);

        add(panelHeader, BorderLayout.NORTH);

        // --- FORM CENTER ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COLOR_BG);
        panelForm.setBorder(new EmptyBorder(10, 40, 10, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 15);

        // Họ tên
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        JLabel lblName = new JLabel("Họ tên:");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblName, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.65;
        txtUsername = new JTextField();
        styleTextField(txtUsername);
        panelForm.add(txtUsername, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEmail.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblEmail, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        txtEmail = new JTextField();
        styleTextField(txtEmail);
        panelForm.add(txtEmail, gbc);

        // Mật khẩu
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblPassword, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        txtPassword = new JPasswordField();
        styleTextField(txtPassword);
        panelForm.add(txtPassword, gbc);

        // Xác nhận MK
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblConfirm = new JLabel("Xác nhận MK:");
        lblConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblConfirm.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblConfirm, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        txtConfirmPassword = new JPasswordField();
        styleTextField(txtConfirmPassword);
        panelForm.add(txtConfirmPassword, gbc);

        add(panelForm, BorderLayout.CENTER);

        // --- FOOTER BUTTONS ---
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        panelFooter.setBackground(COLOR_BG);

        btnBack = new RoundedButton("Quay lại", 12, COLOR_BORDER, 1);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(new Color(100, 100, 100));
        btnBack.setPreferredSize(new Dimension(130, 45));

        btnRegister = new RoundedButton("Đăng ký", 12, COLOR_PRIMARY, 1);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRegister.setBackground(COLOR_PRIMARY);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setPreferredSize(new Dimension(150, 45));

        panelFooter.add(btnBack);
        panelFooter.add(btnRegister);

        add(panelFooter, BorderLayout.SOUTH);

        // Events
        btnRegister.addActionListener(e -> handleRegister());
        btnBack.addActionListener(e -> {
            new Login().setVisible(true);
            this.dispose();
        });
    }

    private void styleTextField(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setPreferredSize(new Dimension(200, 40));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void handleRegister() {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirmPassword.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String result = userBLL.register(username, email, password, confirm);

        if (result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập.");
            new Login().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Giữ nguyên phương thức này như code cũ của bạn
    private void createCalendarForUser(Connection conn, int userId) {
        try {
            String sql = "INSERT INTO Calendars (owner_id) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException ignored) {}
    }
}