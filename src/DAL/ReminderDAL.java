package DAL;

import DTO.Reminder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReminderDAL {
    public static List<Reminder> getAllReminder_24Hours(int userId) {
        List<Reminder> list = new ArrayList<>();

        String sql = "SELECT r.* FROM Reminders r\n" +
                "JOIN Appointments a ON r.appointment_id = a.appointment_id\n" +
                "JOIN Calendars c ON a.calendar_id = c.calendar_id\n" +
                "WHERE c.owner_id = ? \n" +
                "AND r.target_time BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 1 DAY) " +
                "ORDER BY r.target_time ASC";


        try (Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Reminder(
                            rs.getInt("reminder_id"),
                            rs.getInt("appointment_id"),
                            rs.getString("reminder_type"),
                            rs.getTimestamp("target_time").toLocalDateTime(),
                            rs.getString("message")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean insertReminder(Reminder reminder) {
        String sql = "INSERT INTO Reminders (appointment_id, reminder_type, target_time, message) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, reminder.getAppointmentId());
            stmt.setString(2, reminder.getReminderType());
            stmt.setTimestamp(3, Timestamp.valueOf(reminder.getTargetTime())); 
            stmt.setString(4, reminder.getMessage());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean updateReminder(Reminder reminder) {
        String sql = "UPDATE Reminders SET reminder_type = ?, target_time = ?, message = ? WHERE reminder_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reminder.getReminderType());
            stmt.setTimestamp(2, Timestamp.valueOf(reminder.getTargetTime()));
            stmt.setString(3, reminder.getMessage());
            stmt.setInt(4, reminder.getReminderId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteReminder(int reminderId) {
        String sql = "DELETE FROM Reminders WHERE reminder_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reminderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}