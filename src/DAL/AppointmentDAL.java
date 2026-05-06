/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import BLL.DBConnection;
import DTO.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class AppointmentDAL {
    public static List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM Appointments";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment apt = new Appointment();
                    apt.setAppointmentId(rs.getInt("appointment_id"));
                    apt.setName(rs.getString("name"));
                    apt.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                    apt.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                    
                    list.add(apt); 
                }
            }
        } catch (SQLException e) {}
        return list;
    }

    public static Appointment getAppointmentById(int appointmentId) {
        Appointment apt = null;
        String sql = "SELECT * FROM Appointments WHERE appointment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appointmentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    apt = new Appointment();
                    apt.setAppointmentId(rs.getInt("appointment_id"));
                    apt.setName(rs.getString("name"));
                    apt.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                    apt.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apt;
    }


    public static int insertAppointment(Appointment apt) {
        String sql = "INSERT INTO Appointments ( name, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, apt.getName());
            stmt.setTimestamp(2, Timestamp.valueOf(apt.getStartTime()));
            stmt.setTimestamp(3, Timestamp.valueOf(apt.getEndTime()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);

                        apt.setAppointmentId(newId);

                        return newId;
                    }
                }
            }
            return -1;
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    
    public static boolean updateAppointment(Appointment appt) {
        String sql = "UPDATE Appointments SET name = ?, start_time = ?, end_time = ? WHERE appointment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, appt.getName());
            stmt.setTimestamp(2, Timestamp.valueOf(appt.getStartTime()));
            stmt.setTimestamp(3, Timestamp.valueOf(appt.getEndTime()));
            stmt.setInt(4, appt.getAppointmentId());
            
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


    public static List<Appointment> getUpcomingAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT DISTINCT a.* FROM Appointments a " +
                "WHERE a.end_time >= NOW() " +
                "ORDER BY a.start_time ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment apt = new Appointment();
                    apt.setAppointmentId(rs.getInt("appointment_id"));
                    apt.setName(rs.getString("name"));
                    apt.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                    apt.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                    list.add(apt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
