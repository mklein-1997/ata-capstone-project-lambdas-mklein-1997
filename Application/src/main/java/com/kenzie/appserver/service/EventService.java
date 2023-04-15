package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.EventResponse;
import com.kenzie.appserver.repositories.EventRepository;
import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.service.model.Event;


import com.kenzie.capstone.service.client.NotificationServiceClient;
import com.kenzie.capstone.service.model.NotificationRequest;
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
    private final EventRepository eventRepository;
    private final NotificationServiceClient notificationServiceClient;


    public EventService(EventRepository eventRepository, NotificationServiceClient notificationServiceClient) {
        this.eventRepository = eventRepository;
        this.notificationServiceClient = notificationServiceClient;
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
     * @param  id Event id
     * @return Event
     */

    public EventResponse findEventById(String id) {
        EventRecord eventRecord = eventRepository.findById(id).orElse(null);
        return toEventResponse(eventRecord);
    }

    public EventResponse addNewEvent(Event event) {

        if(event == null) {
            throw new NullPointerException("Event cannot be null");
        }

        EventRecord eventRecord = toEventRecord(event);

        eventRepository.save(eventRecord);
        //Commented out because it breaks the tests in the pipeline
        notificationServiceClient.addNotification(recordToNotificationRequest(eventRecord));
        return toEventResponse(eventRecord);
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

        notificationServiceClient.updateNotification(recordToNotificationRequest(eventRecord));
        return toEventResponse(eventRecord);
    }

    public void deleteEvent(String id) {
        if(id != null){
            eventRepository.deleteById(id);
            notificationServiceClient.deleteNotification(id);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event Not Found");
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
    private EventRecord toEventRecord(Event event) {
        EventRecord eventRecord = new EventRecord();
        eventRecord.setCustomerName(event.getCustomerName());
        eventRecord.setCustomerEmail(event.getCustomerEmail());
        eventRecord.setDate(event.getEventDate());
        eventRecord.setEventId(event.getEventId());
        eventRecord.setStatus(event.getEventStatus());
        return eventRecord;
    }
    private NotificationRequest recordToNotificationRequest(EventRecord record) {
        NotificationRequest request = new NotificationRequest();
        request.setEventId(record.getEventId());
        request.setCustomerEmail(record.getCustomerEmail());
        request.setDate(record.getDate());
        return request;
    }
}