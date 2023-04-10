package com.kenzie.capstone.service.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.model.LambdaEventRecord;

import javax.inject.Inject;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CachingEventDao implements EventDao {
    private static final int EVENT_READ_TTL = 60 * 60;
    private static final String EVENT_KEY = "EventKey::%s";

    private final CacheClient cacheClient;
    private final NonCachingEventDao eventDaoInterface;

    @Inject
    public CachingEventDao(CacheClient cacheClient, NonCachingEventDao eventDaoInterface) {
        this.cacheClient = cacheClient;
        this.eventDaoInterface = eventDaoInterface;
    }

    @Override
    public LambdaEventRecord addEvent(LambdaEventRecord event) {
        String key = String.format(EVENT_KEY, event.getEventId());
        cacheClient.invalidate(key);
        return eventDaoInterface.addEvent(event);
    }

    @Override
    public LambdaEventRecord updateEvent(LambdaEventRecord event) {
        String key = String.format(EVENT_KEY, event.getEventId());
        cacheClient.invalidate(key);
        return eventDaoInterface.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(LambdaEventRecord event) {
        boolean result = eventDaoInterface.deleteEvent(event);

        if (result) {
            String key = String.format(EVENT_KEY, event.getEventId());
            cacheClient.invalidate(key);
        }

        return result;
    }

    @Override
    public List<LambdaEventRecord> findByEventId(String eventId) {

        String key = String.format(EVENT_KEY, eventId);

        Optional<String> cache = cacheClient.getValue(key);

        if (cache.isPresent()) {
            return fromJson(cache.get());
        } else {
            List<LambdaEventRecord> records = eventDaoInterface.findByEventId(eventId);
            addToCache(records);
            return records;
        }
    }

    @Override
    public List<LambdaEventRecord> findUsersWithoutEventId() {
        // Look up customer from the data source
        return eventDaoInterface.findUsersWithoutEventId();
    }

    // Create the Gson object with instructions for ZonedDateTime
    private Gson getGson() {
        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(
                ZonedDateTime.class,
                new TypeAdapter<ZonedDateTime>() {
                    @Override
                    public void write(JsonWriter out, ZonedDateTime value) throws IOException {
                        out.value(value.toString());
                    }
                    @Override
                    public ZonedDateTime read(JsonReader in) throws IOException {
                        return ZonedDateTime.parse(in.nextString());
                    }
                }
        ).enableComplexMapKeySerialization();
        // Store this in your class
        return builder.create();
    }

    // Converting out of the cache
    private List<LambdaEventRecord> fromJson(String json) {
        return getGson().fromJson(json, new TypeToken<ArrayList<LambdaEventRecord>>() { }.getType());
    }
    // Setting value
    private void addToCache(List<LambdaEventRecord> records) {
        String eventId = "";

        if (records.size() != 0) {
            eventId = records.get(0).getEventId();
        }

        String key = String.format(EVENT_KEY, eventId);
        cacheClient.setValue(
                key,
                EVENT_READ_TTL,
                getGson().toJson(records)
        );
    }
}
