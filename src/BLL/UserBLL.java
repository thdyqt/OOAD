package BLL;

import DAL.UserDAL;
import DTO.User;

public class UserBLL {
    private UserDAL userDAL = new UserDAL();

    public User login(String email, String password) {
        // Có thể thêm logic mã hóa password (MD5, SHA, Bcrypt) ở đây trước khi truyền xuống DAL
        return userDAL.checkLogin(email, password);
    }

    public String register(String username, String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return "Mật khẩu xác nhận không khớp!";
        }
        if (userDAL.isEmailExist(email)) {
            return "Email này đã được đăng ký!";
        }

        User newUser = new User(0, username, email, password);
        boolean success = userDAL.insertUser(newUser);

        return success ? "SUCCESS" : "Đã xảy ra lỗi khi đăng ký vào hệ thống!";
    }
}