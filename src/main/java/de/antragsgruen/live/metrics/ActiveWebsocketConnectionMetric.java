package de.antragsgruen.live.metrics;

import de.antragsgruen.live.multisite.ConsultationScope;
import de.antragsgruen.live.websocket.TopicPermissionChecker;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActiveWebsocketConnectionMetric {
    private final SimpUserRegistry userRegistry;
    private final MeterRegistry registry;

    private static final String METRIC_NAME = "antragsgruen_live.ws.active_connections";

    @Scheduled(fixedRateString = "${antragsgruen.metrics.interval.ms}")
    public void processGauge() {
        userRegistry.findSubscriptions(subscription -> true)
                .stream()
                .map(subscription -> TopicPermissionChecker.consultationScopeFromPathParts(subscription.getDestination().split("/")))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach(this::trackMetric);
    }

    private void trackMetric(ConsultationScope scope, Long subscribers) {
        log.debug("Logging active websocket metrics: " + scope + " - " + subscribers);

        registry.gauge(
                METRIC_NAME,
                List.of(
                        Tag.of("installation", scope.installation()),
                        Tag.of("site", scope.site()),
                        Tag.of("consultation", scope.consultation())
                ),
                subscribers
        );
    }
}
