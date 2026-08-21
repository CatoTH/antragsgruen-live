package de.antragsgruen.live.websocket.dto;

import lombok.Getter;
import org.springframework.lang.Nullable;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WSDebateItem(
        @Getter Integer id,
        @Getter String targetType,
        @Getter Integer targetId,
        @Getter String title,
        @Getter String dateStarted,
        @Getter @Nullable String titleWithPrefix,
        @Getter @Nullable String initiatorsHtml,
        @Getter @Nullable String urlJson,
        @Getter @Nullable String urlHtml,
        @Getter @Nullable WSDebateItemSpeechQueue speechQueue,
        @Getter @Nullable WSDebateItemVotingBlock votingBlock
) {
}
