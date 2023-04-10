package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.EventData;
import com.kenzie.capstone.service.model.LambdaEventRequest;
import com.kenzie.capstone.service.model.LambdaEventResponse;

import java.util.List;


public class LambdaServiceClient {

    private static final String GET_EVENT_ENDPOINT = "/events/eventId";
    private static final String ADD_EVENT_ENDPOINT = "/events/";
    private static final String DELETE_EVENT_ENDPOINT = "/events/eventId";
    private static final String UPDATE_EVENT_ENDPOINT = "/events/eventId";

    private final ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public EventData getEventData(String eventId) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GET_EVENT_ENDPOINT.replace("eventId", eventId));
        EventData eventData;
        try {
            eventData = mapper.readValue(response, EventData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return eventData;
    }

    public LambdaEventResponse addEvent(LambdaEventRequest eventRequest) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(eventRequest);
        } catch(JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }
        String response = endpointUtility.postEndpoint(ADD_EVENT_ENDPOINT, request);
        LambdaEventResponse eventResponse;
        try {
            eventResponse = mapper.readValue(response, LambdaEventResponse.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return eventResponse;
    }

    public LambdaEventResponse updateEvent(LambdaEventRequest eventRequest) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(eventRequest);
        } catch(JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }
        String response = endpointUtility.putEndpoint(UPDATE_EVENT_ENDPOINT.replace("eventId", eventRequest.getEventId()), request);
        LambdaEventResponse eventResponse;
        try {
            eventResponse = mapper.readValue(response, LambdaEventResponse.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return eventResponse;
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
