package DAL;

import DTO.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAL {
    public static User checkLogin(String user, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {}
        return null;
    }

    public static boolean isUserExist(String user) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {e.printStackTrace();}
        return false;
    }

    public static boolean insertUser(User user) {
        String sql = "INSERT INTO Users (username, name, password) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getPassword());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    CalendarDAL.createDefaultCalendar(conn, keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {e.printStackTrace();}
        return false;
    }

    public static List<User> getParticipantsByAppointmentId(int appointmentId) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.*, mp.joined_at FROM Users u " +
                "JOIN Meeting_Participants mp ON u.user_id = mp.user_id " +
                "WHERE mp.appointment_id = ? " +
                "ORDER BY mp.joined_at ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User u = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        ""
                );

                Timestamp ts = rs.getTimestamp("joined_at");
                if (ts != null) {
                    u.setJoinedAt(ts.toLocalDateTime());
                }
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}