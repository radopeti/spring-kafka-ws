package com.radopeti.actionmonitor.controller;

import com.radopeti.actionmonitor.model.ChatMessage;
import com.radopeti.actionmonitor.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KafkaService kafkaService;

    public ChatController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public String receiveChatMessage(ChatMessage message) {
        logger.info(message.getContent());
        kafkaService.sendMsg(message.getContent());
        return message.getContent();
    }
}
