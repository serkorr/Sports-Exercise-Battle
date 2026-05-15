package org.sports.exercise.battle.application.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.sports.exercise.battle.application.responses.dtos.UserRanking;

import java.util.List;

public record ScoreboardResponse(
        @JsonProperty("Scoreboard")
        List<UserRanking> scoreboard
) {
}