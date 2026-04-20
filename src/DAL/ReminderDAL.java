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

    //get all reminders whose remaining time before target_time is 24 hours or less
    public static List<Reminder> getAllReminder_24Hours(){
        List<Reminder> results = new ArrayList<>();
        String sql = "SELECT * FROM Reminders WHERE target_time >= NOW() " +
                "AND target_time <= DATE_ADD(NOW(), INTERVAL 1 DAY) " +
                "ORDER BY target_time ASC";

        try
                (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery())
        {
            while (rs.next()){
                Reminder reminder = new Reminder(
                        rs.getInt("reminder_id"),
                        rs.getInt("appointment_id"),
                        rs.getString("reminder_type"),
                        rs.getTimestamp("target_time").toLocalDateTime(),
                        rs.getString("message")
                );
                results.add(reminder);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return results;
    }
}