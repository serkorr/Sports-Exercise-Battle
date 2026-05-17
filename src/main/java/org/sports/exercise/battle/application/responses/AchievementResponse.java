package org.sports.exercise.battle.application.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.sports.exercise.battle.application.responses.dtos.AchievementEntry;

import java.util.List;

public record AchievementResponse (
        @JsonProperty("Achievements")
        List<AchievementEntry> achievements
){
}
