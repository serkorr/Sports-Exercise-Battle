package org.sports.exercise.battle.application.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserStatsResponse(
        @JsonProperty("Elo")
        int elo,
        @JsonProperty("TotalPushUps")
        int totalPushUps
) {
}
