package org.sports.exercise.battle.application.services;

import org.sports.exercise.battle.application.common.DTOConverter;
import org.sports.exercise.battle.application.responses.LoginResponse;
import org.sports.exercise.battle.application.responses.dtos.UserDTO;
import org.sports.exercise.battle.application.ports.UserRepository;
import org.sports.exercise.battle.application.requests.LogInUserRequest;
import org.sports.exercise.battle.application.requests.RegisterUserRequest;
import org.sports.exercise.battle.application.requests.UpdateUserProfileRequest;
import org.sports.exercise.battle.core.entities.User;
import org.sports.exercise.battle.core.exceptions.InvalidCredentialsException;
import org.sports.exercise.battle.core.exceptions.UserAlreadyExistsException;
import org.sports.exercise.battle.infrastructure.security.PasswordHasher;
import org.sports.exercise.battle.server.exceptions.UnauthorizedException;

import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository;
    private final DTOConverter<User, UserDTO> dtoConverter;

    public UserService(UserRepository userRepository, DTOConverter<User, UserDTO> dtoConverter){
        this.userRepository = userRepository;
        this.dtoConverter = dtoConverter;
    }

    public UserDTO register(RegisterUserRequest request){
        Optional<User> optionalUser = userRepository.findByUsername(request.username());

        if(optionalUser.isPresent()){
            throw new UserAlreadyExistsException(request.username());
        }

        String passwordHash;

        try{
            passwordHash = PasswordHasher.hashPassword(request.password().toCharArray());

        } catch (Exception e) {
            throw new RuntimeException("Hashing Password failed during Registration", e);
        }

        User user = new User(
                UUID.randomUUID(),
                request.username(),
                passwordHash
        );

        userRepository.save(user);

        return dtoConverter.toDTO(user);
    }

    public LoginResponse login(LogInUserRequest request) {
        User user = userRepository.findByUsernameOrThrowNotFound(request.username());

        //verify password
        boolean validPassword = PasswordHasher.verifyPassword(request.password().toCharArray(), user.getPasswordHash());

        if (!validPassword) {
            throw new InvalidCredentialsException();
        }

        //fake token - as specified in the script curl from the lecture
        String token = user.getUsername() + "-sebToken";

        return new LoginResponse(token);
    }

    public UserDTO updateProfile(UpdateUserProfileRequest request, String authenticatedUsername, String requestedUsername){

        if(!authenticatedUsername.equals(requestedUsername)){
            throw new UnauthorizedException("Invalid request and token");
        }

        User user = userRepository.findByUsernameOrThrowNotFound(requestedUsername);

        //update the user profile
        user.setBio(request.bio());
        user.setImage(request.image());
        user.setName(request.name());

        userRepository.update(user);

        return dtoConverter.toDTO(user);
    }

    public UserDTO getUser(String authenticatedUsername, String requestedUsername){

        if(!authenticatedUsername.equals(requestedUsername)){
            throw new UnauthorizedException("Invalid request and token");
        }

        User user = userRepository.findByUsernameOrThrowNotFound(requestedUsername);

        return dtoConverter.toDTO(user);
    }
}
