package com.kenzie.capstone.service.dependency;


import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.dao.CachingNotificationDao;
import com.kenzie.capstone.service.dao.NonCachingNotificationDao;
import com.kenzie.capstone.service.dao.NotificationDao;
import com.kenzie.capstone.service.util.DynamoDbClientProvider;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Provides DynamoDBMapper instance to DAO classes.
 */
@Module
public class DaoModule {

    @Singleton
    @Provides
    @Named("DynamoDBMapper")
    public DynamoDBMapper provideDynamoDBMapper() {
        return new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient());
    }

    @Singleton
    @Provides
    @Named("NonCachingEventDao")
    @Inject
    public NonCachingNotificationDao provideNonCachingNotificationDao(@Named("DynamoDBMapper") DynamoDBMapper mapper) {
        return new NonCachingNotificationDao(mapper);
    }

    @Singleton
    @Provides
    @Named("EventDao")
    @Inject
    public NotificationDao provideNotificationDao(
            @Named("CacheClient")CacheClient cacheClient,
            @Named("NonCachingEventDao") NonCachingNotificationDao nonCachingEventDao) {
        return new CachingNotificationDao(cacheClient, nonCachingEventDao);
    }

}
