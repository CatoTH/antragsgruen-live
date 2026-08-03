package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;

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
        @Nullable Integer speechQueueId,
        @Nullable Integer votingBlockId
) {
    @JsonCreator
    public MQDebateItem(
            Integer id,
            String targetType,
            Integer targetId,
            String title,
            String dateStarted,
            @Nullable String titleWithPrefix,
            @Nullable String initiatorsHtml,
            @Nullable String urlJson,
            @Nullable String urlHtml,
            @Nullable Integer speechQueueId,
            @Nullable Integer votingBlockId
    ) {
        this.id = id;
        this.targetType = targetType;
        this.targetId = targetId;
        this.title = title;
        this.dateStarted = dateStarted;
        this.titleWithPrefix = titleWithPrefix;
        this.initiatorsHtml = initiatorsHtml;
        this.urlJson = urlJson;
        this.urlHtml = urlHtml;
        this.speechQueueId = speechQueueId;
        this.votingBlockId = votingBlockId;
    }
}
