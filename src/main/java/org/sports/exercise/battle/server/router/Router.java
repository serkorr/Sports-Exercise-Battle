package org.sports.exercise.battle.server.router;

import org.sports.exercise.battle.server.messages.HttpRequest;
import org.sports.exercise.battle.server.messages.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private final Map<RouteKey, RouteHandler> routes = new HashMap<>();

    public void get(String path, RouteHandler handler){
        routes.put(new RouteKey("GET", path), handler);
    }

    public void post(String path, RouteHandler handler){
        routes.put(new RouteKey("POST", path), handler);
    }

    public void delete(String path, RouteHandler handler){
        routes.put(new RouteKey("DELETE", path), handler);
    }

    public void put(String path, RouteHandler handler){
        routes.put(new RouteKey("PUT", path), handler);
    }

    // Finds the matching route for the request method and path
    public HttpResponse route(HttpRequest httpRequest){
        String method = httpRequest.method().toUpperCase();
        String path = httpRequest.path();

        //looks for the registered handler for this method and path combination
        RouteHandler handler = routes.get(new RouteKey(method, path));

        if(handler == null){
            return HttpResponse.notFound("{\"Error\":\"Route not found\"}");
        }

        //delegate the request to the corresponding controller endpoint
        return handler.handle(httpRequest);
    }
}
