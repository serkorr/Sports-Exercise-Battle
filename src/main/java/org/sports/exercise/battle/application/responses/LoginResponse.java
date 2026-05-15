package org.sports.exercise.battle.application.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(@JsonProperty("Token") String token) {
}
