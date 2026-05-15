package org.sports.exercise.battle.application.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateUserProfileRequest(
        @JsonProperty("Name")
        String name,
        @JsonProperty("Bio")
        String bio,
        @JsonProperty("Image")
        String image
) { }
