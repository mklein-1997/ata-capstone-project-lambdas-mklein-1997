package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.CreateEventRequest;
import com.kenzie.appserver.controller.model.EventResponse;
import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.service.EventService;

import com.kenzie.appserver.service.model.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private EventService eventService;

    EventController(EventService eventservice){
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponse> addNewEvent(@RequestBody CreateEventRequest createEvent) {

        if (createEvent.getEventId() == null || createEvent.getEventId().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Event ID");
        }

        EventResponse response = eventService.addNewEvent(createEvent);

        return ResponseEntity.created(URI.create("/events/" + response.getEventId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventService.findAllEvents();
        if (events == null || events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> searchEventById(@PathVariable("id") String eventId) {
        EventResponse response = eventService.getEvent(eventId);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEventById(@PathVariable("id") String eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok().build();
    }

    private EventResponse convertToResponse(EventRecord event){
        EventResponse response = new EventResponse();
        response.setEventId(event.getEventId());
        response.setDate(event.getDate());
        response.setStatus(event.getStatus());
        response.setCustomerName(event.getCustomerName());
        response.setCustomerEmail(event.getCustomerEmail());
        return response;
    }

}
