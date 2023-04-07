package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

    @DynamoDBTable(tableName = "Events")
    public class EventRecord {

        private String eventId;
        private String date;
        private String status;
        private String customerName;
        private String customerEmail;

        private String data;

        @DynamoDBHashKey(attributeName = "eventId")
        public String getEventId() {
            return eventId;
        }

        @DynamoDBAttribute(attributeName = "date")
        public String getDate() {
            return date;
        }

        @DynamoDBAttribute(attributeName = "status")
        public String getStatus() {
            return status;
        }

        @DynamoDBAttribute(attributeName = "customerName")
        public String getCustomerName() {
            return customerName;
        }

        @DynamoDBAttribute(attributeName = "customerEmail")
        public String getCustomerEmail() {
            return customerEmail;
        }

        @DynamoDBAttribute(attributeName = "data")
        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EventRecord that = (EventRecord) o;
            return Objects.equals(eventId, that.eventId) && Objects.equals(date, that.date) && Objects.equals(status,
                    that.status) && Objects.equals(customerName, that.customerName) && Objects.equals(customerEmail,
                    that.customerEmail) && Objects.equals(data,
                    that.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(eventId, date, status, customerName, customerEmail, data);
        }
    }
