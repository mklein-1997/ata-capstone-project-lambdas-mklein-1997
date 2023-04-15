package com.kenzie.capstone.service.model;

import java.util.Objects;


public class Notification {
    private String eventId;
    private String date;
    private String customerEmail;

    public Notification(String eventId, String email, String date) {
        this.eventId = eventId;
        this.customerEmail = email;
        this.date = date;
    }

    public Notification(){}

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventDate() {
        return date;
    }

    public void setEventDate(String eventDate) {
        this.date = eventDate;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification event = (Notification) o;
        return Objects.equals(eventId, event.eventId) && Objects.equals(date, event.date) && Objects.equals(customerEmail, event.customerEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, date, customerEmail);
    }

    @Override
    public String toString(){
        return "EventResponse{" +
                "eventId='" + eventId + '\'' +
                ", date='" + date + '\'' +
                ", customerEmail=" + customerEmail +
                '}';
    }
}