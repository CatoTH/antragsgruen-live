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
class DebateUserTests {
    @Autowired
    private StompRabbitMQTestHelper testHelper;

    @LocalServerPort
    private int port;

    @Test
    public void sendAndConvertRabbitMQMessage_debate1() throws IOException {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        stompConnection.connectAndWait("installation", "site", "con", "login-1", null);
        stompConnection.subscribe("/user/installation/site/con/login-1/debate");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_debate1_in.json", "debate.installation.site.con");
        testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_debate1_user_out.json");
    }

    /**
     * A debated item with an attached speaking list and voting. Both are objects, not IDs - the widgets
     * need to be able to use them the same way as the ones the REST API delivers.
     */
    @Test
    public void sendAndConvertRabbitMQMessage_debate2() throws IOException {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        stompConnection.connectAndWait("installation", "site", "con", "login-1", null);
        stompConnection.subscribe("/user/installation/site/con/login-1/debate");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_debate2_in.json", "debate.installation.site.con", "de");
        testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_debate2_user_out.json");
    }

    /**
     * Hint on the user IDs: connections are never closed and the Spring context is shared by all
     * tests, so a user whose language is stated here must not be used by any other test - the user
     * registry keeps the principal of the session that connected first.
     *
     * An event of a multi-language consultation carries every language it is held in; each user gets
     * the one they are reading Antragsgrün in, as stated by their JWT.
     */
    @Test
    public void sendAndConvertRabbitMQMessage_debate3_readerLanguage() throws IOException {
        StompTestConnection germanReader = testHelper.getStompConnection(port);
        germanReader.connectAndWait("installation", "site", "con", "login-101", null, "de");
        germanReader.subscribe("/user/installation/site/con/login-101/debate");

        StompTestConnection englishReader = testHelper.getStompConnection(port);
        englishReader.connectAndWait("installation", "site", "con", "login-102", null, "en");
        englishReader.subscribe("/user/installation/site/con/login-102/debate");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_debate3_in.json", "debate.installation.site.con", "de");

        testHelper.expectStompToSendFileContent(germanReader, "sendAndConvertRabbitMQMessage_debate3_user_de_out.json");
        testHelper.expectStompToSendFileContent(englishReader, "sendAndConvertRabbitMQMessage_debate3_user_en_out.json");
    }

    /**
     * A user whose language the event does not contain - or who did not state one at all - gets the
     * message's default language.
     */
    @Test
    public void sendAndConvertRabbitMQMessage_debate3_fallsBackToDefaultLanguage() throws IOException {
        StompTestConnection frenchReader = testHelper.getStompConnection(port);
        frenchReader.connectAndWait("installation", "site", "con", "login-103", null, "fr");
        frenchReader.subscribe("/user/installation/site/con/login-103/debate");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_debate3_in.json", "debate.installation.site.con", "en");
        testHelper.expectStompToSendFileContent(frenchReader, "sendAndConvertRabbitMQMessage_debate3_user_en_out.json");
    }
}
