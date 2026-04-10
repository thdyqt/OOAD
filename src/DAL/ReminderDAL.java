package DAL;

import DTO.Reminder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

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
}