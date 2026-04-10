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
public class Reminder {
    private int reminderId;
    private int appointmentId;
    private String reminderType;
    private LocalDateTime targetTime;
    private String message; 
   
    public Reminder() {}

    public Reminder(int reminderId, int appointmentId, String reminderType, LocalDateTime targetTime, String message) {
        this.reminderId = reminderId;
        this.appointmentId = appointmentId;
        this.reminderType = reminderType;
        this.targetTime = targetTime;
        this.message = message;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public LocalDateTime getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(LocalDateTime targetTime) {
        this.targetTime = targetTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}


