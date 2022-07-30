package com.radopeti.actionmonitor.component;

import com.radopeti.actionmonitor.model.ChatMessage;
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
    public void processMsg(ChatMessage chatMessage) {
        logger.info("received message {}", chatMessage.getContent());
        template.convertAndSend("/topic/actions", chatMessage);
    }
}
