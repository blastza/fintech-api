package com.platform_domain.fintech_api.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic transactionsTopic() {
        return TopicBuilder.name("transactions")
                .partitions(3) // 3 consumers can read in parallel
                .replicas(1)    // 1 replica fine for local dev, use 3 in production
                .build();
    }

    public NewTopic fraudAlertsTopic() {
        return TopicBuilder.name("fraud-alerts")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
