package org.sports.exercise.battle.application.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.sports.exercise.battle.application.responses.dtos.TournamentParticipant;
import org.sports.exercise.battle.core.entities.Tournament;

import java.util.List;

public record TournamentResponse(
        @JsonProperty("Id")
        String id,

        @JsonProperty("Status")
        String status,

        @JsonProperty("StartedAt")
        String startedAt,

        @JsonProperty("EndedAt")
        String endedAt,

        @JsonProperty("Participants")
        List<TournamentParticipant> participants
) {
}
