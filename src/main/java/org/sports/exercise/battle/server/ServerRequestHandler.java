package org.sports.exercise.battle.server;

import org.sports.exercise.battle.server.exceptions.BadRequestException;
import org.sports.exercise.battle.server.exceptions.UnauthorizedException;
import org.sports.exercise.battle.server.messages.HttpRequest;
import org.sports.exercise.battle.server.messages.HttpResponse;
import org.sports.exercise.battle.server.router.Router;

import java.util.logging.Logger;

//global request handling for

public class ServerRequestHandler {
    private final Router router;
    private final Logger logger = Logger.getLogger(ServerRequestHandler.class.getName());

    public ServerRequestHandler(Router router) {
        this.router = router;
    }

    public HttpResponse handle(HttpRequest httpRequest){
        try{
            return router.route(httpRequest);
        }catch(BadRequestException e){
            return HttpResponse.badRequest("{\"error\":\"" + e.getMessage() + "\"}");
        }catch(UnauthorizedException e){
            return HttpResponse.unauthorized("{\"error\":\"" + e.getMessage() + "\"}");
        }catch(Exception e){
            return HttpResponse.internalServerError("{\"error\":\"Internal Server Error\"}");
        }
    }
}
