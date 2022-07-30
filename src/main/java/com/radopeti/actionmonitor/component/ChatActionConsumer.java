package com.radopeti.actionmonitor.component;

import com.radopeti.actionmonitor.domain.Message;
import com.radopeti.actionmonitor.model.ChatAction;
import com.radopeti.actionmonitor.model.ChatMessage;
import com.radopeti.actionmonitor.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
public class ChatActionConsumer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SimpMessagingTemplate template;

    private final MessageRepository messageRepository;

    public ChatActionConsumer(SimpMessagingTemplate template, MessageRepository messageRepository) {
        this.template = template;
        this.messageRepository = messageRepository;
    }

    @KafkaListener(topics = "${app.kafka.topic.actions.name}")
    @Transactional
    public void processMsg(ChatMessage chatMessage) {
        logger.info("received message {}", chatMessage.getContent());

        final Message message = new Message();
        message.setContent(chatMessage.getContent());
        final Message savedMessage = messageRepository.save(message);

        final ChatAction chatAction = new ChatAction();
        chatAction.setId(savedMessage.getId());
        chatAction.setAction("insert");
        chatAction.setTime(LocalDateTime.now());

        logger.info("sending action {}", chatAction);
        template.convertAndSend("/topic/actions", chatAction);
    }
}
