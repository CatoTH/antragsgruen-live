package de.antragsgruen.live;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.antragsgruen.live.utils.StompTestConnection;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
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
	public void sendAndConvertRabbitMQMessage_speech1() throws IOException {
		StompTestConnection stompConnection = new StompTestConnection(port, privateKeyFilename);

		stompConnection.connectAndWait("site", "con", "login-1");
		stompConnection.subscribeAndWait("/user/site/con/login-1/speech");

		sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech1_in.json", "speech.site.con");
		expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_speech1_user_out.json");
	}

	@Test
	public void sendAndConvertRabbitMQMessage_speech2() throws IOException {
		StompTestConnection stompConnection = new StompTestConnection(port, privateKeyFilename);

		stompConnection.connectAndWait("site", "con", "anonymous-qVnRU4NFICsBGtnWfi0dzGgWcKGlQoiN");
		stompConnection.subscribeAndWait("/user/site/con/anonymous-qVnRU4NFICsBGtnWfi0dzGgWcKGlQoiN/speech");

		sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech2_in.json", "speech.site.con");
		expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_speech2_user_out.json");
	}

	@Test
	public void sendAndConvertRabbitMQMessage_speech3() throws IOException {
		StompTestConnection stompConnection = new StompTestConnection(port, privateKeyFilename);

		stompConnection.connectAndWait("site", "con", "login-1");
		stompConnection.subscribeAndWait("/user/site/con/login-1/speech");

		sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech3_in.json", "speech.site.con");
		expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_speech3_user_out.json");
	}

	@Test
	public void servesUmdJsLibrary() {
		// Hint: this test case only works after npm install / npm ci has been called
		String uri = "http://localhost:" + port + "/stomp.umd.min.js";
		assertThat(this.restTemplate.getForObject(uri, String.class)).contains("StompHeaders");
	}

	private void sendFileContentToRabbitMQ(String fileName, String routingKey) throws IOException {
		Path path = Path.of("", "src/test/resources");
		String jsonIn = Files.readString(path.resolve(fileName));

		// Use the exact JSON that Antragsgrün sends
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> message = mapper.readValue(jsonIn, new TypeReference<>(){});
		this.template.convertAndSend(exchangeName, routingKey, message);
	}

	private void expectStompToSendFileContent(StompTestConnection session, String fileName) throws IOException
	{
		Path path = Path.of("", "src/test/resources");
		String jsonIn = Files.readString(path.resolve(fileName));

		Map<String, Object> received = session.waitForMessageReceived();

		assertThatJson(received).isEqualTo(jsonIn);
	}
}
