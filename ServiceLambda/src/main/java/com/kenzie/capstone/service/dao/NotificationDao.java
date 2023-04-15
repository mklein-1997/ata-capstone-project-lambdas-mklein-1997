package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.NotificationRecord;

import java.util.List;

public interface NotificationDao {
    NotificationRecord addEvent(NotificationRecord event);
    boolean deleteEvent(NotificationRecord event);
    List<NotificationRecord> findByEventId(String eventId);
    List<NotificationRecord> findUsersWithoutEventId();
    NotificationRecord updateEvent(NotificationRecord event);

}
