package VIEW;

import BLL.UserManager;
import DAL.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Register extends JFrame {

    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;
    private JButton btnBack;

    public Register() {
        setTitle("Đăng ký tài khoản");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("ĐĂNG KÝ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel panelCenter = new JPanel(new GridLayout(4, 2, 10, 10));
        panelCenter.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panelCenter.add(new JLabel("Họ tên:"));
        txtUsername = new JTextField();
        panelCenter.add(txtUsername);

        panelCenter.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelCenter.add(txtEmail);

        panelCenter.add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        panelCenter.add(txtPassword);

        panelCenter.add(new JLabel("Xác nhận MK:"));
        txtConfirmPassword = new JPasswordField();
        panelCenter.add(txtConfirmPassword);

        add(panelCenter, BorderLayout.CENTER);

        JPanel panelBottom = new JPanel();
        btnRegister = new JButton("Đăng ký");
        btnBack = new JButton("Quay lại Đăng nhập");

        panelBottom.add(btnRegister);
        panelBottom.add(btnBack);
        add(panelBottom, BorderLayout.SOUTH);

        btnRegister.addActionListener(e -> handleRegister());
        btnBack.addActionListener(e -> {
            new Login().setVisible(true);
            this.dispose();
        });
    }

    private UserManager userBLL = new UserManager();

    private void handleRegister() {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirmPassword.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
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

    // Tạo 1 lịch mặc định cho user mới đăng ký để liên kết bảng Calendars
    private void createCalendarForUser(Connection conn, int userId) {
        try {
            String sql = "INSERT INTO Calendars (owner_id) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException ignored) {}
    }
}