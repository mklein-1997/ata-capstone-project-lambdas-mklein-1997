package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.EventDao;
import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.model.EventData;
import com.kenzie.capstone.service.model.EventRecord;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.ExampleRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> dataCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String data = "somedata";

        // WHEN
        EventData response = this.lambdaService.setEventData(data);

        // THEN
        verify(eventDao, times(1)).setEventData(idCaptor.capture(), dataCaptor.capture());

        assertNotNull(idCaptor.getValue(), "An ID is generated");
        assertEquals(data, dataCaptor.getValue(), "The data is saved");

        assertNotNull(response, "A response is returned");
        assertEquals(idCaptor.getValue(), response.getEventId(), "The response id should match");
        assertEquals(data, response.getData(), "The response data should match");
    }

    @Test
    void getDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String id = "fakeid";
        String data = "somedata";
        EventRecord record = new EventRecord();
        record.setId(id);
        record.setData(data);


        when(eventDao.getEventData(id)).thenReturn(Arrays.asList(record));

        // WHEN
        EventData response = this.lambdaService.getEventData(id);

        // THEN
        verify(eventDao, times(1)).getEventData(idCaptor.capture());

        assertEquals(id, idCaptor.getValue(), "The correct id is used");

        assertNotNull(response, "A response is returned");
        assertEquals(id, response.getEventId(), "The response id should match");
        assertEquals(data, response.getData(), "The response data should match");
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

}