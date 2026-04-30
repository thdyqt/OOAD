package BLL;

import DAL.UserDAL;
import DTO.User;

public class UserManager {
    public static User login(String name, String password) {
        return UserDAL.checkLogin(name, password);
    }

    public static String register(String username, String name, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return "Mật khẩu xác nhận không khớp!";
        }

        if (UserDAL.isUserExist(username)) {
            return "Username này đã được sử dụng!";
        }

        User newUser = new User(username, name, password);
        boolean success = UserDAL.insertUser(newUser);

        return success ? "SUCCESS" : "Đã xảy ra lỗi khi đăng ký vào hệ thống!";
    }
}