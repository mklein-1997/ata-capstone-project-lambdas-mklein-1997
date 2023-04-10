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
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    EventController(EventService service) {
        //Rename it to avoid confusion and any possible bugs
        this.eventService = service;
    }
    @GetMapping("/all")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventService.findAllEvents();
        if (events == null || events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(events);
    }
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> searchEventById(@PathVariable("eventId") String eventId) {
        EventResponse response = eventService.findEventById(eventId);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);

    }
    @PostMapping
    public ResponseEntity<EventResponse> addNewEvent(@RequestBody CreateEventRequest createEvent) {

        String date = createEvent.getDate();

       Event event = new Event(UUID.randomUUID().toString(), createEvent.getCustomerName(), createEvent.getCustomerEmail(), date , createEvent.getStatus());
       EventResponse response = eventService.addNewEvent(event);

        return ResponseEntity.created(URI.create("/events/" + response.getEventId())).body(response);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable("eventId") String eventId, @RequestBody CreateEventRequest createEvent) {
        Event event = new Event(eventId, createEvent.getCustomerName(), createEvent.getCustomerEmail(), createEvent.getDate(), createEvent.getStatus());
        EventResponse response = eventService.update(eventId, event);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{eventId}")
    public ResponseEntity<EventResponse> deleteEventById(@PathVariable("eventId") String eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
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

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResponseStatusException.class})
    public void handleNotFound() {
    }

}
