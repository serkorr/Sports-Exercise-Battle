package org.sports.exercise.battle.application.responses.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PushUpRecordDTO(
        @JsonProperty("Name")
        String name,
        @JsonProperty("Count")
        int count,
        @JsonProperty("DurationInSeconds")
        int durationInSeconds
) {
}
