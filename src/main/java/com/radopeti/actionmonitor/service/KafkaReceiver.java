package com.radopeti.actionmonitor.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaReceiver {

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
