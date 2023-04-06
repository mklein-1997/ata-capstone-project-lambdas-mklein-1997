package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.EventData;

import java.util.List;


public class LambdaServiceClient {

    private static final String GET_EVENT_ENDPOINT = "events/{eventId}";
    private static final String SET_EVENT_ENDPOINT = "events";
    private static final String DELETE_EVENT_ENDPOINT = "events/delete";

    private ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public EventData getEventData(String eventId) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_EVENT_ENDPOINT.replace("{eventId}", eventId));
        EventData eventData;
        try {
            eventData = mapper.readValue(response, EventData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return eventData;
    }

    public EventData setEventData(String data) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.postEndpoint(SET_EVENT_ENDPOINT, data);
        EventData eventData;
        try {
            eventData = mapper.readValue(response, EventData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return eventData;
    }

    public boolean deleteEventData(List<String> eventIds) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;

        try{
            request = mapper.writeValueAsString(eventIds);
        } catch (JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }

        String response = endpointUtility.postEndpoint(DELETE_EVENT_ENDPOINT, request);
        boolean result;

        try{
            result = mapper.readValue(response, Boolean.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to deserialize JSON: " + e);
        }

        return result;
    }
}
