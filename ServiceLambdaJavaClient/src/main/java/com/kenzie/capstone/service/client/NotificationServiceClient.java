package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.Notification;
import com.kenzie.capstone.service.model.NotificationRequest;
import com.kenzie.capstone.service.model.NotificationResponse;


public class NotificationServiceClient {

    private static final String GET_NOTIFICATION_ENDPOINT = "/notification/eventId";
    private static final String ADD_NOTIFICATION_ENDPOINT = "/notification/";
    private static final String DELETE_NOTIFICATION_ENDPOINT = "/notification/eventId";
    private static final String UPDATE_NOTIFICATION_ENDPOINT = "/notification/eventId";

    private final ObjectMapper mapper;

    public NotificationServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public Notification getNotification(String eventId) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_NOTIFICATION_ENDPOINT.replace("eventId", eventId));
        Notification notification;
        try {
            notification = mapper.readValue(response, Notification.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return notification;
    }

    public NotificationResponse addNotification(NotificationRequest eventRequest) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(eventRequest);
        } catch(JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }
        String response = endpointUtility.postEndpoint(ADD_NOTIFICATION_ENDPOINT, request);
        NotificationResponse eventResponse;
        try {
            eventResponse = mapper.readValue(response, NotificationResponse.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return eventResponse;
    }

    public NotificationResponse updateNotification(NotificationRequest eventRequest) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(eventRequest);
        } catch(JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }
        String response = endpointUtility.putEndpoint(UPDATE_NOTIFICATION_ENDPOINT.replace("eventId", eventRequest.getEventId()), request);
        NotificationResponse eventResponse;
        try {
            eventResponse = mapper.readValue(response, NotificationResponse.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return eventResponse;
    }

    public boolean deleteNotification(String eventId) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;

        try{
            request = mapper.writeValueAsString(eventId);
        } catch (JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }

        String response = endpointUtility.postEndpoint(DELETE_NOTIFICATION_ENDPOINT, request);
        boolean result;

        try{
            result = mapper.readValue(response, Boolean.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to deserialize JSON: " + e);
        }

        return result;
    }
}
