package org.sports.exercise.battle.application.common;

import org.sports.exercise.battle.application.responses.dtos.UserDTO;
import org.sports.exercise.battle.core.entities.User;

public class UserDTOConverter implements DTOConverter<User, UserDTO>{

    @Override
    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getName(),
                user.getBio(),
                user.getImage(),
                user.getElo()
        );
    }
}
