package com.kenzie.capstone.service.model;

public class NotificationRequest {
    private String eventId;

    private String date;

    private String customerEmail;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
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
