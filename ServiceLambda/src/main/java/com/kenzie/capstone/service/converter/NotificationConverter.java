package com.kenzie.capstone.service.converter;

import com.kenzie.capstone.service.model.*;


public class NotificationConverter {

    public static NotificationRecord fromRequestToRecord(NotificationRequest event) {
        NotificationRecord record = new NotificationRecord();
        record.setEventId(event.getEventId());
        record.setCustomerName(event.getCustomerName());
        record.setCustomerEmail(event.getCustomerEmail());
        record.setDate(event.getDate());
        record.setStatus(event.getStatus());
        return record;
    }

    public static NotificationResponse fromRecordToResponse(NotificationRecord record) {
        NotificationResponse eventResponse = new NotificationResponse();
        eventResponse.setEventId(record.getEventId());
        eventResponse.setCustomerName(record.getCustomerName());
        eventResponse.setCustomerEmail(record.getCustomerEmail());
        eventResponse.setDate(record.getDate());
        eventResponse.setStatus(record.getStatus());
        return eventResponse;
    }

}
