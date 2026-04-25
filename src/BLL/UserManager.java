package BLL;

import DAL.UserDAL;
import DTO.User;

public class UserManager {
    public static User login(String email, String password) {
        return UserDAL.checkLogin(email, password);
    }

    public static String register(String username, String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return "Mật khẩu xác nhận không khớp!";
        }

        if (UserDAL.isEmailExist(email)) {
            return "Email này đã được đăng ký!";
        }

        if (UserDAL.isUserExist(username)) {
            return "Username này đã được sử dụng!";
        }

        User newUser = new User(username, email, password);
        boolean success = UserDAL.insertUser(newUser);

        return success ? "SUCCESS" : "Đã xảy ra lỗi khi đăng ký vào hệ thống!";
    }
}