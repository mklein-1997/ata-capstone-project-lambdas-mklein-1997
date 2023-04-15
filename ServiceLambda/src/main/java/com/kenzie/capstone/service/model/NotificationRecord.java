package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

    @DynamoDBTable(tableName = "Notifications")
    public class NotificationRecord {

        private String eventId;
        private String date;
        private String customerEmail;

        @DynamoDBHashKey(attributeName = "eventId")
        public String getEventId() {
            return eventId;
        }

        @DynamoDBAttribute(attributeName = "date")
        public String getDate() {
            return date;
        }

        @DynamoDBAttribute(attributeName = "customerEmail")
        public String getCustomerEmail() {
            return customerEmail;
        }


        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NotificationRecord that = (NotificationRecord) o;
            return Objects.equals(eventId, that.eventId) && Objects.equals(date, that.date) &&
                    Objects.equals(customerEmail, that.customerEmail);
        }

        @Override
        public int hashCode() {
            return Objects.hash(eventId, date, customerEmail);
        }
    }
