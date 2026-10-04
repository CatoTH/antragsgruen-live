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
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class VotingTests {
    private static final String VOTING_ROUTING_KEY = "voting.installation.site.con";

    @Autowired
    private StompRabbitMQTestHelper testHelper;

    @LocalServerPort
    private int port;

    private List<String> getRoles(String role) {
        List<String> roles = new ArrayList<>();
        roles.add(role);
        return roles;
    }

    /**
     * A participant is given the section published for everyone, plus their own state - which the
     * event carries for each user separately, and which the proxy hands out one entry of.
     */
    @Test
    public void votingUserGetsOwnState() throws IOException {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        stompConnection.connectAndWait("installation", "site", "con", "login-2", null);
        stompConnection.subscribe("/user/installation/site/con/login-2/voting/de");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_voting1_in.json", VOTING_ROUTING_KEY, "de");
        testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_voting1_user_de_out.json");
    }

    /**
     * Somebody the event knows nothing about: they have not voted, and the voting cannot name who is
     * entitled to it, so they are described by the state it states for everybody else.
     */
    @Test
    public void votingUserWithoutOwnStateGetsTheDefaultOne() throws IOException {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        stompConnection.connectAndWait("installation", "site", "con", "login-999", null);
        stompConnection.subscribe("/user/installation/site/con/login-999/voting/de");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_voting1_in.json", VOTING_ROUTING_KEY, "de");
        testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_voting1_unknown_user_out.json");
    }

    /**
     * The strings of a voting - the answers above all - are published in every language of the
     * consultation, and each reader gets the one they are browsing in. The sections themselves are
     * opaque to this server, so the localized strings within them are found by their shape.
     */
    @Test
    public void votingIsDeliveredInTheReaderLanguage() throws IOException {
        StompTestConnection englishReader = testHelper.getStompConnection(port);
        englishReader.connectAndWait("installation", "site", "con", "login-2", null);
        englishReader.subscribe("/user/installation/site/con/login-2/voting/en");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_voting1_in.json", VOTING_ROUTING_KEY, "de");
        testHelper.expectStompToSendFileContent(englishReader, "sendAndConvertRabbitMQMessage_voting1_user_en_out.json");
    }

    /**
     * The administration is given what only it may see on top of what everybody gets - the single
     * votes of this voting, its settings and its log.
     */
    @Test
    public void votingAdminGetsTheAdminOnlySection() throws IOException {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        stompConnection.connectAndWait("installation", "site", "con", "login-1", getRoles("ROLE_VOTING_ADMIN"));
        stompConnection.subscribe("/admin/installation/site/con/login-1/voting/de");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_voting1_in.json", VOTING_ROUTING_KEY, "de");
        testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_voting1_admin_out.json");
    }

    /**
     * The guarantee this whole split exists for: what is published as being for the administration
     * does not reach a participant - no matter what it contains. Here it is the names of the people
     * who voted, which this voting shows to its administrators but to nobody else.
     */
    @Test
    public void votesForTheAdministrationDoNotReachAParticipant() throws IOException {
        StompTestConnection participant = testHelper.getStompConnection(port);
        participant.connectAndWait("installation", "site", "con", "login-2", null);
        participant.subscribe("/user/installation/site/con/login-2/voting/de");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_voting1_in.json", VOTING_ROUTING_KEY, "de");

        Map<String, Object> received = participant.waitForMessageReceived();
        assertThat(received.toString())
                .as("A participant is not told who voted")
                .doesNotContain("testuser@example.org")
                .doesNotContain("fixeddata@example.org");
    }

    /**
     * A voting of a consultation held in one language only, exactly as Antragsgrün publishes it -
     * the same payload the other fixtures here are a shortened, bilingual version of.
     */
    @Test
    public void votingOfASingleLanguageConsultation() throws IOException {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        stompConnection.connectAndWait("installation", "site", "con", "login-5", null);
        stompConnection.subscribe("/user/installation/site/con/login-5/voting/de");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_voting3_in.json", VOTING_ROUTING_KEY, "de");
        testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_voting3_user_out.json");
    }

    /**
     * Subscribing to the administration's view of a voting takes the privilege to administer votings,
     * which Antragsgrün states in the token it issues.
     */
    @Test
    public void tryToSubscribeAsAdminWithoutTheRole() {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        stompConnection.connectAndWait("installation", "site", "con", "login-1", getRoles("ROLE_SPEECH_ADMIN"));
        FutureTask<String> onError = stompConnection.subscribeAndExpectError("/admin/installation/site/con/login-1/voting/de");
        try {
            assertThat(onError.get(5, TimeUnit.SECONDS)).isEqualTo("Forbidden to subscribe to this destination");
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A cast vote changes the counting and nothing else, so it is published as a tally: an event
     * carrying only those fields, marked as describing part of a voting rather than all of it.
     */
    @Test
    public void votingTallyIsDeliveredAsAPartialUpdate() throws IOException {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        stompConnection.connectAndWait("installation", "site", "con", "login-2", null);
        stompConnection.subscribe("/user/installation/site/con/login-2/voting/de");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_voting2_in.json", VOTING_ROUTING_KEY, "de");
        testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_voting2_user_out.json");
    }

    /**
     * A tally is scoped like any other event: the counting an administrator sees can differ from the
     * one everybody sees, and it is the administration's copy that reaches them.
     */
    @Test
    public void votingTallyIsScopedForTheAdministration() throws IOException {
        StompTestConnection stompConnection = testHelper.getStompConnection(port);

        stompConnection.connectAndWait("installation", "site", "con", "login-1", getRoles("ROLE_VOTING_ADMIN"));
        stompConnection.subscribe("/admin/installation/site/con/login-1/voting/de");

        testHelper.sendFileContentToRabbitMQ("sendAndConvertRabbitMQMessage_voting2_in.json", VOTING_ROUTING_KEY, "de");
        testHelper.expectStompToSendFileContent(stompConnection, "sendAndConvertRabbitMQMessage_voting2_admin_out.json");
    }
}
