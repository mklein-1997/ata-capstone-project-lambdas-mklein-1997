package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.LambdaEventRecord;

import java.util.List;

public interface EventDao {
    LambdaEventRecord addEvent(LambdaEventRecord event);
    boolean deleteEvent(LambdaEventRecord event);
    List<LambdaEventRecord> findByEventId(String eventId);
    List<LambdaEventRecord> findUsersWithoutEventId();
    LambdaEventRecord updateEvent(LambdaEventRecord event);

}
