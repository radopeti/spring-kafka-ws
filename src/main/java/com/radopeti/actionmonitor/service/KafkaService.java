package com.radopeti.actionmonitor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Value("${app.kafka.topic.actions.name}")
    private String actionsTopicName;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMsg(String msg) {
        logger.info("Sending content {} to topic {}", msg, actionsTopicName);
        kafkaTemplate.send(actionsTopicName, msg);
    }
}
