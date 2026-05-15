package org.sports.exercise.battle.application.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.sports.exercise.battle.application.responses.dtos.PushUpHistoryEntry;

import java.util.List;

public record PushUpHistoryResponse (
        @JsonProperty("History")
        List<PushUpHistoryEntry> history
){}
