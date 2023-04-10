package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.EventData;
import com.kenzie.capstone.service.model.LambdaEventRecord;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;

import java.util.List;

public class EventDao {
    private final DynamoDBMapper mapper;

    /**
     * Allows access to and manipulation of Match objects from the data store.
     *
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

    public void addNewEvent(LambdaEventRecord event) {
        try {
            mapper.save(event, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "eventId",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new InvalidDataException("Event already exists");
        }

    }


    public List<LambdaEventRecord> getEventData(String eventId) {
        LambdaEventRecord eventRecord = new LambdaEventRecord();
        //Rename this to eventId
        eventRecord.setEventId(eventId);

        DynamoDBQueryExpression<LambdaEventRecord> queryExpression = new DynamoDBQueryExpression<LambdaEventRecord>()
                .withHashKeyValues(eventRecord)
                .withConsistentRead(false);

        return mapper.query(LambdaEventRecord.class, queryExpression);
    }

    public void updateEvent(LambdaEventRecord record) {
        try {
            mapper.save(record, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "eventId",
                            new ExpectedAttributeValue().withExists(true)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new InvalidDataException("Event already exists");
        }
    }

    public Boolean deleteEventData(LambdaEventRecord eventId) {
        try {
            mapper.delete(eventId, new DynamoDBDeleteExpression()
                    .withExpected(ImmutableMap.of(
                            "eventId",
                            new ExpectedAttributeValue().withExists(true)
                    )));
            return true;
        } catch (ConditionalCheckFailedException e) {
            return false;
        }
    }

    public List<LambdaEventRecord> findByEventId(String eventId) {
        LambdaEventRecord eventRecord = new LambdaEventRecord();
        eventRecord.setEventId(eventId);

        DynamoDBQueryExpression<LambdaEventRecord> queryExpression = new DynamoDBQueryExpression<LambdaEventRecord>()
                .withHashKeyValues(eventRecord)
                .withIndexName("EventIdIndex")
                .withConsistentRead(false);

        return mapper.query(LambdaEventRecord.class, queryExpression);
    }
}

