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

    @BeforeEach
    void setup() {
        eventRepository = mock(EventRepository.class);
        LambdaServiceClient lambdaServiceClient = mock(LambdaServiceClient.class);
        eventService = new EventService(eventRepository, lambdaServiceClient);
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
        EventResponse response = eventService.findEventById(id);

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
        EventResponse response = eventService.findEventById(id);

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
        // WHEN
        when(eventRepository.findAll()).thenReturn(List.of());
        List<EventResponse> response = eventService.findAllEvents();

        // THEN
        Assertions.assertNull(response, "The object is returned");
    }

    @Test
    void addNewEvent() {
        // GIVEN
        String name = "Test Name";
        String email = "email";
        String date = "date";
        String status = "status";

        Event event = new Event(name, email, date, status);

        EventRecord record = new EventRecord();
        record.setEventId(event.getEventId());
        record.setCustomerName("Test Name");
        record.setCustomerEmail("email");
        record.setDate("date");
        record.setStatus("status");

        // WHEN
        when(eventRepository.save(record)).thenReturn(record);
        EventResponse response = eventService.addNewEvent(event);

        // THEN
        Assertions.assertNotNull(response, "The object is returned");
        Assertions.assertEquals(record.getEventId(), response.getEventId(), "The id matches");
        Assertions.assertEquals(record.getCustomerName(), response.getCustomerName(), "The name matches");
    }

    @Test
    void addNullEvent_throwsNullPointerException(){
        //THEN
        assertThrows(NullPointerException.class, () -> eventService.addNewEvent(null));
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
        assertThrows(ResponseStatusException.class, () -> eventService.update(id, event));

    }
}
