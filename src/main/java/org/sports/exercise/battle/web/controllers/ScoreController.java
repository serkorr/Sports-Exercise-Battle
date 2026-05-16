package org.sports.exercise.battle.web.controllers;

import org.sports.exercise.battle.application.responses.ScoreboardResponse;
import org.sports.exercise.battle.application.services.ScoreboardService;
import org.sports.exercise.battle.infrastructure.database.DatabaseConfig;
import org.sports.exercise.battle.infrastructure.repositories.JDBCPushUpRecordRepository;
import org.sports.exercise.battle.infrastructure.repositories.JDBCUserRepository;
import org.sports.exercise.battle.server.auth.AuthService;
import org.sports.exercise.battle.server.messages.HttpRequest;
import org.sports.exercise.battle.server.messages.HttpResponse;
import org.sports.exercise.battle.server.messages.JsonMessageMarshaller;
import org.sports.exercise.battle.web.common.ServiceFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ScoreController {
    private final AuthService authService;
    private final Logger logger = Logger.getLogger(ScoreController.class.getName());
    private final JsonMessageMarshaller marshaller;
    private final ServiceFactory serviceFactory;

    public ScoreController(ServiceFactory serviceFactory, JsonMessageMarshaller marshaller, AuthService authService){
        this.authService = authService;
        this.marshaller = marshaller;
        this.serviceFactory = serviceFactory;
    }

    public HttpResponse getScoreboard(HttpRequest httpRequest){
        try(Connection connection = DatabaseConfig.getConnection()){
            ScoreboardService scoreboardService = serviceFactory.createScoreboardService(connection);
            String authenticatedUsername = authService.getAuthenticatedUsername(httpRequest);

            ScoreboardResponse response = scoreboardService.getScoreboard(authenticatedUsername);

            String jsonBody = marshaller.marshalToJSON(response);

            return HttpResponse.ok(jsonBody);

        }catch(SQLException e){
            logger.severe("Fetching Scoreboard from DB failed");
            throw new RuntimeException("Database error during fetching scoreboard", e);
        }
    }
}
