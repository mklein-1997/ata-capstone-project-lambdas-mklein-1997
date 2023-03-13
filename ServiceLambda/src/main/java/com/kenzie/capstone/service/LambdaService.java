package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.EventDao;
import com.kenzie.capstone.service.model.EventData;
import com.kenzie.capstone.service.model.EventRecord;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.model.ExampleRecord;

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
            return new EventData(records.get(0).getId(), records.get(0).getData());
        }
        return null;
    }

    public EventData setEventData(String data) {
        String eventId = UUID.randomUUID().toString();
        //changed id to eventId
        EventRecord eventRecord = eventDao.setEventData(eventId, data);
        return new EventData(eventId, data);
    }
}
