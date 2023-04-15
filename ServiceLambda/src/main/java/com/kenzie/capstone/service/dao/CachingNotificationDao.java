package com.kenzie.capstone.service.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.model.NotificationRecord;

import javax.inject.Inject;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CachingNotificationDao implements NotificationDao {
    private static final int EVENT_READ_TTL = 60 * 60;
    private static final String EVENT_KEY = "EventKey::%s";

    private final CacheClient cacheClient;
    private final NonCachingNotificationDao eventDaoInterface;

    @Inject
    public CachingNotificationDao(CacheClient cacheClient, NonCachingNotificationDao eventDaoInterface) {
        this.cacheClient = cacheClient;
        this.eventDaoInterface = eventDaoInterface;
    }

    @Override
    public NotificationRecord addEvent(NotificationRecord event) {
        String key = String.format(EVENT_KEY, event.getEventId());
        cacheClient.invalidate(key);
        return eventDaoInterface.addEvent(event);
    }

    @Override
    public NotificationRecord updateEvent(NotificationRecord event) {
        String key = String.format(EVENT_KEY, event.getEventId());
        cacheClient.invalidate(key);
        return eventDaoInterface.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(NotificationRecord event) {
        boolean result = eventDaoInterface.deleteEvent(event);

        if (result) {
            String key = String.format(EVENT_KEY, event.getEventId());
            cacheClient.invalidate(key);
        }

        return result;
    }

    @Override
    public List<NotificationRecord> findByEventId(String eventId) {

        String key = String.format(EVENT_KEY, eventId);

        Optional<String> cache = cacheClient.getValue(key);

        if (cache.isPresent()) {
            return fromJson(cache.get());
        } else {
            List<NotificationRecord> records = eventDaoInterface.findByEventId(eventId);
            addToCache(records);
            return records;
        }
    }

    @Override
    public List<NotificationRecord> findUsersWithoutEventId() {
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
    private List<NotificationRecord> fromJson(String json) {
        return getGson().fromJson(json, new TypeToken<ArrayList<NotificationRecord>>() { }.getType());
    }
    // Setting value
    private void addToCache(List<NotificationRecord> records) {
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
