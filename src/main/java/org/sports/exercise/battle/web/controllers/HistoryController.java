package org.sports.exercise.battle.web.controllers;

import org.sports.exercise.battle.application.requests.AddPushUpRecordRequest;
import org.sports.exercise.battle.application.responses.PushUpHistoryResponse;
import org.sports.exercise.battle.application.responses.dtos.PushUpRecordDTO;
import org.sports.exercise.battle.application.services.HistoryService;
import org.sports.exercise.battle.application.services.TournamentService;
import org.sports.exercise.battle.core.entities.Tournament;
import org.sports.exercise.battle.infrastructure.database.DatabaseConfig;
import org.sports.exercise.battle.server.auth.AuthService;
import org.sports.exercise.battle.server.messages.HttpRequest;
import org.sports.exercise.battle.server.messages.HttpResponse;
import org.sports.exercise.battle.server.messages.JsonMessageMarshaller;
import org.sports.exercise.battle.web.common.ServiceFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class HistoryController {
    private final Logger logger = Logger.getLogger(HistoryController.class.getName());
    private final ServiceFactory serviceFactory;
    private final JsonMessageMarshaller marshaller;
    private final AuthService authService;

    public HistoryController(ServiceFactory serviceFactory, JsonMessageMarshaller marshaller, AuthService authService){
        this.authService = authService;
        this.marshaller = marshaller;
        this.serviceFactory = serviceFactory;
    }

    public HttpResponse addPushUpRecord(HttpRequest request){
        try(Connection connection = DatabaseConfig.getConnection()){

            HistoryService historyService = serviceFactory.createHistoryService(connection);
            TournamentService tournamentService = serviceFactory.createTournamentService(connection);

            String authenticatedName = authService.getAuthenticatedUsername(request);

            Tournament tournament = tournamentService.getOrCreateRunningTournament();

            AddPushUpRecordRequest addPushUpRecordRequest = marshaller.unmarshalFromJSON(request.body(), AddPushUpRecordRequest.class);

            PushUpRecordDTO response = historyService.addPushUpRecord(addPushUpRecordRequest, authenticatedName, tournament);

            String jsonBody = marshaller.marshalToJSON(response);

            return HttpResponse.ok(jsonBody);

        }catch(SQLException e){
            logger.severe("Inserting push up record into DB failed");
            throw new RuntimeException("Error during inserting push up up record DB");        }
    }

    public HttpResponse getUserPushUpHistory(HttpRequest request){
        try(Connection connection = DatabaseConfig.getConnection()){

            HistoryService historyService = serviceFactory.createHistoryService(connection);

            String authenticatedName = authService.getAuthenticatedUsername(request);

            PushUpHistoryResponse response = historyService.getUserPushUpHistory(authenticatedName);

            String jsonBody = marshaller.marshalToJSON(response);

            return HttpResponse.ok(jsonBody);

        }catch(SQLException e){
            logger.severe("Fetching User PushUp History from DB failed");
            throw new RuntimeException("Error during fetching User PushUp history from DB");
        }
    }
}
