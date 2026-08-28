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
	public void sendAndConvertRabbitMQMessage_speech1_v4_16() throws IOException {
		StompTestConnection stompConnection = testHelper.getStompConnection(port);

		stompConnection.connectAndWait("installation", "site", "con", "login-1", null);
		stompConnection.subscribe("/user/installation/site/con/login-1/speech");

		testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech1_in_4.16.json", "speech.installation.site.con");
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

	/**
	 * Subqueue names are part of the user-facing payload, so a multi-language consultation sends
	 * them in every language and each reader gets their own.
	 *
	 * Hint on the user IDs: connections are never closed and the Spring context is shared by all
	 * tests, so a user whose language is stated here must not be used by any other test - the user
	 * registry keeps the principal of the session that connected first.
	 */
	@Test
	public void sendAndConvertRabbitMQMessage_speech4_readerLanguage() throws IOException {
		StompTestConnection germanReader = testHelper.getStompConnection(port);
		germanReader.connectAndWait("installation", "site", "con", "login-121", null, "de");
		germanReader.subscribe("/user/installation/site/con/login-121/speech");

		StompTestConnection englishReader = testHelper.getStompConnection(port);
		englishReader.connectAndWait("installation", "site", "con", "login-122", null, "en");
		englishReader.subscribe("/user/installation/site/con/login-122/speech");

		testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech4_in.json", "speech.installation.site.con", "de");

		testHelper.expectStompToSendFileContent(germanReader, "sendAndConvertRabbitMQMessage_speech4_user_de_out.json");
		testHelper.expectStompToSendFileContent(englishReader, "sendAndConvertRabbitMQMessage_speech4_user_en_out.json");
	}
}
