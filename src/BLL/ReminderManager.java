/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BLL;

import DAL.DBConnection;
import DAL.ReminderDAL;
import DTO.Reminder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ReminderManager {
    public static String addReminder(Reminder reminder) {
        if (reminder.getAppointmentId() <= 0) {
            return "Lỗi: Không xác định được Cuộc hẹn để tạo nhắc nhở!";
        }

        if (reminder.getTargetTime() == null) {
            return "Lỗi: Thời gian nhắc nhở không được để trống!";
        }

        if (reminder.getTargetTime().isBefore(LocalDateTime.now())) {
            return "Thời gian nhắc nhở không hợp lệ vì đã trôi qua!";
        }

        boolean isSuccess = ReminderDAL.insertReminder(reminder);

        if (isSuccess) {
            return "SUCCESS";
        } else {
            return "Lỗi hệ thống: Không thể lưu nhắc nhở vào cơ sở dữ liệu.";
        }
    }

    public static List<Reminder> getReminder_24H(){
        return ReminderDAL.getAllReminder_24Hours();
    }

}
