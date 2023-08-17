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
    public void tryToConnectWithoutAdminRole() throws IOException {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        List<String> roles = new ArrayList<>();
        roles.add("WRONG_ROLE");

        stompConnection.connectAndWait("site", "con", "login-1", roles);
        FutureTask<String> onError = stompConnection.subscribeAndExpectError("/admin/site/con/login-1/speech");
        try {
            String message = onError.get(5, TimeUnit.SECONDS);
            assertThat(message).isEqualTo("Forbidden to subscribe to this destination");
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}