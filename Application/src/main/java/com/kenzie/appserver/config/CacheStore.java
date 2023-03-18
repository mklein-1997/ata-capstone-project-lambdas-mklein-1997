package com.kenzie.appserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.w3c.dom.events.Event;

import java.util.concurrent.TimeUnit;

public class CacheStore {
    private Cache<String, Event> cache;

    public CacheStore(int expiry, TimeUnit timeUnit) {
            // initialize the cache
        this.cache = CacheBuilder.newBuilder()
                    .expireAfterWrite(expiry, timeUnit)
                    .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                    .build();
        }

    public Event get(String key) {
        // Write your code here
        // Retrieve and return the Event
        return cache.getIfPresent(key);
    }

    public void delete(String key) {
        // Write your code here
        // Invalidate/evict the Event from cache
        cache.invalidate(key);
    }

    public void add(String key, Event value) {
        // Write your code here
        // Add Event to cache
        cache.put(key, value);
    }
}
