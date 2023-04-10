package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.LambdaEventRecord;

import java.util.List;

public interface EventDaoInterface {
    LambdaEventRecord addEvent(LambdaEventRecord event);
    public boolean deleteEvent(LambdaEventRecord event);
    List<LambdaEventRecord> findByEventId(String eventId);
    List<LambdaEventRecord> findUsersWithoutEventId();

}
