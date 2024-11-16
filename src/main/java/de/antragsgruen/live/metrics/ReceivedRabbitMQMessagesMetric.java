package de.antragsgruen.live.metrics;

import de.antragsgruen.live.multisite.ConsultationScope;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceivedRabbitMQMessagesMetric {
    private final MeterRegistry registry;

    private static final String METRIC_NAME = "antragsgruen_live.rabbitmq.msg_count";

    private static final String TYPE_SPEECH = "speech";
    private static final String TYPE_USER = "user";

    public void onSpeechEvent(ConsultationScope scope) {
        this.receivedMessage(scope, TYPE_SPEECH);
    }

    public void onUserEvent(ConsultationScope scope) {
        this.receivedMessage(scope, TYPE_USER);
    }

    private void receivedMessage(ConsultationScope scope, String type) {
        log.debug("Logging RabbitMQ message count: " + scope + " - " + type);

        registry.counter(METRIC_NAME, List.of(
                Tag.of("installation", scope.installation()),
                Tag.of("site", scope.site()),
                Tag.of("consultation", scope.consultation()),
                Tag.of("type", type)
        )).increment();
    }
}
