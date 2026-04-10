package VIEW;

import BLL.UserManager;
import DAL.DBConnection;
import DTO.User;

import java.awt.*;
import javax.swing.*;

public class Login extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnGoToRegister;

    public Login() {
        setTitle("Đăng nhập hệ thống Calendar");
        setSize(600, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);


        JPanel panelCenter = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCenter.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panelCenter.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelCenter.add(txtEmail);

        panelCenter.add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        panelCenter.add(txtPassword);

        add(panelCenter, BorderLayout.CENTER);


        JPanel panelBottom = new JPanel();
        btnLogin = new JButton("Đăng nhập");
        btnGoToRegister = new JButton("Chưa có tài khoản? Đăng ký ngay");

        panelBottom.add(btnLogin);
        panelBottom.add(btnGoToRegister);
        add(panelBottom, BorderLayout.SOUTH);


        btnLogin.addActionListener(e -> handleLogin());
        btnGoToRegister.addActionListener(e -> {
            new Register().setVisible(true);
            this.dispose();
        });
    }
    private UserManager userBLL = new UserManager();

    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Email và Mật khẩu!");
            return;
        }

        User loggedInUser = userBLL.login(email, password);

        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Xin chào " + loggedInUser.getUsername());
        } else {
            JOptionPane.showMessageDialog(this, "Email hoặc Mật khẩu không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}