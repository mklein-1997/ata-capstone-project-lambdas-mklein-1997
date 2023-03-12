package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.CreateEventRequest;
import com.kenzie.appserver.controller.model.EventResponse;
import com.kenzie.appserver.repositories.EventRepository;
import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.service.model.Event;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class EventService {
    private EventRepository eventRepository;


    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Find all events
     * @return List of Events
     */
    public List<EventResponse> findAllEvents() {
      List<EventRecord> eventRecords = StreamSupport.stream(eventRepository.findAll().spliterator(), true).collect(Collectors.toList());

        if(eventRecords.isEmpty()) {
          return null;
        }

        List<EventResponse> events = new ArrayList<>();
        for (EventRecord eventRecord : eventRecords) {
            events.add(toEventResponse(eventRecord));
        }
        return events;
     }

    /**
     * Find event by id
     * @param id
     * @return Event
     */

    public EventResponse getEvent(String id) {
        EventRecord eventRecord = eventRepository.findById(id).orElse(null);
        return toEventResponse(eventRecord);
    }

/**
     * Create a new event
     * @param event
     * @return Event
     */
    public EventResponse addNewEvent(EventRecord event) {
        if(event.getEventId() == null || event.getEventId().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Event ID");
        }
        event = eventRepository.save(event);
        return toEventResponse(event);

    }

    public EventResponse update(String id, Event event) {
        Optional<EventRecord> eventRecords = eventRepository.findById(id);
        if(eventRecords.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event Not Found");
        }
        EventRecord eventRecord = eventRecords.get();
        eventRecord.setCustomerName(event.getCustomerName());
        eventRecord.setCustomerEmail(event.getCustomerEmail());
        eventRecord.setDate(event.getEventDate());
        eventRecord.setEventId(event.getEventId());
        eventRecord.setStatus(event.getEventStatus());
        eventRecord = eventRepository.save(eventRecord);
        return toEventResponse(eventRecord);
    }

    public void deleteEvent(String id) {
        if(id != null){
            eventRepository.deleteById(id);
        }
    }

    public void deleteAll() {
        eventRepository.deleteAll();
    }

    private EventResponse toEventResponse(EventRecord eventRecord) {
        if(eventRecord == null) {
            return null;
        }
        EventResponse eventResponse = new EventResponse();
        eventResponse.setCustomerName(eventRecord.getCustomerName());
        eventResponse.setCustomerEmail(eventRecord.getCustomerEmail());
        eventResponse.setDate(eventRecord.getDate());
        eventResponse.setEventId(eventRecord.getEventId());
        eventResponse.setStatus(eventRecord.getStatus());
        return eventResponse;
    }

//    private EventRecord toEventRecord(CreateEventRequest createEventRequest) {
//        if(createEventRequest == null) {
//            return null;
//        }
//        EventRecord eventRecord = new EventRecord();
//        eventRecord.setCustomerName(String.valueOf(createEventRequest.getCustomerName()));
//        eventRecord.setCustomerEmail(String.valueOf(createEventRequest.getCustomerEmail()));
//        eventRecord.setDate(String.valueOf(createEventRequest.getDate()));
//        eventRecord.setEventId(String.valueOf(createEventRequest.getEventId()));
//        eventRecord.setStatus(String.valueOf(createEventRequest.getStatus()));
//        return eventRecord;
//    }

}