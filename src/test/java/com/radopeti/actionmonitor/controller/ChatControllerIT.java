package com.radopeti.actionmonitor.controller;


import com.radopeti.actionmonitor.domain.Message;
import com.radopeti.actionmonitor.model.ChatMessage;
import com.radopeti.actionmonitor.repository.MessageRepository;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.testcontainers.containers.PostgreSQLContainer;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@ContextConfiguration(initializers = { ChatControllerIT.Initializer.class })
public class ChatControllerIT {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:13.1-alpine")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            postgreSQLContainer.start();
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.jpa.hibernate.ddl-auto=create-drop"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @LocalServerPort
    private int port;

    @Autowired
    MessageRepository messageRepository;

    private String CHAT_URL;

    @BeforeEach
    public void setup() {
        CHAT_URL = "ws://localhost:" + port + "/chat-ws";
    }

    @Test
    public void testSendChatMessage() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
        //given
        WebSocketStompClient chatClient = new WebSocketStompClient(
                new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient())))
        );
        chatClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = chatClient
                .connect(CHAT_URL, new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);

        final ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("Hi!");

        //when
        stompSession.send("/app/chat", chatMessage);

        Thread.sleep(1000);

        //then message is saved to database
        final List<Message> messages = messageRepository.findAll();
        assertEquals(1, messages.size());
        final Message message = messages.get(0);
        assertEquals(chatMessage.getContent(), message.getContent());
        assertNotNull(message.getId());
        assertNotNull(message.getUpdatedAt());
        assertNotNull( message.getCreatedAt());
    }
}
