package de.antragsgruen.live;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class StaticFilesTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void servesUmdJsLibrary() {
        // Hint: this test case only works after npm install / npm ci has been called
        String uri = "http://localhost:" + port + "/stomp.umd.min.js";
        assertThat(this.restTemplate.getForObject(uri, String.class)).contains("StompHeaders");
    }
}
