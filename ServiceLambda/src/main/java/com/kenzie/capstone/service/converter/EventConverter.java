package com.kenzie.capstone.service.converter;

import com.kenzie.capstone.service.model.Event;
import com.kenzie.capstone.service.model.EventRecord;
import com.kenzie.capstone.service.model.EventRequest;
import com.kenzie.capstone.service.model.EventResponse;



public class EventConverter {

    public static EventRecord fromRequestToRecord(EventRequest event) {
        EventRecord record = new EventRecord();
        record.setEventId(event.getEventId());
        record.setCustomerName(event.getCustomerName());
        record.setCustomerEmail(event.getCustomerEmail());
        record.setDate(event.getDate());
        record.setStatus(event.getStatus());
        return record;
    }

    public static EventResponse fromRecordToResponse(EventRecord record) {
        EventResponse eventResponse = new EventResponse();
        eventResponse.setEventId(record.getEventId());
        eventResponse.setCustomerName(record.getCustomerName());
        eventResponse.setCustomerEmail(record.getCustomerEmail());
        eventResponse.setDate(record.getDate());
        eventResponse.setStatus(record.getStatus());
        return eventResponse;
    }

    public static Event fromRecordToReferral(EventRecord record) {
        Event event = new Event();
        event.setEventId(record.getEventId());
        event.setEventDate(record.getDate());
        event.setEventStatus(record.getStatus());
        event.setCustomerEmail(record.getCustomerEmail());
        event.setCustomerName(record.getCustomerName());
        return event;
    }
}
