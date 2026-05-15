package org.sports.exercise.battle.application.responses.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRanking(
        @JsonProperty("Placement")
        int placement,
        @JsonProperty("Username")
        String username,
        @JsonProperty("Elo")
        int elo,
        @JsonProperty("TotalPushUps")
        int totalPushUps){}
