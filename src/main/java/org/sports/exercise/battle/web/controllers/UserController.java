package org.sports.exercise.battle.web.controllers;

import org.sports.exercise.battle.server.ServerRequestHandler;
import org.sports.exercise.battle.server.messages.HttpRequest;
import org.sports.exercise.battle.server.messages.HttpResponse;

import java.util.logging.Logger;

public class UserController {

    private final Logger logger = Logger.getLogger(UserController.class.getName());

    public HttpResponse register(HttpRequest request){
        //testing
        logger.info("UserController.register reached");
        logger.info("Request body: " + request.body());


        return HttpResponse.created("{\"message\":\"User registered test endpoint reached\"}");
    }

    public HttpResponse test(HttpRequest request) {
        logger.info("UserController.test reached");

        return HttpResponse.ok("{\"message\":\"UserController reached\"}");
    }
}
