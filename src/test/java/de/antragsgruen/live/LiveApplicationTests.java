package de.antragsgruen.live;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.antragsgruen.live.utils.StompTestConnection;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

	@Value("classpath:${antragsgruen.jwt.key.private}")
	private Resource privateKeyFilename;

	@Test
	void contextLoads() {
	}

	@Test
	public void indexShouldShowDummyMessage() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/", String.class)).contains("Hello Antragsgrün-user");
	}

	@Test
	public void sendRabbitMq() throws IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeySpecException {
		StompTestConnection stompSessionTestHandler = new StompTestConnection(port, privateKeyFilename);
		FutureTask<Object> onConnect = stompSessionTestHandler.connect("site", "con", 1);

		try {
			onConnect.get(5, TimeUnit.SECONDS);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		} catch (TimeoutException e) {
			throw new RuntimeException("Could not connect to STOMP within a reasonable amount of time");
		}

		// Use the exact JSON that Antragsgrün sends
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> message = mapper.readValue("{\"username\": \"Test\"}", new TypeReference<>(){});
		this.template.convertAndSend(exchangeName, "user.site.con.1", message);
	}
}
