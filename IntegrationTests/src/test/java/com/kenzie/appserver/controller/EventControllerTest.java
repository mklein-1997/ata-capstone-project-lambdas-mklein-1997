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

//    @Test
//    public void getByEventId_Exists() throws Exception {
//
//        String event = mockNeat.strings().valStr();
//
//        Event persistedEvent = eventService.addNewEvent(event);
//        //addNewEvent in EventService returns EventRecord instead of String
//        //original ExampleService file had String type https://tinyurl.com/addNewExample
//
//        mvc.perform(get("/events/{eventId}", persistedEvent.getEventId())
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("eventId")
//                        .isString())
//                .andExpect(jsonPath("event")
//                        .value(is(event)))
//                .andExpect(status().is2xxSuccessful());
//    }

    @Test
    public void createEvent_CreateSuccessful() throws Exception {
        String event = mockNeat.strings().valStr();

        CreateEventRequest createEventRequest = new CreateEventRequest();
        createEventRequest.setEventId(event);

        mapper.registerModule(new JavaTimeModule());

        mvc.perform(post("/events")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createEventRequest)))
                .andExpect(jsonPath("eventId")
                        .exists())
                .andExpect(jsonPath("event")
                        .value(is(event)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getEvent_EventDoesNotExist() throws Exception {
        String eventId = UUID.randomUUID().toString();

        mvc.perform(get("/events/{eventId}", eventId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

//    @Test
//    public void updateEvent_UpdatesEvent() throws Exception{
//        String eventId = UUID.randomUUID().toString();
//        String date = LocalDate.now().toString();
//        String status = "upcoming";
//        String customerName = "Erica";
//        String customerEmail = "erica.muse@mykenzie.snhu.edu";
//
//        Event event = new Event(eventId,date,status,customerName,customerEmail);
//        Event persistedEvent = eventService.addNewEvent(event);
//        //**addNewEvent in EventService has EventRecord type
//
//        String newCustomerEmail = "erica.muse.new@mykenzie.snhu.edu";
//
//        CreateEventRequest createEventRequest = new CreateEventRequest();
//        createEventRequest.setEventId(eventId);
//        createEventRequest.setDate(Optional.of(date));
//        createEventRequest.setStatus(Optional.of(status));
//        createEventRequest.setCustomerName(Optional.of(customerName));
//        createEventRequest.setCustomerEmail(Optional.of(newCustomerEmail));
//
//        mapper.registerModule(new JavaTimeModule());
//
//        mvc.perform(put("/events")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(createEventRequest)))
//
//                .andExpect(jsonPath("eventId")
//                        .exists())
//                .andExpect(jsonPath("date")
//                        .value(is(date)))
//                .andExpect(jsonPath("status")
//                        .value(is(status)))
//                .andExpect(jsonPath("customerName")
//                        .value(is(customerName)))
//                .andExpect(jsonPath("customerEmail")
//                        .value(is(newCustomerEmail)))
//                .andExpect(status().isOk());
//    }

//    @Test
//    public void deleteEvent_DeleteSuccessful() throws Exception{
//        String eventId = UUID.randomUUID().toString();
//        String date = LocalDate.now().toString();
//        String status = "upcoming";
//        String customerName = "Sarah";
//        String customerEmail = "sarah_event_planner@gmail.com";
//
//        Event event = new Event(eventId,date,status,customerName,customerEmail);
//        Event persistedEvent = eventService.addNewEvent(event);
//        //**addNewEvent in EventService has EventRecord type
//
//        mvc.perform(delete("/events/{eventId}", persistedEvent.getEventId())
//                .accept(MediaType.APPLICATION_JSON))
//
//                .andExpect(status().isNoContent());
//                assertThat(eventService.findByEventId(eventId)).isNull();
//                //EventService needs findByEventId method
//    }

}