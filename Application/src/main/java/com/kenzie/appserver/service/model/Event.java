package com.kenzie.appserver.service.model;

import java.util.Objects;

public class Event {
    private final String eventId;
    private String customerName;
    private String eventDate;
    private String eventStatus;
    private String customerEmail;

    public Event(String eventId, String name, String email) {
        this.eventId = eventId;
        this.customerName = name;
        this.customerEmail = email;
    }

    public Event(String eventId, String name, String email, String date, String status) {
        this.eventId = eventId;
        this.customerName = name;
        this.customerEmail = email;
        this.eventDate = date;
        this.eventStatus = status;
    }

    public String getEventId() {
        return eventId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String email) {
        this.customerEmail = email;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventId, event.eventId) && Objects.equals(customerName, event.customerName) && Objects.equals(eventDate, event.eventDate) && Objects.equals(eventStatus, event.eventStatus) && Objects.equals(customerEmail, event.customerEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, customerName, eventDate, eventStatus, customerEmail);
    }
}
