/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import DTO.Appointment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class AppointmentDAL {
    public static List<Appointment> getAppointmentsByCalendarId(int calendarId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM Appointments WHERE calendar_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, calendarId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment apt = new Appointment();
                    apt.setAppointmentId(rs.getInt("appointment_id"));
                    apt.setCalendarId(rs.getInt("calendar_id"));
                    apt.setName(rs.getString("name"));
                    apt.setLocation(rs.getString("location"));
                    apt.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                    apt.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                    apt.setIsGroupMeeting(rs.getBoolean("is_group_meeting"));
                    
                    list.add(apt); 
                }
            }
        } catch (SQLException e) {}
        return list;
    }
    
    public static boolean insertAppointment(Appointment apt) {
        String sql = "INSERT INTO Appointments (calendar_id, name, location, start_time, end_time, is_group_meeting) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = DBConnection.getConnection(); 
                PreparedStatement stmt = con.prepareStatement(sql)){
            
            stmt.setInt(1, apt.getCalendarId());
            stmt.setString(2, apt.getName());
            stmt.setString(3, apt.getLocation());
            stmt.setTimestamp(4, Timestamp.valueOf(apt.getStartTime()));
            stmt.setTimestamp(5, Timestamp.valueOf(apt.getEndTime()));
            stmt.setBoolean(6, apt.isGroupMeeting());
            
            return stmt.executeUpdate() > 0;
        } catch (Exception e){
            return false;
        }
    }
    
    public static boolean updateAppointment(Appointment appt) {
        String sql = "UPDATE Appointments SET name = ?, location = ?, start_time = ?, end_time = ?, is_group_meeting = ? WHERE appointment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, appt.getName());
            stmt.setString(2, appt.getLocation());
            stmt.setTimestamp(3, Timestamp.valueOf(appt.getStartTime()));
            stmt.setTimestamp(4, Timestamp.valueOf(appt.getEndTime()));
            stmt.setBoolean(5, appt.isGroupMeeting());    
            stmt.setInt(6, appt.getAppointmentId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            return false;
        }
    }
    
    public static boolean deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM Appointments WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, appointmentId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            return false;
        }
    }
}
