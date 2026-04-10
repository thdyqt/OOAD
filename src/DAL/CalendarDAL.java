/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import DTO.Calendar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class CalendarDAL {
     public static List<Calendar> getCalendarsByUserId(int userId) {
        List<DTO.Calendar> list = new ArrayList<>();
        String sql = "SELECT * FROM Calendars WHERE owner_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                list.add(new DTO.Calendar(
                    rs.getInt("calendar_id"),
                    rs.getInt("owner_id"),
                    rs.getString("name"),
                    rs.getString("time_zone")
                ));
            }
        } catch (SQLException e) {}
        return list;
    }
}
