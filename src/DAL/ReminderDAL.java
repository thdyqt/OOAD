package DAL;

import DTO.Reminder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReminderDAL {  
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

    //for central
    //get all reminders whose remaining time before target_time is 24 hours or less
    public static List<Reminder> getAllReminder_24Hours(int userId) {
        java.util.List<Reminder> list = new java.util.ArrayList<>();

        String sql = "SELECT r.* FROM Reminders r\n" +
                "JOIN Appointments a ON r.appointment_id = a.appointment_id\n" +
                "JOIN Calendars c ON a.calendar_id = c.calendar_id\n" +
                "WHERE c.owner_id = ? \n" +
                "AND r.target_time BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 1 DAY) " +
                "ORDER BY r.target_time ASC";


        try (Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            //pass user id into the query
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
}