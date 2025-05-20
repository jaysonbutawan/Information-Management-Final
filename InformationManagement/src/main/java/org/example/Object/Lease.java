package org.example.Object;

import java.time.LocalDate;

public class Lease {
    private int leaseID;
    private int user_id;
    private int property_id;
    private LocalDate start_date;
    private LocalDate due_date;
    private String status;

    public Lease() {
    }

    public Lease(int leaseID, int user_id, int property_id, LocalDate start_date, LocalDate due_date, String status) {
        this.leaseID = leaseID;
        this.user_id = user_id;
        this.property_id = property_id;
        this.start_date = start_date;
        this.due_date = due_date;
        this.status = status;
    }

    public int getLeaseID() {
        return leaseID;
    }

    public void setLeaseID(int leaseID) {
        this.leaseID = leaseID;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProperty_id() {
        return property_id;
    }

    public void setProperty_id(int property_id) {
        this.property_id = property_id;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
