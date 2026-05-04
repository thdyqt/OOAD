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
    //attributes
    private String timezone;

    //methods
    //constructors
    public Calendar() {}

    public Calendar(String timezone) {
        this.timezone = timezone;
    }

    //get-set
    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
