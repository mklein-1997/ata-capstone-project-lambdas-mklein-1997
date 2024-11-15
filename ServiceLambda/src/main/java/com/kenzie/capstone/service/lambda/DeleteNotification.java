package com.kenzie.capstone.service.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.NotificationService;
import com.kenzie.capstone.service.converter.JsonStringToArrayListStringsConverter;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.dependency.ServiceComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DeleteNotification implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    static final Logger log = LogManager.getLogger();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        // Logging the request json to make debugging easier.
        log.info(gson.toJson(input));

        ServiceComponent serviceComponent = DaggerServiceComponent.create();
        NotificationService notificationService = serviceComponent.provideNotificationService();

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            String eventId = gson.fromJson(input.getBody(), String.class);
            boolean allDeleted = notificationService.deleteNotification(eventId);

            return response
                    .withStatusCode(200)
                    .withBody(gson.toJson(allDeleted));
        } catch(InvalidDataException e){
            return response
                    .withStatusCode(400)
                    .withBody(gson.toJson(e.errorPayload()));
        }

    }
}
