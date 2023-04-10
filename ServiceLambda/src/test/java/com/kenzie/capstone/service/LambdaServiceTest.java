package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.EventDao;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.EventData;
import com.kenzie.capstone.service.model.LambdaEventRecord;
import com.kenzie.capstone.service.model.LambdaEventRequest;
import com.kenzie.capstone.service.model.LambdaEventResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaServiceTest {

    /** ------------------------------------------------------------------------
     *  expenseService.getExpenseById
     *  ------------------------------------------------------------------------ **/

    private EventDao eventDao;
    private LambdaService lambdaService;

    @BeforeAll
    void setup() {
        this.eventDao = mock(EventDao.class);
        this.lambdaService = new LambdaService(eventDao);
    }

    @Test
    void setDataTest() {
        //GIVEN
        LambdaEventRequest eventRequest = new LambdaEventRequest();
        eventRequest.setEventId("fakeid");
        eventRequest.setDate("2021-01-01");
        eventRequest.setCustomerEmail("fakeemail");
        eventRequest.setCustomerName("fakename");

        //WHEN
        LambdaEventResponse response = this.lambdaService.addEvent(eventRequest);

        //THEN
        assertNotNull(response);
        assertEquals("fakeid", response.getEventId());
        assertEquals("2021-01-01", response.getDate());
        assertEquals("fakeemail", response.getCustomerEmail());
        assertEquals("fakename", response.getCustomerName());
    }

    @Test
    void getDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String id = "fakeid";
        String name = "fakename";
        String email = "fakeemail";
        String date = "2021-01-01";

        LambdaEventRecord record = new LambdaEventRecord();
        record.setEventId(id);
        record.setCustomerName(name);
        record.setCustomerEmail(email);
        record.setDate(date);

        when(eventDao.getEventData(id)).thenReturn(List.of(record));

        // WHEN
        EventData response = this.lambdaService.getEventData(id);

        // THEN
        verify(eventDao, times(1)).getEventData(idCaptor.capture());

        assertEquals(id, idCaptor.getValue(), "The correct id is used");

        assertNotNull(response, "A response is returned");
        assertEquals(id, response.getEventId(), "The response id should match");
        reset(eventDao);
    }

    @Test
    void getData_emptyListFromDao_returnsNull() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        //GIVEN
        String id = "fakeid";
        when(eventDao.getEventData(id)).thenReturn(Collections.emptyList());

        //WHEN
        EventData response = this.lambdaService.getEventData(id);

        //THEN
        verify(eventDao, times(1)).getEventData(idCaptor.capture());

        assertEquals(id, idCaptor.getValue(), "The correct id is used");
        assertNull(response, "The response should be null when an empty list is returned");
    }

    // Write additional tests here
    @Test
    void deleteDataTest() {
        //GIVEN
        List<String> ids = new ArrayList<>();
        ids.add("fakeid");

        LambdaEventRecord record = new LambdaEventRecord();
        record.setEventId("fakeid");

        //WHEN
        when(eventDao.deleteEventData(record)).thenReturn(true);
        boolean response = this.lambdaService.deleteEventData(ids);

        //THEN
        verify(eventDao, times(1)).deleteEventData(record);
        assertTrue(response);
    }

    @Test
    void deleteData_nullList_throwsInvalidDataException() {
        //GIVEN
        //WHEN && THEN
        assertThrows(InvalidDataException.class, () -> this.lambdaService.deleteEventData(null));
    }

    @Test
    void deleteData_nullId_throwsInvalidDataException() {
        //GIVEN
        List<String> ids = new ArrayList<>();
        ids.add(null);

        //WHEN && THEN
        assertThrows(InvalidDataException.class, () -> this.lambdaService.deleteEventData(ids));
    }

    @Test
    void deleteData_daoReturnsFalse_returnsFalse() {
        //GIVEN
        List<String> ids = new ArrayList<>();
        ids.add("fakeid");

        LambdaEventRecord record = new LambdaEventRecord();
        record.setEventId("fakeid");

        //WHEN
        when(eventDao.deleteEventData(record)).thenReturn(false);
        boolean response = this.lambdaService.deleteEventData(ids);

        //THEN
        verify(eventDao, times(1)).deleteEventData(record);
        assertFalse(response);
    }

    @Test
    void addNewEvent_Test() {
        //GIVEN
        LambdaEventRequest eventRequest = new LambdaEventRequest();
        eventRequest.setEventId("fakeid");
        eventRequest.setDate("2021-01-01");
        eventRequest.setCustomerEmail("fakeemail");
        eventRequest.setCustomerName("fakename");

        //WHEN
        LambdaEventResponse response = this.lambdaService.addEvent(eventRequest);

        //THEN
        assertNotNull(response);
        assertEquals("fakeid", response.getEventId());
        assertEquals("2021-01-01", response.getDate());
        assertEquals("fakeemail", response.getCustomerEmail());
        assertEquals("fakename", response.getCustomerName());
    }

    @Test
    void addNewEvent_nullEventRequest_throwsInvalidDataException() {
        //GIVEN

        //WHEN && THEN
        assertThrows(InvalidDataException.class, () -> this.lambdaService.addEvent(null));
    }

    @Test
    void addNewEvent_nullEventId_throwsInvalidDataException() {
        //GIVEN
        LambdaEventRequest eventRequest = new LambdaEventRequest();
        eventRequest.setEventId(null);
        eventRequest.setDate("2021-01-01");
        eventRequest.setCustomerEmail("fakeemail");
        eventRequest.setCustomerName("fakename");

        //WHEN && THEN
        assertThrows(InvalidDataException.class, () -> this.lambdaService.addEvent(eventRequest));
    }

    @Test
    void addNewEvent_invalidName_throwsInvalidDataException() {
        //GIVEN
        LambdaEventRequest eventRequest = new LambdaEventRequest();
        eventRequest.setEventId(null);
        eventRequest.setDate("2021-01-01");
        eventRequest.setCustomerEmail("fakeemail");
        eventRequest.setCustomerName("");

        //WHEN && THEN
        assertThrows(InvalidDataException.class, () -> this.lambdaService.addEvent(eventRequest));
    }

}