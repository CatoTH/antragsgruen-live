package de.antragsgruen.live;

import de.antragsgruen.live.utils.StompRabbitMQTestHelper;
import de.antragsgruen.live.utils.StompTestConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthenticationErrorTests {
    @Autowired
    private StompRabbitMQTestHelper testHelper;

    @LocalServerPort
    private int port;

    @Test
    public void tryToConnectToIncorrectTopic1() {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        stompConnection.connectAndWait("installation", "site", "con", "login-1", null);
        FutureTask<String> onError = stompConnection.subscribeAndExpectError("/user/installation/site/othercon/login-1/speech");
        try {
            String message = onError.get(5, TimeUnit.SECONDS);
            assertThat(message).isEqualTo("Forbidden to subscribe to this destination");
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void tryToConnectToIncorrectTopic2() {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        stompConnection.connectAndWait("installation", "site", "con", "login-2", null);
        FutureTask<String> onError = stompConnection.subscribeAndExpectError("/user/installation/site/con/login-1/speech");
        try {
            String message = onError.get(5, TimeUnit.SECONDS);
            assertThat(message).isEqualTo("Forbidden to subscribe to this destination");
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void tryToConnectToIncorrectInstallation() {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        FutureTask<String> onError = stompConnection.connectAndExpectError("other", "site", "con", "login-1", null);
        try {
            String message = onError.get(5, TimeUnit.SECONDS);
            assertThat(message).isEqualTo("Could not authenticate JWT: Invalid installation id: other");
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}