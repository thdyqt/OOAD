package DTO;

import java.time.LocalDateTime;

public class Appointment {
    //attributes
    private int appointmentId;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    //methods
    //constructors
    public Appointment() {}

    public Appointment(int appointmentId, String name, LocalDateTime startTime, LocalDateTime endTime) {
        this.appointmentId = appointmentId;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    //get-set
    public int getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
}
