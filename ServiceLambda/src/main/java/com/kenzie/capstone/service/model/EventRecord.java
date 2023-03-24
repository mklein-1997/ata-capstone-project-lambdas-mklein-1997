package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "LambdaEvent")
public class EventRecord {

    private String eventId;
    private String data;

    @DynamoDBHashKey(attributeName = "eventId")
    public String getEventId() {
        return eventId;
    }

    @DynamoDBAttribute(attributeName = "data")
    public String getData() {
        return data;
    }

    public void setEventId(String id) {
        this.eventId = eventId;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventRecord that = (EventRecord) o;
        return Objects.equals(eventId, that.eventId) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, data);
    }
}
