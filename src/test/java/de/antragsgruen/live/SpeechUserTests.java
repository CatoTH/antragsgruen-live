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
		stompConnection.subscribe("/user/installation/site/con/login-1/speech/de");

		testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech1_in.json", "speech.installation.site.con");
		testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_speech1_user_out.json");
	}

	@Test
	public void sendAndConvertRabbitMQMessage_speech1_v4_16() throws IOException {
		StompTestConnection stompConnection = testHelper.getStompConnection(port);

		stompConnection.connectAndWait("installation", "site", "con", "login-1", null);
		stompConnection.subscribe("/user/installation/site/con/login-1/speech/de");

		testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech1_in_4.16.json", "speech.installation.site.con");
		testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_speech1_user_out.json");
	}

	@Test
	public void sendAndConvertRabbitMQMessage_speech2() throws IOException {
		StompTestConnection stompConnection = testHelper.getStompConnection(port);

		stompConnection.connectAndWait("installation", "site", "con", "anonymous-qVnRU4NFICsBGtnWfi0dzGgWcKGlQoiN", null);
		stompConnection.subscribe("/user/installation/site/con/anonymous-qVnRU4NFICsBGtnWfi0dzGgWcKGlQoiN/speech/de");

		testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech2_in.json", "speech.installation.site.con");
		testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_speech2_user_out.json");
	}

	@Test
	public void sendAndConvertRabbitMQMessage_speech3() throws IOException {
		StompTestConnection stompConnection = testHelper.getStompConnection(port);

		stompConnection.connectAndWait("installation", "site", "con", "login-1", null);
		stompConnection.subscribe("/user/installation/site/con/login-1/speech/de");

		testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech3_in.json", "speech.installation.site.con");
		testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_speech3_user_out.json");
	}

	/**
	 * Subqueue names are part of the user-facing payload, so a multi-language consultation sends
	 * them in every language and each reader gets their own.
	 *
	 * The two readers are the same user - one person with two browser tabs open in two languages.
	 * They share a user ID and differ only in the destination they subscribed to, which is why the
	 * language has to be taken from there.
	 */
	@Test
	public void sendAndConvertRabbitMQMessage_speech4_readerLanguage() throws IOException {
		StompTestConnection germanReader = testHelper.getStompConnection(port);
		germanReader.connectAndWait("installation", "site", "con", "login-121", null);
		germanReader.subscribe("/user/installation/site/con/login-121/speech/de");

		StompTestConnection englishReader = testHelper.getStompConnection(port);
		englishReader.connectAndWait("installation", "site", "con", "login-121", null);
		englishReader.subscribe("/user/installation/site/con/login-121/speech/en");

		testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech4_in.json", "speech.installation.site.con", "de");

		testHelper.expectStompToSendFileContent(germanReader, "sendAndConvertRabbitMQMessage_speech4_user_de_out.json");
		testHelper.expectStompToSendFileContent(englishReader, "sendAndConvertRabbitMQMessage_speech4_user_en_out.json");
	}

	/**
	 * A reader whose language the event does not contain, and one whose client does not state a
	 * language at all (Antragsgrün <= 4.17): both get the language the message declares as its
	 * default.
	 */
	@Test
	public void sendAndConvertRabbitMQMessage_speech4_fallsBackToDefaultLanguage() throws IOException {
		StompTestConnection frenchReader = testHelper.getStompConnection(port);
		frenchReader.connectAndWait("installation", "site", "con", "login-122", null);
		frenchReader.subscribe("/user/installation/site/con/login-122/speech/fr");

		StompTestConnection unstatedReader = testHelper.getStompConnection(port);
		unstatedReader.connectAndWait("installation", "site", "con", "login-123", null);
		unstatedReader.subscribe("/user/installation/site/con/login-123/speech");

		testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech4_in.json", "speech.installation.site.con", "en");

		testHelper.expectStompToSendFileContent(frenchReader, "sendAndConvertRabbitMQMessage_speech4_user_en_out.json");
		testHelper.expectStompToSendFileContent(unstatedReader, "sendAndConvertRabbitMQMessage_speech4_user_en_out.json");
	}
}
