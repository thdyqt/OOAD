package DTO;

import java.time.LocalDateTime;

public class Reminder {
    //attributes
    public enum ReminderType{
        H24, H12, H1, M30, M10
    }

    private int reminderId;
    private int appointmentId;
    private ReminderType reminderType;
    private LocalDateTime targetTime;
    private String message; 

    //methods
    //constructors
    public Reminder() {}
    
    public Reminder(int reminderId, int appointmentId, ReminderType reminderType, LocalDateTime targetTime, String message) {
        this.reminderId = reminderId;
        this.appointmentId = appointmentId;
        this.reminderType = reminderType;
        this.targetTime = targetTime;
        this.message = message;
    }

    //get-set
    public int getReminderId() {
        return reminderId;
    }
    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(int appointmentId) {this.appointmentId = appointmentId;}

    public ReminderType getReminderType() {
        return reminderType;
    }
    public void setReminderType(ReminderType reminderType) {
        this.reminderType = reminderType;
    }

    public LocalDateTime getTargetTime() {
        return targetTime;
    }
    public void setTargetTime(LocalDateTime targetTime) {
        this.targetTime = targetTime;
    }

    public String getMessage() {return message;}
    public void setMessage(String message) {
        this.message = message;
    }
}


