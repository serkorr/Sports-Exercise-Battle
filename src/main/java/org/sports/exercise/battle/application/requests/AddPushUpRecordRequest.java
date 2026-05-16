package org.sports.exercise.battle.application.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddPushUpRecordRequest(
        @JsonProperty("Name")
        String name,
        @JsonProperty("Count")
        int count,
        @JsonProperty("DurationInSeconds")
        int durationInSeconds
) {
}
