package com.kenzie.capstone.service.caching;

import com.kenzie.capstone.service.dependency.DaggerServiceComponent;
import com.kenzie.capstone.service.exceptions.InvalidDataException;
import redis.clients.jedis.Jedis;

import java.util.Optional;

public class CacheClient {
    public CacheClient() {}


    public void setValue(String key, int seconds, String value) {
        // Check for non-null key
        // Set the value in the cache
        checkNonNullKey(key);
        Jedis cache = DaggerServiceComponent.create().provideJedis();
        cache.setex(key, seconds, value);
        cache.close();
    }

    public Optional<String> getValue(String key) {
        // Check for non-null key
        // Retrieves the Optional values from the cache
        checkNonNullKey(key);
        Jedis cache = DaggerServiceComponent.create().provideJedis();
        Optional<String> obj = Optional.ofNullable(cache.get(key));
        cache.close();

        return obj;
    }

    public void invalidate(String key) {
        // Check for non-null key
        // Delete the key
        checkNonNullKey(key);
        Jedis cache = DaggerServiceComponent.create().provideJedis();
        cache.del(key);
        cache.close();
    }
    private void checkNonNullKey(String key) {
        // Ensure the key isn't null
        // What should you do if the key *is* null?
        if (key == null) {
            throw new InvalidDataException("Null key passed into cache");
        }
    }

}
