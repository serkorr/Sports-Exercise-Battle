package org.sports.exercise.battle.application.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterUserRequest(
        @JsonProperty("Username")
        String username,
        @JsonProperty("Password")
        String password
) { }
