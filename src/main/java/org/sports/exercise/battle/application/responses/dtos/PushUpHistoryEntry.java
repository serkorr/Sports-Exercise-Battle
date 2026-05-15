package org.sports.exercise.battle.application.responses.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PushUpHistoryEntry(
        @JsonProperty("Date")
        String date,
        @JsonProperty("Count")
        int count,
        @JsonProperty("DurationInSeconds")
        int durationInSeconds
) {
}
