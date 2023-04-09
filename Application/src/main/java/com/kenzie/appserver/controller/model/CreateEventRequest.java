package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

public class CreateEventRequest {
    @NotEmpty
    @JsonProperty("eventId")
    private String eventId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'")
    @JsonProperty("date")
    private String date;
    @NotEmpty
    @JsonProperty("customerName")
    private String customerName;

    @NotEmpty
    @JsonProperty("customerEmail")
    private String customerEmail;

    public CreateEventRequest() {}

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public String getEventId() {
        return eventId;
    }
    public String getDate() {
        return LocalDate.now().toString();
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    public String getCustomerEmail() {
        return customerEmail;
    }


}
