package org.sports.exercise.battle.application.responses.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

//Why User DTO, we dont want to leak unecessary information
//if we would return the whole entity password hash gets leaked back to the client
//security vulnerability
public record UserDTO(
        @JsonProperty("Username") String username,
        @JsonProperty("Name") String name,
        @JsonProperty("Bio") String bio,
        @JsonProperty("Image") String image,
        @JsonProperty("Elo") int elo
) {
}