package org.sports.exercise.battle.web.controllers;

import org.sports.exercise.battle.application.responses.LoginResponse;
import org.sports.exercise.battle.application.responses.dtos.UserDTO;
import org.sports.exercise.battle.application.requests.LogInUserRequest;
import org.sports.exercise.battle.application.requests.RegisterUserRequest;
import org.sports.exercise.battle.application.requests.UpdateUserProfileRequest;
import org.sports.exercise.battle.application.services.UserService;
import org.sports.exercise.battle.infrastructure.database.DatabaseConfig;
import org.sports.exercise.battle.server.auth.AuthService;
import org.sports.exercise.battle.server.exceptions.HttpException;
import org.sports.exercise.battle.server.messages.HttpRequest;
import org.sports.exercise.battle.server.messages.HttpResponse;
import org.sports.exercise.battle.server.messages.JsonMessageMarshaller;
import org.sports.exercise.battle.web.common.ServiceFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import static org.sports.exercise.battle.server.router.PathHelper.extractUsernameFromPath;

public class UserController {
    private final Logger logger = Logger.getLogger(UserController.class.getName());
    private final JsonMessageMarshaller marshaller;
    private final AuthService authenticationService;
    private final ServiceFactory serviceFactory;

    public UserController(ServiceFactory serviceFactory, JsonMessageMarshaller marshaller, AuthService authenticationService){
        this.marshaller = marshaller;
        this.serviceFactory = serviceFactory;
        this.authenticationService = authenticationService;
    }

    public HttpResponse register(HttpRequest request){
        try(Connection connection = DatabaseConfig.getConnection()){
            //normally we would have UserService as an instance for UserController
            //because right now we want to avoid shared connection, create everytime the service
            UserService userService = serviceFactory.createUserService(connection);

            RegisterUserRequest registerUserRequest = marshaller.unmarshalFromJSON(request.body(), RegisterUserRequest.class);

            UserDTO userDTO = userService.register(registerUserRequest);

            logger.info("User registration successfull for user: " + userDTO.username());

            String json = marshaller.marshalToJSON(userDTO);

            return HttpResponse.created(json);

        }catch (Exception e){
            logger.severe("User registration failed");
            throw new HttpException("Error during registering");
        }
    }

    public HttpResponse login(HttpRequest request){
        try(Connection connection = DatabaseConfig.getConnection()){
            UserService userService = serviceFactory.createUserService(connection);

            LogInUserRequest loginRequest = marshaller.unmarshalFromJSON(request.body(), LogInUserRequest.class);

            LoginResponse response = userService.login(loginRequest);

            String json = marshaller.marshalToJSON(response);

            return HttpResponse.ok(json);

        }catch(Exception e){
            logger.severe("User login failed");
            throw new HttpException("Error during login");
        }
    }

    public HttpResponse getProfile(HttpRequest request){
        try(Connection connection = DatabaseConfig.getConnection()){
            UserService userService = serviceFactory.createUserService(connection);

            String authenticatedUsername = authenticationService.getAuthenticatedUsername(request);
            String requestedUsername = extractUsernameFromPath(request.path());

            UserDTO userDTO = userService.getUser(authenticatedUsername, requestedUsername);

            String json = marshaller.marshalToJSON(userDTO);

            return HttpResponse.ok(json);

        }catch(Exception e){
            logger.severe("Fetching user profile failed");
            throw new HttpException("Error fetching Profile");
        }
    }

    public HttpResponse updateProfile(HttpRequest request){
        try(Connection connection = DatabaseConfig.getConnection()){
            UserService userService = serviceFactory.createUserService(connection);

            String authenticatedUsername = authenticationService.getAuthenticatedUsername(request);
            String requestedUsername = extractUsernameFromPath(request.path());

            UpdateUserProfileRequest updateRequest = marshaller.unmarshalFromJSON(request.body(), UpdateUserProfileRequest.class);
            UserDTO userDTO = userService.updateProfile(updateRequest, authenticatedUsername, requestedUsername);

            String json = marshaller.marshalToJSON(userDTO);

            return HttpResponse.ok(json);
        }catch (SQLException e) {
            throw new RuntimeException("Database error during profile update", e);
        }
    }
}
