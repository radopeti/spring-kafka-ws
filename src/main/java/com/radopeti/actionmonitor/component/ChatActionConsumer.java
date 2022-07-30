package com.radopeti.actionmonitor.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatActionConsumer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SimpMessagingTemplate template;

    public ChatActionConsumer(SimpMessagingTemplate template) {
        this.template = template;
    }

    @KafkaListener(topics = "${app.kafka.topic.actions.name}")
    public void processMsg(String content) {
        logger.info("received message {}", content);
        template.convertAndSend("/topic/actions", content);
    }
}
