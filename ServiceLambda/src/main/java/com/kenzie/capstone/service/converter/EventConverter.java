package com.kenzie.capstone.service.converter;

import com.kenzie.capstone.service.model.*;


public class EventConverter {

    public static LambdaEventRecord fromRequestToRecord(LambdaEventRequest event) {
        LambdaEventRecord record = new LambdaEventRecord();
        record.setEventId(event.getEventId());
        record.setCustomerName(event.getCustomerName());
        record.setCustomerEmail(event.getCustomerEmail());
        record.setDate(event.getDate());
        record.setStatus(event.getStatus());
        return record;
    }

    public static LambdaEventResponse fromRecordToResponse(LambdaEventRecord record) {
        LambdaEventResponse eventResponse = new LambdaEventResponse();
        eventResponse.setEventId(record.getEventId());
        eventResponse.setCustomerName(record.getCustomerName());
        eventResponse.setCustomerEmail(record.getCustomerEmail());
        eventResponse.setDate(record.getDate());
        eventResponse.setStatus(record.getStatus());
        return eventResponse;
    }

}
