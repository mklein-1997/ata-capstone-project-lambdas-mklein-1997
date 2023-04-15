package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.NotificationService;

import com.kenzie.capstone.service.dao.NotificationDao;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Module(
    includes = DaoModule.class
)
public class ServiceModule {

    @Singleton
    @Provides
    @Inject
    public NotificationService provideNotificationService(@Named("EventDao") NotificationDao notificationDao) {
        return new NotificationService(notificationDao);
    }
}

