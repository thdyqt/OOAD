package VIEW;

import BLL.UserManager;
import DTO.User;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private RoundedButton btnLogin;
    private RoundedButton btnGoToRegister;

    private final Color COLOR_PRIMARY = new Color(0, 86, 179);
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_TEXT_DARK = new Color(50, 50, 50);
    private final Color COLOR_BORDER = new Color(210, 215, 220);

    private UserManager userBLL = new UserManager();

    public Login() {
        setTitle("Đăng nhập hệ thống Calendar");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(COLOR_BG);
        panelHeader.setBorder(new EmptyBorder(30, 0, 10, 0));

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_PRIMARY);
        panelHeader.add(lblTitle);

        add(panelHeader, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COLOR_BG);
        panelForm.setBorder(new EmptyBorder(10, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 15);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEmail.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblEmail, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        txtEmail = new JTextField();
        styleTextField(txtEmail);
        panelForm.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPassword.setForeground(COLOR_TEXT_DARK);
        panelForm.add(lblPassword, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        txtPassword = new JPasswordField();
        styleTextField(txtPassword);
        panelForm.add(txtPassword, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        panelFooter.setBackground(COLOR_BG);

        btnLogin = new RoundedButton("Đăng nhập", 12, COLOR_PRIMARY, 1);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setBackground(COLOR_PRIMARY);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(150, 45));

        btnGoToRegister = new RoundedButton("Chưa có tài khoản?", 12, COLOR_BORDER, 1);
        btnGoToRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGoToRegister.setBackground(Color.WHITE);
        btnGoToRegister.setForeground(COLOR_PRIMARY);
        btnGoToRegister.setPreferredSize(new Dimension(170, 45));

        panelFooter.add(btnLogin);
        panelFooter.add(btnGoToRegister);

        add(panelFooter, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> handleLogin());
        btnGoToRegister.addActionListener(e -> {
            new Register().setVisible(true);
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

    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Email và Mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User loggedInUser = userBLL.login(email, password);

        if (loggedInUser != null) {
           new CalendarUI(loggedInUser).setVisible(true);
           this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Email hoặc Mật khẩu không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ex) {}
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}