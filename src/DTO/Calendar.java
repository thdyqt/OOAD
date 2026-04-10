/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Admin
 */
public class Calendar {
    private int calendarId;
    private int ownerId;
    private String name;
    private String timezone;
    
    public Calendar() {}

    public Calendar(int ownerId, String name, String timezone) {
        this.ownerId = ownerId;
        this.name = name;
        this.timezone = timezone;
    }
        
    public Calendar(int calendarId, int ownerId, String name, String timezone) {
        this.calendarId = calendarId;
        this.ownerId = ownerId;
        this.name = name;
        this.timezone = timezone;
    }

    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
