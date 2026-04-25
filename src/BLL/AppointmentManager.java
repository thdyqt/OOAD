/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BLL;

import DAL.AppointmentDAL;
import DTO.Appointment;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Admin
 */
public class AppointmentManager {
    public static String addAppointment(Appointment apt) {
        if (apt.getName() == null || apt.getName().trim().isEmpty()) {
            return "Tên cuộc hẹn không được để trống!";
        }
        if (apt.getLocation() == null || apt.getLocation().trim().isEmpty()) {
            return "Địa điểm không được để trống!";
        }

        if (apt.getStartTime().isAfter(apt.getEndTime()) || apt.getStartTime().isEqual(apt.getEndTime())) {
            return "Thời gian kết thúc phải diễn ra sau thời gian bắt đầu!";
        }

        boolean isSuccess = AppointmentDAL.insertAppointment(apt);
        
        if (isSuccess) {
            return "SUCCESS";
        } else {
            return "Lỗi hệ thống: Không thể lưu cuộc hẹn vào cơ sở dữ liệu.";
        }
    }
    public static List<Appointment> getUpcomingAppointments(int userId) {
        return AppointmentDAL.getUpcomingAppointments(userId);
    }

    public static boolean deleteAppointment(int appointmentId) {
        return AppointmentDAL.deleteAppointment(appointmentId);
    }

    public static String updateAppointment(Appointment apt) {
        if (apt.getName() == null || apt.getName().trim().isEmpty()) {
            return "Tên cuộc hẹn không được để trống!";
        }
        if (apt.getStartTime().isAfter(apt.getEndTime()) || apt.getStartTime().isEqual(apt.getEndTime())) {
            return "Thời gian kết thúc phải diễn ra sau thời gian bắt đầu!";
        }

        boolean isSuccess = AppointmentDAL.updateAppointment(apt);
        return isSuccess ? "SUCCESS" : "Lỗi hệ thống: Không thể cập nhật cuộc hẹn.";
    }

    public static Appointment checkGroupMeeting(int calendarId, String name, LocalDateTime startTime, LocalDateTime endTime){
        for (Appointment ap : AppointmentDAL.getAppointmentsByCalendarId(calendarId)) {
            if (ap.getName().equals(name) && ap.getStartTime() == startTime && ap.getEndTime() == endTime && ap.isGroupMeeting()) {
                return ap;
            }
        }
        return null;
    }

    public static boolean joinExistingMeeting(int appointmentId, int userId) {
        return AppointmentDAL.addMeetingParticipant(appointmentId, userId);
    }

    public static Appointment checkTimeConflict(int calendarId, LocalDateTime startTime, LocalDateTime endTime) {
        for (Appointment ap : AppointmentDAL.getAppointmentsByCalendarId(calendarId)) {
            if (startTime.isBefore(ap.getEndTime()) && endTime.isAfter(ap.getStartTime())) {
                return ap;
            }
        }
        return null;
    }

    public static boolean replaceAppointment(int appointmentId, Appointment newAppointment) {
        newAppointment.setAppointmentId(appointmentId);
        return AppointmentDAL.updateAppointment(newAppointment);
    }
}
