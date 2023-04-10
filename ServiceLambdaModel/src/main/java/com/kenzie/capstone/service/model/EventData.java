package com.kenzie.capstone.service.model;


import java.util.Objects;


public class EventData {
    private String eventId;
    private String customerName;
    private String date;
    private String eventStatus;
    private String customerEmail;
    public EventData(String eventId, String name, String email, String date, String status) {
        this.eventId = eventId;
        this.customerName = name;
        this.customerEmail = email;
        this.date = date;
        this.eventStatus = status;
    }

    public EventData(){}

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEventDate() {
        return date;
    }

    public void setEventDate(String eventDate) {
        this.date = eventDate;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
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
        EventData event = (EventData) o;
        return Objects.equals(eventId, event.eventId) && Objects.equals(customerName, event.customerName) && Objects.equals(date, event.date) && Objects.equals(eventStatus, event.eventStatus) && Objects.equals(customerEmail, event.customerEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, customerName, date, eventStatus, customerEmail);
    }

    @Override
    public String toString(){
        return "EventResponse{" +
                "eventId='" + eventId + '\'' +
                ", date='" + date + '\'' +
                ", status=" + eventStatus + '\'' +
                ", customerName=" + customerName + '\'' +
                ", customerEmail=" + customerEmail +
                '}';
    }
}
