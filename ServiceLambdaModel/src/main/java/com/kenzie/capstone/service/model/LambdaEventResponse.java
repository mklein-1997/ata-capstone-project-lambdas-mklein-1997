package com.kenzie.capstone.service.model;

public class LambdaEventResponse {

    private String eventId;

    private String date;

    private String status;

    private String customerName;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Override
    public String toString(){
        return "EventResponse{" +
                "eventId='" + eventId + '\'' +
                ", date='" + date + '\'' +
                ", status=" + status + '\'' +
                ", customerName=" + customerName + '\'' +
                ", customerEmail=" + customerEmail +
                '}';
    }
}
