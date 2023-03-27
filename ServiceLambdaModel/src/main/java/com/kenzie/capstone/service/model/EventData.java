package com.kenzie.capstone.service.model;

import java.util.Objects;

public class EventData {
    private String eventId;
    private String data;


    public EventData(String eventId, String data) {
        this.eventId = eventId;
        this.data = data;
    }

    public EventData() {}

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventData that = (EventData) o;
        return Objects.equals(eventId, that.eventId) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, data);
    }
}
