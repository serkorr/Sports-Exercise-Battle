package org.sports.exercise.battle.web.controllers;

import org.sports.exercise.battle.server.auth.AuthService;
import org.sports.exercise.battle.server.messages.JsonMessageMarshaller;
import org.sports.exercise.battle.web.common.ServiceFactory;

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


}
