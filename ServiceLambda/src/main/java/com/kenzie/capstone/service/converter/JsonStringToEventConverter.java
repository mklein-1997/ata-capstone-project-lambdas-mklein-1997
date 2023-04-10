package com.kenzie.capstone.service.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import com.kenzie.capstone.service.model.LambdaEventRequest;

public class JsonStringToEventConverter {
    public LambdaEventRequest convert(String body) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            LambdaEventRequest referralRequest = gson.fromJson(body, LambdaEventRequest.class);
            return referralRequest;
        } catch (Exception e) {
            throw new InvalidDataException("Referral could not be deserialized");
        }
    }
}
