package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MQDebateItem(
        Integer id,
        String targetType,
        Integer targetId,
        String title,
        String dateStarted,
        @Nullable String titleWithPrefix,
        @Nullable String initiatorsHtml,
        @Nullable String urlJson,
        @Nullable String urlHtml,
        @Nullable MQDebateItemSpeechQueue speechQueue,
        @Nullable MQDebateItemVotingBlock votingBlock
) {
    @JsonCreator
    public MQDebateItem {
    }
}
