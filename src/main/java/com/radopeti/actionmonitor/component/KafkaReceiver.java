package com.radopeti.actionmonitor.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaReceiver {

    @Value("${app.kafka.topic.actions.name}")
    private String actionsTopicName;

    private final SimpMessagingTemplate template;

    public KafkaReceiver(SimpMessagingTemplate template) {
        this.template = template;
    }

    @KafkaListener(topics = "test-topic")
    public void processMsg(String content) {
        System.out.println(content);
        template.convertAndSend("/topic/actions", content);
    }
}
