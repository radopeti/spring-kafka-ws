package com.radopeti.actionmonitor.service;

import com.radopeti.actionmonitor.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatActionProducer {

    @Value("${app.kafka.topic.actions.name}")
    private String actionsTopicName;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KafkaTemplate<String, ChatMessage> kafkaTemplate;

    public ChatActionProducer(KafkaTemplate<String, ChatMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produce(ChatMessage message) {
        logger.info("Sending content {} to topic {}", message, actionsTopicName);
        kafkaTemplate.send(actionsTopicName, message);
    }
}
