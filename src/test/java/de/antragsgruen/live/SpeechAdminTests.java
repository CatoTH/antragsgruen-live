package de.antragsgruen.live;

import de.antragsgruen.live.utils.StompRabbitMQTestHelper;
import de.antragsgruen.live.utils.StompTestConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SpeechAdminTests {
    @Autowired
    private StompRabbitMQTestHelper testHelper;

    @LocalServerPort
    private int port;

    @Test
    public void tryToConnectWithoutAdminRole() {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        stompConnection.connectAndWait("installation", "site", "con", "login-1", getRoles("WRONG_ROLE"));
        FutureTask<String> onError = stompConnection.subscribeAndExpectError("/admin/installation/site/con/login-1/speech");
        try {
            String message = onError.get(5, TimeUnit.SECONDS);
            assertThat(message).isEqualTo("Forbidden to subscribe to this destination");
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void sendAndConvertRabbitMQMessage_speech1() throws IOException {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        stompConnection.connectAndWait("installation", "site", "con", "login-1", getRoles("ROLE_SPEECH_ADMIN"));
        stompConnection.subscribe("/admin/installation/site/con/login-1/speech");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech1_in.json", "speech.installation.site.con");
        testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_speech1_admin_out.json");
    }

    @Test
    public void sendAndConvertRabbitMQMessage_speech1_v4_16() throws IOException {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        stompConnection.connectAndWait("installation", "site", "con", "login-1", getRoles("ROLE_SPEECH_ADMIN"));
        stompConnection.subscribe("/admin/installation/site/con/login-1/speech");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech1_in_4.16.json", "speech.installation.site.con");
        testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_speech1_admin_out.json");
    }

    /**
     * Hint on the user IDs: connections are never closed and the Spring context is shared by all
     * tests, so a user whose language is stated here must not be used by any other test - the user
     * registry keeps the principal of the session that connected first.
     *
     * The speaking list has one reader-dependent string so far - the name of the list that would be
     * deactivated by activating this one. Antragsgrün sends every language of the consultation and
     * each moderator gets the one they are reading it in.
     */
    @Test
    public void sendAndConvertRabbitMQMessage_speech4_readerLanguage() throws IOException {
        StompTestConnection germanAdmin = testHelper.getStompConnection(port);
        germanAdmin.connectAndWait("installation", "site", "con", "login-111", getRoles("ROLE_SPEECH_ADMIN"), "de");
        germanAdmin.subscribe("/admin/installation/site/con/login-111/speech");

        StompTestConnection englishAdmin = testHelper.getStompConnection(port);
        englishAdmin.connectAndWait("installation", "site", "con", "login-112", getRoles("ROLE_SPEECH_ADMIN"), "en");
        englishAdmin.subscribe("/admin/installation/site/con/login-112/speech");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech4_in.json", "speech.installation.site.con", "de");

        testHelper.expectStompToSendFileContent(germanAdmin, "sendAndConvertRabbitMQMessage_speech4_admin_de_out.json");
        testHelper.expectStompToSendFileContent(englishAdmin, "sendAndConvertRabbitMQMessage_speech4_admin_en_out.json");
    }

    /**
     * Antragsgrün <= 4.17 sends that string in one language only, without saying which - every
     * moderator gets it, whichever language they are reading the site in.
     */
    @Test
    public void sendAndConvertRabbitMQMessage_speech4_v4_17() throws IOException {
        StompTestConnection englishAdmin = testHelper.getStompConnection(port);
        englishAdmin.connectAndWait("installation", "site", "con", "login-113", getRoles("ROLE_SPEECH_ADMIN"), "en");
        englishAdmin.subscribe("/admin/installation/site/con/login-113/speech");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_speech4_in_4.17.json", "speech.installation.site.con");
        testHelper.expectStompToSendFileContent(englishAdmin, "sendAndConvertRabbitMQMessage_speech4_admin_de_out.json");
    }

    private List<String> getRoles(String role) {
        List<String> roles = new ArrayList<>();
        roles.add(role);

        return roles;
    }
}