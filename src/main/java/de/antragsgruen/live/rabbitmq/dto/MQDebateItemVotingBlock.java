package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MQDebateItemVotingBlock(
        Integer id,
        Integer status,
        String title
) {
    @JsonCreator
    public MQDebateItemVotingBlock {
    }
}
