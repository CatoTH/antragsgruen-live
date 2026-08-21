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

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_debate2_in.json", "debate.installation.site.con");
        testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_debate2_user_out.json");
    }
}
