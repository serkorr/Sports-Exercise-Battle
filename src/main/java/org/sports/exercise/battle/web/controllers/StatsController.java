package org.sports.exercise.battle.web.controllers;

import org.sports.exercise.battle.application.responses.UserStatsResponse;
import org.sports.exercise.battle.application.services.StatsService;
import org.sports.exercise.battle.infrastructure.database.DatabaseConfig;
import org.sports.exercise.battle.server.auth.AuthService;
import org.sports.exercise.battle.server.messages.HttpRequest;
import org.sports.exercise.battle.server.messages.HttpResponse;
import org.sports.exercise.battle.server.messages.JsonMessageMarshaller;
import org.sports.exercise.battle.web.common.ServiceFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class StatsController {
    private final JsonMessageMarshaller marshaller;
    private final Logger logger = Logger.getLogger(StatsController.class.getName());
    private final ServiceFactory serviceFactory;
    private final AuthService authService;

    public StatsController(ServiceFactory serviceFactory, JsonMessageMarshaller marshaller, AuthService authService){
        this.marshaller = marshaller;
        this.serviceFactory = serviceFactory;
        this.authService = authService;
    }

    public HttpResponse getUserStats(HttpRequest request){
        try(Connection connection = DatabaseConfig.getConnection()){
            StatsService statsService = serviceFactory.createStatsService(connection);

            String authenticatedName = authService.getAuthenticatedUsername(request);

            UserStatsResponse response = statsService.getUserStats(authenticatedName);

            String jsonBody = marshaller.marshalToJSON(response);

            return HttpResponse.ok(jsonBody);

        }catch(SQLException e){
            logger.severe("Fetching user stats failed");
            throw new RuntimeException("Error during fetching user stats", e);
        }
    }
}
