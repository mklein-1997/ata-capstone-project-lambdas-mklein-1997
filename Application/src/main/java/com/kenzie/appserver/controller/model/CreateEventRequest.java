package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

public class CreateEventRequest {
    //assumed all of these are NotEmpty, but may cause problems
    @NotEmpty
    @JsonProperty("eventId")
    private String eventId;

    @NotEmpty
    @JsonProperty("date")
    private Optional<String> date;

    @NotEmpty
    @JsonProperty("status")
    private Optional<String> status;

    @NotEmpty
    @JsonProperty("customerName")
    private Optional<String> customerName;

    @NotEmpty
    @JsonProperty("customerEmail")
    private Optional<String> customerEmail;

    public CreateEventRequest() {}

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Optional<String> getDate() {
        return date;
    }

    public void setDate(Optional<String> date) {
        this.date = date;
    }

    public Optional<String> getStatus() {
        return status;
    }

    public void setStatus(Optional<String> status) {
        this.status = status;
    }

    public Optional<String> getCustomerName() {
        return customerName;
    }

    public void setCustomerName(Optional<String> customerName) {
        this.customerName = customerName;
    }

    public Optional<String> getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(Optional<String> customerEmail) {
        this.customerEmail = customerEmail;
    }
}
