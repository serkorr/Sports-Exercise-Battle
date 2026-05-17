package org.sports.exercise.battle.web.controllers;

import org.sports.exercise.battle.application.responses.AchievementResponse;
import org.sports.exercise.battle.application.services.AchievementService;
import org.sports.exercise.battle.infrastructure.database.DatabaseConfig;
import org.sports.exercise.battle.server.auth.AuthService;
import org.sports.exercise.battle.server.messages.HttpRequest;
import org.sports.exercise.battle.server.messages.HttpResponse;
import org.sports.exercise.battle.server.messages.JsonMessageMarshaller;
import org.sports.exercise.battle.web.common.ServiceFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class AchievementController {
    private final Logger logger = Logger.getLogger(HistoryController.class.getName());
    private final ServiceFactory serviceFactory;
    private final JsonMessageMarshaller marshaller;
    private final AuthService authService;

    public AchievementController(ServiceFactory serviceFactory, JsonMessageMarshaller marshaller, AuthService authService){
        this.authService = authService;
        this.marshaller = marshaller;
        this.serviceFactory = serviceFactory;
    }

    public HttpResponse getAchievement(HttpRequest httpRequest){
        try (Connection connection = DatabaseConfig.getConnection()) {
            String authenticatedUsername = authService.getAuthenticatedUsername(httpRequest);

            AchievementService achievementService =
                    serviceFactory.createAchievementService(connection);

            AchievementResponse response =
                    achievementService.getAchievements(authenticatedUsername);

            return HttpResponse.ok(marshaller.marshalToJSON(response));

        } catch (SQLException e) {
            throw new RuntimeException("Database error during fetching achievements", e);
        }
    }
}
