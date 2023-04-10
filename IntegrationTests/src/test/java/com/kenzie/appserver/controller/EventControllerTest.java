package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.CreateEventRequest;
import com.kenzie.appserver.service.EventService;
import com.kenzie.appserver.service.model.Event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

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

    private final ObjectMapper mapper = new ObjectMapper();


    @Test
    public void getByEventId_Exists() throws Exception {

        String id = UUID.randomUUID().toString();
        Event event = new Event(id, "Thor", "godOfThunder@valhalla.com", LocalDate.now().toString(), "Event Created");
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
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        Event event = new Event(id, "Thor", "godOfThunder@valhalla.com", LocalDate.now().toString(), "Event Created");
        Event event1 = new Event(id1, "Loki", "loki@valhalla.com", LocalDate.now().toString(), "Event Created");
        Event event2 = new Event(id2, "Captain America", "americasbestbutt@Merica.com", LocalDate.now().toString(), "Event Created");
        eventService.addNewEvent(event);
        eventService.addNewEvent(event1);
        eventService.addNewEvent(event2);


        mvc.perform(get("/events/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        eventService.deleteEvent(id);
        eventService.deleteEvent(id1);
        eventService.deleteEvent(id2);

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
        request.setDate(date);
        request.setStatus("upcoming");

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
                .andExpect(jsonPath("$.customerName", is("Shara Smith")))
                .andExpect(jsonPath("$.customerEmail", is("shara@email.com")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteEvent_DeleteSuccessful() throws Exception{
        String eventId = UUID.randomUUID().toString();
        String date = LocalDate.now().toString();
        String status = "upcoming";
        String customerName = "Fiona";
        String customerEmail = "Fiona&Shrek@forver.after.com";

        Event event = new Event(eventId,date,status,customerName,customerEmail);
        eventService.addNewEvent(event);

        mvc.perform(delete("/events/{eventId}", eventId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

}