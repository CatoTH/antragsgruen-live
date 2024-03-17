package de.antragsgruen.live;

import de.antragsgruen.live.utils.StompRabbitMQTestHelper;
import de.antragsgruen.live.utils.StompTestConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SpeechUserTests {
	@Autowired
	private StompRabbitMQTestHelper testHelper;

	@LocalServerPort
	private int port;

	@Test
	public void sendAndConvertRabbitMQMessage_speech1() throws IOException {
		StompTestConnection stompConnection = testHelper.getStompConnection(port);

		stompConnection.connectAndWait("installation", "site", "con", "login-1", null);
		stompConnection.subscribe("/user/installation/site/con/login-1/speech");

		testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech1_in.json", "speech.installation.site.con");
		testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_speech1_user_out.json");
	}

	@Test
	public void sendAndConvertRabbitMQMessage_speech2() throws IOException {
		StompTestConnection stompConnection = testHelper.getStompConnection(port);

		stompConnection.connectAndWait("installation", "site", "con", "anonymous-qVnRU4NFICsBGtnWfi0dzGgWcKGlQoiN", null);
		stompConnection.subscribe("/user/installation/site/con/anonymous-qVnRU4NFICsBGtnWfi0dzGgWcKGlQoiN/speech");

		testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech2_in.json", "speech.installation.site.con");
		testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_speech2_user_out.json");
	}

	@Test
	public void sendAndConvertRabbitMQMessage_speech3() throws IOException {
		StompTestConnection stompConnection = testHelper.getStompConnection(port);

		stompConnection.connectAndWait("installation", "site", "con", "login-1", null);
		stompConnection.subscribe("/user/installation/site/con/login-1/speech");

		testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech3_in.json", "speech.installation.site.con");
		testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_speech3_user_out.json");
	}
}
