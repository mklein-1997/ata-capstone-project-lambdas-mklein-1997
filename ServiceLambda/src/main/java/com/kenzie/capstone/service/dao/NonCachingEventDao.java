package com.kenzie.capstone.service.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.LambdaEventRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class NonCachingEventDao implements EventDao {
    private DynamoDBMapper mapper;

    static final Logger log = LogManager.getLogger();

    /**
     * Allows access to and manipulation of Match objects from the data store.
     * @param mapper Access to DynamoDB
     */
    public NonCachingEventDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public LambdaEventRecord addEvent(LambdaEventRecord event) {
        try {
            mapper.save(event, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "EventId",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new InvalidDataException("This Event already exists.");
        }

        return event;
    }

    @Override
    public LambdaEventRecord updateEvent(LambdaEventRecord record) {
        try {
            mapper.save(record, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "eventId",
                            new ExpectedAttributeValue().withExists(true)
                    )));
            return record;
        } catch (ConditionalCheckFailedException e) {
            throw new InvalidDataException("Event already exists");
        }
    }

    @Override
    public boolean deleteEvent(LambdaEventRecord event) {
        try {
            mapper.delete(event, new DynamoDBDeleteExpression()
                    .withExpected(ImmutableMap.of(
                            "EventId",
                            new ExpectedAttributeValue().withValue(new AttributeValue(event.getEventId())).withExists(true)
                    )));
        } catch (AmazonDynamoDBException e) {
            log.info(e.getMessage());
            log.info(e.getStackTrace());
            return false;
        }

        return true;
    }
    @Override
    public List<LambdaEventRecord> findByEventId(String eventId) {
        LambdaEventRecord eventRecord = new LambdaEventRecord();
        eventRecord.setEventId(eventId);

        DynamoDBQueryExpression<LambdaEventRecord> queryExpression = new DynamoDBQueryExpression<LambdaEventRecord>()
                .withHashKeyValues(eventRecord)
                .withIndexName("EventIdIndex")
                .withConsistentRead(false);

        return mapper.query(LambdaEventRecord.class, queryExpression);
    }
    @Override
    public List<LambdaEventRecord> findUsersWithoutEventId() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("attribute_not_exists(EventId)");

        return mapper.scan(LambdaEventRecord.class, scanExpression);
    }
}

