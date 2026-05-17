package org.sports.exercise.battle.web.controllers;

import org.sports.exercise.battle.application.responses.TournamentResponse;
import org.sports.exercise.battle.application.services.TournamentService;
import org.sports.exercise.battle.infrastructure.database.DatabaseConfig;
import org.sports.exercise.battle.server.auth.AuthService;
import org.sports.exercise.battle.server.messages.HttpRequest;
import org.sports.exercise.battle.server.messages.HttpResponse;
import org.sports.exercise.battle.server.messages.JsonMessageMarshaller;
import org.sports.exercise.battle.web.common.ServiceFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class TournamentController {
    private final JsonMessageMarshaller marshaller;
    private final Logger logger = Logger.getLogger(TournamentController.class.getName());
    private final ServiceFactory serviceFactory;
    private final AuthService authService;

    public TournamentController(ServiceFactory serviceFactory, JsonMessageMarshaller marshaller, AuthService authService){
        this.marshaller = marshaller;
        this.serviceFactory = serviceFactory;
        this.authService = authService;
    }

    public HttpResponse getTournamentInformation(HttpRequest request){
        try(Connection connection = DatabaseConfig.getConnection()){
            TournamentService tournamentService = serviceFactory.createTournamentService(connection);

            String authenticatedName = authService.getAuthenticatedUsername(request);

            TournamentResponse response = tournamentService.getTournamentInfo(authenticatedName);

            String jsonBody = marshaller.marshalToJSON(response);

            return HttpResponse.ok(jsonBody);

        }catch(SQLException e){
            logger.severe("Failed to get tournament info");
            throw new RuntimeException("Error during fetching tournament info");
        }
    }
}
