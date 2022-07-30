package com.radopeti.actionmonitor.controller;

import com.radopeti.actionmonitor.model.ChatMessage;
import com.radopeti.actionmonitor.service.ChatActionProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ChatActionProducer actionProducer;

    public ChatController(ChatActionProducer kafkaService) {
        this.actionProducer = kafkaService;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public String receiveChatMessage(ChatMessage message) {
        logger.info("chat-message received with content {}", message.getContent());
        actionProducer.produce(message);
        return message.getContent();
    }
}
