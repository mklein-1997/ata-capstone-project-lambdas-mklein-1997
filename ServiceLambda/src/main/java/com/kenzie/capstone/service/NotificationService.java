package com.kenzie.capstone.service;

import com.kenzie.capstone.service.converter.NotificationConverter;
import com.kenzie.capstone.service.dao.NotificationDao;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.*;

import javax.inject.Inject;

import java.util.List;


public class NotificationService {
    //original LambdaService file https://tinyurl.com/LambdaService

    private final NotificationDao notificationDao;

    @Inject
    public NotificationService(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    public NotificationResponse addNotification(NotificationRequest event) {
        if (event == null || event.getEventId() == null || event.getCustomerEmail().length() == 0) {
            throw new InvalidDataException("Request must contain a valid Customer Name");
        }
        NotificationRecord record = NotificationConverter.fromRequestToRecord(event);
        notificationDao.addEvent(record);
        return NotificationConverter.fromRecordToResponse(record);
    }

    public Notification getNotification(String eventId) {
        List<NotificationRecord> records = notificationDao.findByEventId(eventId);
        if (records.size() > 0) {
            return new Notification(records.get(0).getEventId(), records.get(0).getCustomerEmail(), records.get(0).getDate());
        }
        return null;
    }

    public NotificationResponse updateNotification(NotificationRequest event) {
        if (event == null || event.getEventId() == null || event.getCustomerEmail().length() == 0) {
            throw new InvalidDataException("Request must contain a valid Customer Name");
        }
        NotificationRecord record = NotificationConverter.fromRequestToRecord(event);
        notificationDao.updateEvent(record);
        return NotificationConverter.fromRecordToResponse(record);
    }

    public Boolean deleteNotification(String eventId) {
        if(eventId == null){
            throw new InvalidDataException("Request must contain a valid Event Id");
        }

        NotificationRecord record = new NotificationRecord();
        record.setEventId(eventId);

        boolean deleted = notificationDao.deleteEvent(record);

        if(!deleted){
                deleted = false;
        }

        return deleted;
    }
}
