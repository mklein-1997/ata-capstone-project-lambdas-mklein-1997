package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.NotificationService;

import dagger.Component;
import redis.clients.jedis.Jedis;

import javax.inject.Singleton;

/**
 * Declares the dependency roots that Dagger will provide.
 */
@Singleton
@Component(modules = {DaoModule.class, CachingModule.class, ServiceModule.class})
public interface ServiceComponent {
    NotificationService provideNotificationService();
    Jedis provideJedis();
}
