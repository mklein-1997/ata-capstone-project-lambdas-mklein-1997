package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.NotificationDao;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.*;
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
class NotificationServiceTest {

    /** ------------------------------------------------------------------------
     *  expenseService.getExpenseById
     *  ------------------------------------------------------------------------ **/

    private NotificationDao notificationDao;
    private NotificationService notificationService;

    @BeforeAll
    void setup() {
        this.notificationDao = mock(NotificationDao.class);
        this.notificationService = new NotificationService(notificationDao);
    }

    @Test
    void setDataTest() {
        //GIVEN
        NotificationRequest eventRequest = new NotificationRequest();
        eventRequest.setEventId("fakeid");
        eventRequest.setDate("2021-01-01");
        eventRequest.setCustomerEmail("fakeemail");

        //WHEN
        NotificationResponse response = this.notificationService.addNotification(eventRequest);

        //THEN
        assertNotNull(response);
        assertEquals("fakeid", response.getEventId());
        assertEquals("2021-01-01", response.getDate());
        assertEquals("fakeemail", response.getCustomerEmail());
    }

    @Test
    void getDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String id = "fakeid";
        String name = "fakename";
        String email = "fakeemail";
        String date = "2021-01-01";

        NotificationRecord record = new NotificationRecord();
        record.setEventId(id);
        record.setCustomerEmail(email);
        record.setDate(date);

        when(notificationDao.findByEventId(id)).thenReturn(List.of(record));

        // WHEN
        Notification response = this.notificationService.getNotification(id);

        // THEN
        verify(notificationDao, times(1)).findByEventId(idCaptor.capture());

        assertEquals(id, idCaptor.getValue(), "The correct id is used");

        assertNotNull(response, "A response is returned");
        assertEquals(id, response.getEventId(), "The response id should match");
        reset(notificationDao);
    }

    @Test
    void getData_emptyListFromDao_returnsNull() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        //GIVEN
        String id = "fakeid";
        when(notificationDao.findByEventId(id)).thenReturn(Collections.emptyList());

        //WHEN
        Notification response = this.notificationService.getNotification(id);

        //THEN
        verify(notificationDao, times(1)).findByEventId(idCaptor.capture());

        assertEquals(id, idCaptor.getValue(), "The correct id is used");
        assertNull(response, "The response should be null when an empty list is returned");
    }

    // Write additional tests here
    @Test
    void deleteDataTest() {
        //GIVEN
        String id = "fakeid";

        NotificationRecord record = new NotificationRecord();
        record.setEventId("fakeid");

        //WHEN
        when(notificationDao.deleteEvent(record)).thenReturn(true);
        boolean response = this.notificationService.deleteNotification(id);

        //THEN
        verify(notificationDao, times(1)).deleteEvent(record);
        assertTrue(response);
    }

    @Test
    void deleteData_nullId_throwsInvalidDataException() {
        //GIVEN
        String id = null;

        //WHEN && THEN
        assertThrows(InvalidDataException.class, () -> this.notificationService.deleteNotification(id));
    }

    @Test
    void deleteData_daoReturnsFalse_returnsFalse() {
        //GIVEN
        String id = "fakeid";

        NotificationRecord record = new NotificationRecord();
        record.setEventId("fakeid");

        //WHEN
        when(notificationDao.deleteEvent(record)).thenReturn(false);
        boolean response = this.notificationService.deleteNotification(id);

        //THEN
        verify(notificationDao, times(1)).deleteEvent(record);
        assertFalse(response);
    }

    @Test
    void addNewEvent_Test() {
        //GIVEN
        NotificationRequest eventRequest = new NotificationRequest();
        eventRequest.setEventId("fakeid");
        eventRequest.setDate("2021-01-01");
        eventRequest.setCustomerEmail("fakeemail");

        //WHEN
        NotificationResponse response = this.notificationService.addNotification(eventRequest);

        //THEN
        assertNotNull(response);
        assertEquals("fakeid", response.getEventId());
        assertEquals("2021-01-01", response.getDate());
        assertEquals("fakeemail", response.getCustomerEmail());
    }

    @Test
    void addNewEvent_nullEventRequest_throwsInvalidDataException() {
        //GIVEN

        //WHEN && THEN
        assertThrows(InvalidDataException.class, () -> this.notificationService.addNotification(null));
    }

    @Test
    void addNewEvent_nullEventId_throwsInvalidDataException() {
        //GIVEN
        NotificationRequest eventRequest = new NotificationRequest();
        eventRequest.setEventId(null);
        eventRequest.setDate("2021-01-01");
        eventRequest.setCustomerEmail("fakeemail");

        //WHEN && THEN
        assertThrows(InvalidDataException.class, () -> this.notificationService.addNotification(eventRequest));
    }

    @Test
    void addNewEvent_invalidName_throwsInvalidDataException() {
        //GIVEN
        NotificationRequest eventRequest = new NotificationRequest();
        eventRequest.setEventId(null);
        eventRequest.setDate("2021-01-01");
        eventRequest.setCustomerEmail("fakeemail");

        //WHEN && THEN
        assertThrows(InvalidDataException.class, () -> this.notificationService.addNotification(eventRequest));
    }

    @Test
    void updateEvent_Test() {
        //GIVEN
        NotificationRequest eventRequest = new NotificationRequest();
        eventRequest.setEventId("fakeid");
        eventRequest.setDate("2021-01-01");
        eventRequest.setCustomerEmail("fakeemail");


        //WHEN
        NotificationResponse response = this.notificationService.updateNotification(eventRequest);

        //THEN
        assertNotNull(response);
        assertEquals("fakeid", response.getEventId());
        assertEquals("2021-01-01", response.getDate());
        assertEquals("fakeemail", response.getCustomerEmail());
    }

    @Test
    void updateEvent_nullEventRequest_throwsInvalidDataException() {
        //GIVEN

        //WHEN && THEN
        assertThrows(InvalidDataException.class, () -> this.notificationService.updateNotification(null));
    }

}