package com.kenzie.capstone.service;

import com.kenzie.capstone.service.converter.EventConverter;
import com.kenzie.capstone.service.dao.EventDao;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.EventData;
import com.kenzie.capstone.service.model.LambdaEventRecord;
import com.kenzie.capstone.service.model.LambdaEventRequest;
import com.kenzie.capstone.service.model.LambdaEventResponse;

import javax.inject.Inject;

import java.util.List;


public class LambdaService {
    //original LambdaService file https://tinyurl.com/LambdaService

    private final EventDao eventDao;

    @Inject
    public LambdaService(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public LambdaEventResponse addEvent(LambdaEventRequest event) {
        if (event == null || event.getEventId() == null || event.getCustomerName().length() == 0) {
            throw new InvalidDataException("Request must contain a valid Customer Name");
        }
        LambdaEventRecord record = EventConverter.fromRequestToRecord(event);
        eventDao.addEvent(record);
        return EventConverter.fromRecordToResponse(record);
    }

    public EventData getEventData(String eventId) {
        List<LambdaEventRecord> records = eventDao.findByEventId(eventId);
        if (records.size() > 0) {
            return new EventData(records.get(0).getEventId(), records.get(0).getCustomerName(),
                    records.get(0).getCustomerEmail(), records.get(0).getDate(), records.get(0).getStatus());
        }
        return null;
    }

    public LambdaEventResponse updateEvent(LambdaEventRequest event) {
        if (event == null || event.getEventId() == null || event.getCustomerName().length() == 0) {
            throw new InvalidDataException("Request must contain a valid Customer Name");
        }
        LambdaEventRecord record = EventConverter.fromRequestToRecord(event);
        eventDao.updateEvent(record);
        return EventConverter.fromRecordToResponse(record);
    }

    public Boolean deleteEventData(List<String> eventIds) {
        boolean allDeleted = true;

        if(eventIds == null){
            throw new InvalidDataException("Request must contain a valid list of Event Id's");
        }

        for(String eventId : eventIds){
            if(eventId == null || eventId.length() == 0){
                throw new InvalidDataException("Event ID cannot be null or empty to delete");
            }

            LambdaEventRecord record = new LambdaEventRecord();
            record.setEventId(eventId);

            boolean deleted = eventDao.deleteEvent(record);

            if(!deleted){
                allDeleted = false;
            }
        }
        return allDeleted;
    }
}
