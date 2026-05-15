package org.sports.exercise.battle.application.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LogInUserRequest(
        @JsonProperty("Username")
        String username,
        @JsonProperty("Password")
        String password
) { }
