package org.sports.exercise.battle.application.responses.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TournamentParticipant(
        @JsonProperty("Name")
        String username,

        @JsonProperty("TotalPushUps")
        int totalPushUps
) { }
