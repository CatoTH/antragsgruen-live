package de.antragsgruen.live;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LiveApplicationTests {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private RabbitTemplate template;

	@Value("${rabbitmq.exchange.name}")
	private String exchangeName;

	@Test
	void contextLoads() {
	}

	@Test
	public void indexShouldShowDummyMessage() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/", String.class)).contains("Hello Antragsgrün-user");
	}

	@Test
	public void sendRabbitMq() throws JsonProcessingException, InterruptedException {
		StompSessionTestHandler stompSessionTestHandler = new StompSessionTestHandler();
		stompSessionTestHandler.connect(port);

		System.out.println("Connecting");

		Thread.sleep(5000);

		// Use the exact JSON that Antragsgrün sends
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> message = mapper.readValue("{\"username\": \"Test\"}", new TypeReference<>(){});
		this.template.convertAndSend(exchangeName, "user.sitd.con.testuser", message);
	}
}
