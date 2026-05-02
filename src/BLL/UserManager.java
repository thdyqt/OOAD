package BLL;

import DAL.UserDAL;
import DTO.User;

import java.util.List;

public class UserManager {
    public static User login(String username, String password) {
        return UserDAL.checkLogin(username, password);
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

    public static List<User> getMeetingParticipants(int appointmentId) {
        return UserDAL.getParticipantsByAppointmentId(appointmentId);
    }
}