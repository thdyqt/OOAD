package DAL;

import DTO.Reminder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReminderDAL {
    public static List<Reminder> getRemindersByCalendar_24H(int calendarId, int userId) {
        List<Reminder> list = new ArrayList<>();
        String sql = "SELECT DISTINCT r.* FROM Reminders r " +
                "JOIN Appointments a ON r.appointment_id = a.appointment_id " +
                "LEFT JOIN Meeting_Participants mp ON a.appointment_id = mp.appointment_id " +
                "WHERE (a.calendar_id = ? OR mp.user_id = ?) AND r.user_id = ? " +
                "AND r.target_time BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 1 DAY) " +
                "ORDER BY r.target_time ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, calendarId);
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Reminder(
                            rs.getInt("reminder_id"),
                            rs.getInt("appointment_id"),
                            rs.getInt("user_id"),
                            rs.getString("reminder_type"),
                            rs.getTimestamp("target_time").toLocalDateTime(),
                            rs.getString("message")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean insertReminder(Reminder reminder) {
        String sql = "INSERT INTO Reminders (appointment_id, user_id, reminder_type, target_time, message) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reminder.getAppointmentId());
            stmt.setInt(2, reminder.getUserId());
            stmt.setString(3, reminder.getReminderType());
            stmt.setTimestamp(4, Timestamp.valueOf(reminder.getTargetTime()));
            stmt.setString(5, reminder.getMessage());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean updateReminder(Reminder reminder) {
        String sql = "UPDATE Reminders SET reminder_type = ?, target_time = ?, message = ? WHERE reminder_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reminder.getReminderType());
            stmt.setTimestamp(2, Timestamp.valueOf(reminder.getTargetTime()));
            stmt.setString(3, reminder.getMessage());
            stmt.setInt(4, reminder.getReminderId());
            stmt.setInt(5, reminder.getUserId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean deleteReminder(int reminderId, int userId) {
        String sql = "DELETE FROM Reminders WHERE reminder_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reminderId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean deleteRemindersByAppointmentId(int appointmentId, int userId) {
        String sql = "DELETE FROM Reminders WHERE appointment_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }
}