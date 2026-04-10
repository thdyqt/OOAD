/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class Appointment {
    private int appointmentId;
    private int calendarId;
    private String name;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isGroupMeeting;
    
    public Appointment() {}
    
    public Appointment(int appointmentId, int calendarId, String name, String location, LocalDateTime startTime, LocalDateTime endTime, boolean isGroupMeeting) {
        this.appointmentId = appointmentId;
        this.calendarId = calendarId;
        this.name = name;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isGroupMeeting = isGroupMeeting;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isIsGroupMeeting() {
        return isGroupMeeting;
    }

    public void setIsGroupMeeting(boolean isGroupMeeting) {
        this.isGroupMeeting = isGroupMeeting;
    }

}
