package org.sports.exercise.battle.application.responses.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AchievementEntry (
        @JsonProperty("Name")
        String name,
        @JsonProperty("Description")
        String description
){
}
