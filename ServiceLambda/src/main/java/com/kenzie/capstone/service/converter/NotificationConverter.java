package com.kenzie.capstone.service.converter;

import com.kenzie.capstone.service.model.*;


public class NotificationConverter {

    public static NotificationRecord fromRequestToRecord(NotificationRequest event) {
        NotificationRecord record = new NotificationRecord();
        record.setEventId(event.getEventId());
        record.setCustomerEmail(event.getCustomerEmail());
        record.setDate(event.getDate());
        return record;
    }

    public static NotificationResponse fromRecordToResponse(NotificationRecord record) {
        NotificationResponse eventResponse = new NotificationResponse();
        eventResponse.setEventId(record.getEventId());
        eventResponse.setCustomerEmail(record.getCustomerEmail());
        eventResponse.setDate(record.getDate());
        return eventResponse;
    }

}
