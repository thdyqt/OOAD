package BLL;

import DAL.CalendarDAL;
import DTO.Calendar;

import java.util.List;

public class CalendarManager {
    public static String addCalendar(int userId, String name) {
        if (name == null || name.trim().isEmpty()) return "Tên lịch không được để trống!";

        Calendar cal = new Calendar();
        cal.setOwnerId(userId);
        cal.setName(name);

        return CalendarDAL.insertCalendar(cal) ? "SUCCESS" : "Lỗi khi tạo lịch mới.";
    }

    public static String updateCalendarName(int calendarId, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            return "Tên lịch không được để trống!";
        }
        return DAL.CalendarDAL.updateCalendarName(calendarId, newName) ? "SUCCESS" : "Lỗi khi đổi tên lịch.";
    }

    public static String deleteCalendar(int userId, int calendarId) {
        List<Calendar> userCalendars = CalendarDAL.getCalendarsByUserId(userId);

        if (userCalendars.size() <= 1) {
            return "Bạn không thể xóa tờ lịch cuối cùng!";
        }

        return CalendarDAL.deleteCalendar(calendarId) ? "SUCCESS" : "Lỗi khi xóa lịch.";
    }
}
