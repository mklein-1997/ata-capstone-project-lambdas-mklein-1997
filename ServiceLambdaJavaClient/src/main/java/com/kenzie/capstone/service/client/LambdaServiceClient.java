package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.EventData;


public class LambdaServiceClient {

    private static final String GET_EVENT_ENDPOINT = "events/{eventId}";
    private static final String SET_EVENT_ENDPOINT = "events";

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
}
