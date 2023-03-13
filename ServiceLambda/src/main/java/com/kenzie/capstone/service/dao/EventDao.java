package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.EventData;
import com.kenzie.capstone.service.model.EventRecord;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.ExampleRecord;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;

import java.util.List;

public class EventDao {
    private DynamoDBMapper mapper;

    /**
     * Allows access to and manipulation of Match objects from the data store.
     * @param mapper Access to DynamoDB
     */
    public EventDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public EventData storeEventData(EventData eventData) {
        try {
            mapper.save(eventData, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "eventId",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("event id has already been used");
        }

        return eventData;
    }

    public List<EventRecord> getEventData(String eventId) {
        EventRecord eventRecord = new EventRecord();
        eventRecord.setId(eventId);

        DynamoDBQueryExpression<EventRecord> queryExpression = new DynamoDBQueryExpression<EventRecord>()
                .withHashKeyValues(eventRecord)
                .withConsistentRead(false);

        return mapper.query(EventRecord.class, queryExpression);
    }

    public EventRecord setEventData(String eventId, String data) {
        EventRecord eventRecord = new EventRecord();
        eventRecord.setId(eventId);
        eventRecord.setData(data);

        try {
            mapper.save(eventRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "eventId",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("event id already exists");
        }

        return eventRecord;
    }
}

