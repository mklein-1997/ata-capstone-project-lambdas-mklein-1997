package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.CreateEventRequest;
import com.kenzie.appserver.controller.model.EventResponse;
import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.service.EventService;
import com.kenzie.appserver.service.model.Event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class EventControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    EventService eventService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();


    @Test
    public void getByEventId_Exists() throws Exception {

        String id = UUID.randomUUID().toString();
        Event event = new Event(id, "Erica", "email", LocalDate.now().toString(), "Event Created");
        eventService.addNewEvent(event);

        mvc.perform(get("/events/{eventId}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.eventId", is(id)));

        eventService.deleteEvent(id);
    }

    @Test
    public void getAllEvents_Exists() throws Exception {
        String id = UUID.randomUUID().toString();
        Event event = new Event(id, "Erica", "email", LocalDate.now().toString(), "Event Created");
        eventService.addNewEvent(event);

        mvc.perform(get("/events/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        eventService.deleteEvent(id);

    }

    @Test
    public void createEvent_CreateSuccessful() throws Exception {
        String date = LocalDate.now().toString();
        String customerName = "Erica";
        String customerEmail = "email";
        String eventId = UUID.randomUUID().toString();

        CreateEventRequest request = new CreateEventRequest();
        request.setCustomerName(customerName);
        request.setCustomerEmail(customerEmail);
        request.setEventId(eventId);

        mapper.registerModule(new JavaTimeModule());

        String json = mapper.writeValueAsString(request);

        mvc.perform(post("/events")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getEvent_EventDoesNotExist() throws Exception {
        String eventId = UUID.randomUUID().toString();

        mvc.perform(get("/events/{eventId}", eventId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void updateEvent_UpdatesEvent() throws Exception{
        String eventId = UUID.randomUUID().toString();
        String date = LocalDate.now().toString();
        String status = "upcoming";
        String customerName = "Sarah";
        String customerEmail = "email";

        Event event = new Event(eventId,date,status,customerName,customerEmail);
        eventService.addNewEvent(event);

        CreateEventRequest request = new CreateEventRequest();
        request.setCustomerName("Shara Smith");
        request.setCustomerEmail("shara@email.com");
        request.setEventId(eventId);

        mapper.registerModule(new JavaTimeModule());

        String json = mapper.writeValueAsString(request);

        mvc.perform(put("/events/{eventId}", eventId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteEvent_DeleteSuccessful() throws Exception{
        String eventId = UUID.randomUUID().toString();
        String date = LocalDate.now().toString();
        String status = "upcoming";
        String customerName = "Sarah";
        String customerEmail = "sarah_event_planner@gmail.com";

        Event event = new Event(eventId,date,status,customerName,customerEmail);
        eventService.addNewEvent(event);

        mvc.perform(delete("/events/{eventId}", eventId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

}