package com.kenzie.capstone.service;

import com.kenzie.capstone.service.converter.NotificationConverter;
import com.kenzie.capstone.service.dao.NotificationDao;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.EventData;
import com.kenzie.capstone.service.model.NotificationRecord;
import com.kenzie.capstone.service.model.NotificationRequest;
import com.kenzie.capstone.service.model.NotificationResponse;

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
        if (event == null || event.getEventId() == null || event.getCustomerName().length() == 0) {
            throw new InvalidDataException("Request must contain a valid Customer Name");
        }
        NotificationRecord record = NotificationConverter.fromRequestToRecord(event);
        notificationDao.addEvent(record);
        return NotificationConverter.fromRecordToResponse(record);
    }

    public EventData getNotification(String eventId) {
        List<NotificationRecord> records = notificationDao.findByEventId(eventId);
        if (records.size() > 0) {
            return new EventData(records.get(0).getEventId(), records.get(0).getCustomerName(),
                    records.get(0).getCustomerEmail(), records.get(0).getDate(), records.get(0).getStatus());
        }
        return null;
    }

    public NotificationResponse updateNotification(NotificationRequest event) {
        if (event == null || event.getEventId() == null || event.getCustomerName().length() == 0) {
            throw new InvalidDataException("Request must contain a valid Customer Name");
        }
        NotificationRecord record = NotificationConverter.fromRequestToRecord(event);
        notificationDao.updateEvent(record);
        return NotificationConverter.fromRecordToResponse(record);
    }

    public Boolean deleteNotification(List<String> eventIds) {
        boolean allDeleted = true;

        if(eventIds == null){
            throw new InvalidDataException("Request must contain a valid list of Event Id's");
        }

        for(String eventId : eventIds){
            if(eventId == null || eventId.length() == 0){
                throw new InvalidDataException("Event ID cannot be null or empty to delete");
            }

            NotificationRecord record = new NotificationRecord();
            record.setEventId(eventId);

            boolean deleted = notificationDao.deleteEvent(record);

            if(!deleted){
                allDeleted = false;
            }
        }
        return allDeleted;
    }
}
