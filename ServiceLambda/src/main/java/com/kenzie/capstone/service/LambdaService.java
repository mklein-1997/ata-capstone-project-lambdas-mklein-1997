package com.kenzie.capstone.service;

import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.dao.EventDao;
import com.kenzie.capstone.service.model.EventData;
import com.kenzie.capstone.service.model.EventRecord;

import javax.inject.Inject;

import java.util.List;
import java.util.UUID;

public class LambdaService {

    private EventDao eventDao;

    @Inject
    public LambdaService(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public EventData getEventData(String eventId) {
        List<EventRecord> records = eventDao.getEventData(eventId);
        if (records.size() > 0) {
            return new EventData(records.get(0).getEventId(), records.get(0).getData());
        }
        return null;
    }

    public EventData setEventData(String data) {
        String eventId = UUID.randomUUID().toString();
        //changed id to eventId
        EventRecord eventRecord = eventDao.setEventData(eventId, data);
        return new EventData(eventId, data);
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

            EventRecord record = new EventRecord();
            record.setEventId(eventId);

            boolean deleted = eventDao.deleteEventData(record);

            if(!deleted){
                allDeleted = false;
            }
        }
        return allDeleted;
    }
}
