package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.EventResponse;
import com.kenzie.appserver.repositories.EventRepository;
import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.service.model.Event;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    private EventRepository eventRepository;
    private EventService eventService;
    private LambdaServiceClient lambdaServiceClient;

    @BeforeEach
    void setup() {
        eventRepository = mock(EventRepository.class);
        lambdaServiceClient = mock(LambdaServiceClient.class);
        eventService = new EventService(eventRepository);
    }
    /** ------------------------------------------------------------------------
     *  exampleService.findById
     *  ------------------------------------------------------------------------ **/

    @Test
    void findById() {
        // GIVEN
        String id = randomUUID().toString();

        EventRecord record = new EventRecord();
        record.setEventId(id);
        record.setCustomerName("Test Name");


        // WHEN
        when(eventRepository.findById(id)).thenReturn(Optional.of(record));
        EventResponse response = eventService.getEvent(id);

        // THEN
        Assertions.assertNotNull(response, "The object is returned");
        Assertions.assertEquals(record.getEventId(), response.getEventId(), "The id matches");
        Assertions.assertEquals(record.getCustomerName(), response.getCustomerName(), "The name matches");
    }

    @Test
    void findByIdNotFound() {
        // GIVEN
        String id = randomUUID().toString();

        // WHEN
        when(eventRepository.findById(id)).thenReturn(Optional.empty());
        EventResponse response = eventService.getEvent(id);

        // THEN
        Assertions.assertNull(response, "The object is returned");
    }

    @Test
    void findAllEvents() {
        // GIVEN
        String id = randomUUID().toString();

        EventRecord record = new EventRecord();
        record.setEventId(id);
        record.setCustomerName("Test Name");
        record.setCustomerEmail("email");
        record.setDate("date");
        record.setStatus("status");

        // WHEN
        when(eventRepository.findAll()).thenReturn(List.of(record));
        List<EventResponse> response = eventService.findAllEvents();

        // THEN
        Assertions.assertNotNull(response, "The object is returned");
        Assertions.assertEquals(record.getEventId(), response.get(0).getEventId(), "The id matches");
        Assertions.assertEquals(record.getCustomerName(), response.get(0).getCustomerName(), "The name matches");
    }

    @Test
    void findAllEventsNotFound() {
        // GIVEN
        String id = randomUUID().toString();

        // WHEN
        when(eventRepository.findAll()).thenReturn(List.of());
        List<EventResponse> response = eventService.findAllEvents();

        // THEN
        Assertions.assertNull(response, "The object is returned");
    }

    @Test
    void addNewEvent() {
        // GIVEN
        String id = randomUUID().toString();

        EventRecord record = new EventRecord();
        record.setEventId(id);
        record.setCustomerName("Test Name");
        record.setCustomerEmail("email");
        record.setDate("date");
        record.setStatus("status");

        // WHEN
        when(eventRepository.save(record)).thenReturn(record);
        EventResponse response = eventService.addNewEvent(record);

        // THEN
        Assertions.assertNotNull(response, "The object is returned");
        Assertions.assertEquals(record.getEventId(), response.getEventId(), "The id matches");
        Assertions.assertEquals(record.getCustomerName(), response.getCustomerName(), "The name matches");
    }

    @Test
    void addNewEventInvalidId_throws_ResponseStatusException() {
        // GIVEN
        EventRecord record = new EventRecord();
        record.setEventId(null);
        record.setCustomerName("Test Name");
        record.setCustomerEmail("email");
        record.setDate("date");
        record.setStatus("status");

        assertThrows(ResponseStatusException.class, () -> {
            eventService.addNewEvent(record);
        });
    }

    @Test
    void deleteEvent() {
        // GIVEN
        String id = randomUUID().toString();

        EventRecord record = new EventRecord();
        record.setEventId(id);
        record.setCustomerName("Test Name");
        record.setCustomerEmail("email");
        record.setDate("date");
        record.setStatus("status");

        // WHEN
        when(eventRepository.findById(id)).thenReturn(Optional.of(record));
        eventService.deleteEvent(id);

        // THEN
        verify(eventRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteAllEvents() {
        // GIVEN
        String id = randomUUID().toString();
        String id2 = randomUUID().toString();


        EventRecord record = new EventRecord();
        record.setEventId(id);
        record.setCustomerName("Test Name");
        record.setCustomerEmail("email");
        record.setDate("date");
        record.setStatus("status");

        EventRecord record2 = new EventRecord();
        record2.setEventId(id2);
        record2.setCustomerName("Test Name2");
        record2.setCustomerEmail("email2");
        record2.setDate("date2");
        record2.setStatus("status2");

        List<EventRecord> records = List.of(record, record2);

        // WHEN
        when(eventRepository.findAll()).thenReturn(records);
        eventService.deleteAll();

        // THEN
        verify(eventRepository, times(1)).deleteAll();
    }

    @Test
    void updateEvent() {
        // GIVEN
        String id = randomUUID().toString();

        EventRecord record = new EventRecord();
        record.setEventId(id);
        record.setCustomerName("Test Name");
        record.setCustomerEmail("email");
        record.setDate("date");
        record.setStatus("Pending");

        Event record2 = new Event(record.getCustomerName(), record.getCustomerEmail(), record.getDate(), record.getStatus());
        record2.setEventStatus("Completed");

        record.setStatus(record2.getEventStatus());


        // WHEN
        when(eventRepository.findById(id)).thenReturn(Optional.of(record));
        when(eventRepository.save(record)).thenReturn(record);
        EventResponse response = eventService.update(id, record2);

        // THEN
        Assertions.assertNotNull(response, "The object is returned");
        Assertions.assertEquals(record.getEventId(), response.getEventId(), "The id matches");
        Assertions.assertEquals(record.getCustomerName(), response.getCustomerName(), "The name matches");
        Assertions.assertEquals(record.getCustomerEmail(), response.getCustomerEmail(), "The email matches");
        Assertions.assertEquals(record.getDate(), response.getDate(), "The date matches");
        Assertions.assertEquals(record.getStatus(), response.getStatus(), "The status matches");
    }

    @Test
    void updateEventInvalidId_throws_ResponseStatusException() {
        // GIVEN
        String id = randomUUID().toString();
        Event event = new Event("Test Name", "email", "date", "status");
        //WHEN
        when(eventRepository.findById(anyString())).thenReturn(Optional.empty());

        //THEN
        assertThrows(ResponseStatusException.class, () -> {
            eventService.update(id, event);
        });

    }
}
