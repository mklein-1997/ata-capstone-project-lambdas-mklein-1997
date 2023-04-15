package com.kenzie.appserver.config;

import com.kenzie.capstone.service.client.NotificationServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LambdaServiceClientConfiguration {

    @Bean
    public NotificationServiceClient lambdaServiceClient() {
        return new NotificationServiceClient();
    }
}
