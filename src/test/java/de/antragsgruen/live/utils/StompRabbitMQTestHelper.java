package de.antragsgruen.live.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@Component
public class StompRabbitMQTestHelper {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RabbitTemplate template;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("classpath:${antragsgruen.jwt.key.private}")
    private Resource privateKeyFilename;

    public StompTestConnection getStompConnection(int port)
    {
        return new StompTestConnection(port, privateKeyFilename);
    }

    public void sendFileContentToRabbitMQ(String fileName, String routingKey) throws IOException {
        Path path = Path.of("", "src/test/resources");
        String jsonIn = Files.readString(path.resolve(fileName));

        // Use the exact JSON that Antragsgrün sends
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> message = mapper.readValue(jsonIn, new TypeReference<>(){});
        this.template.convertAndSend(exchangeName, routingKey, message);
    }

    public void expectStompToSendFileContent(StompTestConnection session, String fileName) throws IOException
    {
        Path path = Path.of("", "src/test/resources");
        String jsonIn = Files.readString(path.resolve(fileName));

        Map<String, Object> received = session.waitForMessageReceived();

        assertThatJson(received).isEqualTo(jsonIn);
    }
}
