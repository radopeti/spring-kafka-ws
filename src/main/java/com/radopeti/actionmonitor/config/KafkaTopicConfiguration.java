package com.radopeti.actionmonitor.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    @Value("${app.kafka.topic.actions.name}")
    private String actionsTopicName;

    @Bean
    public NewTopic actionsTopic() {
        return TopicBuilder
                .name(actionsTopicName)
                .replicas(1)
                .partitions(1)
                .build();
    }
}
